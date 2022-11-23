package uz.nt.simplechatproject.mapper.custom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.nt.simplechatproject.dto.custom.MessageCustomDTO;
import uz.nt.simplechatproject.mapper.MessageMapper;
import uz.nt.simplechatproject.model.Chat;
import uz.nt.simplechatproject.model.Message;
import uz.nt.simplechatproject.model.User;
import uz.nt.simplechatproject.repository.ChatRepository;
import uz.nt.simplechatproject.repository.UserRepository;

import java.util.Optional;
@Component
@RequiredArgsConstructor
public class MessageCustomMapper {
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final MessageMapper messageMapper;

    public MessageCustomDTO toDto(Message message){
        MessageCustomDTO dto = messageMapper.toCustomDto(message);
        Optional<User> user = userRepository.findById(message.getAuthorId());
        user.ifPresentOrElse(u -> dto.setUserName(String.format("%s %s", u.getFirstName(), u.getLastName())),
                () -> dto.setUserName("DELETED ACCOUNT"));

        Optional<Chat> chat = chatRepository.findById(message.getChatId());
        chat.ifPresentOrElse(ch -> dto.setChatName(ch.getName()),
                ()-> dto.setChatName("DELETED CHAT"));

        return dto;
    }
}
