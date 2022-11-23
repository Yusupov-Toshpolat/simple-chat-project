package uz.nt.simplechatproject.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.nt.simplechatproject.dto.ChatDTO;
import uz.nt.simplechatproject.dto.ResponseDTO;
import uz.nt.simplechatproject.dto.custom.ChatCustomDTO;
import uz.nt.simplechatproject.service.ChatService;

import java.util.List;

@RestController
@RequestMapping("chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @ApiOperation(value = "Add new chat", nickname = "add chat")
    @PostMapping("/add")
    public ResponseDTO<Integer> addChat(@RequestBody ChatDTO chatDTO){
        return chatService.addChat(chatDTO);
    }

    @ApiOperation(value = "Add new member to the chat. You need to give chatId and userId", nickname = "add member")
    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR', 'CHAT')")
    @PostMapping("/add-member")
    public ResponseDTO<String> addMember(@RequestParam Integer chatId, @RequestParam Integer userId){
        return chatService.addMember(chatId, userId);
    }

    @ApiOperation(value = "List of all chats. Returns chat's author too.")
    @GetMapping
    public ResponseDTO<List<ChatCustomDTO>> allChats(){
        return chatService.getAll();
    }

    @ApiOperation(value = "Chat info with list of user IDs who are members of the chat")
    @GetMapping("/{chatId}")
    public ResponseDTO<ChatDTO> getChatById(@PathVariable Integer chatId){
        return chatService.getChatById(chatId);
    }

    @ApiOperation(value = "Get all chats with user ID")
    @GetMapping("/get")
    public ResponseDTO<List<ChatCustomDTO>> chatsByUserId(@RequestParam Integer userId){
        return chatService.getChatsByUserId(userId);
    }
}
