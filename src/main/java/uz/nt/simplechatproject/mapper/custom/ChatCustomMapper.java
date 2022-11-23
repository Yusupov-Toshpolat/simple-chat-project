package uz.nt.simplechatproject.mapper.custom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.nt.simplechatproject.dto.custom.ChatCustomDTO;
import uz.nt.simplechatproject.mapper.ChatMapper;
import uz.nt.simplechatproject.model.Chat;
import uz.nt.simplechatproject.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class ChatCustomMapper {
    private final ChatMapper chatMapper;
    private final UserRepository userRepository;

    public ChatCustomDTO toDto(Chat chat){
        ChatCustomDTO dto = chatMapper.toCustomDto(chat);

        userRepository.findById(chat.getAuthorId()).ifPresentOrElse(
                u -> dto.setAuthor(String.format("%s %s",u.getFirstName(), u.getLastName())),
                () -> dto.setAuthor("DELETED ACCOUNT")
        );

        return dto;
    }
}
