package uz.nt.simplechatproject.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import uz.nt.simplechatproject.dto.MessageDTO;
import uz.nt.simplechatproject.dto.ResponseDTO;
import uz.nt.simplechatproject.dto.custom.MessageCustomDTO;
import uz.nt.simplechatproject.exceptions.ImageNotFoundException;
import uz.nt.simplechatproject.service.MessageService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @ApiOperation(value = "Write message to a chat. Type of message must be either TEXT or IMAGE")
    @PostMapping
    public ResponseDTO<Integer> add(@RequestBody MessageDTO messageDTO) throws IOException {
        return messageService.add(messageDTO);
    }

    @ApiOperation(value = "Get all messages of the chat. It returns messages with author name and chat name")
    @GetMapping("/by-chat/{chatId}")
    public ResponseDTO<List<MessageCustomDTO>> getAll(@PathVariable Integer chatId){
        return messageService.getMessagesByChatId(chatId);
    }

    @GetMapping("/by-chat/{userId}")
    public ResponseEntity<String> user(@PathVariable String userId){
        return ResponseEntity.ok(userId);
    }

    @GetMapping("/image")
    public ResponseDTO<String> getImage(@RequestParam MultiValueMap<String, String> params) throws ImageNotFoundException, IOException {
        return messageService.getImage(params);
    }
}
