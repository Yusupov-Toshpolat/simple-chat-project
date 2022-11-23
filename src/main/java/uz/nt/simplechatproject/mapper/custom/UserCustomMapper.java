package uz.nt.simplechatproject.mapper.custom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.nt.simplechatproject.dto.custom.UserCustomDTO;
import uz.nt.simplechatproject.mapper.UserMapper;
import uz.nt.simplechatproject.model.ChatUsers;
import uz.nt.simplechatproject.model.User;
import uz.nt.simplechatproject.repository.ChatRepository;
import uz.nt.simplechatproject.repository.ChatUsersRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserCustomMapper {
    private final UserMapper userMapper;
    private final ChatCustomMapper chatMapper;
    private final ChatUsersRepository chatUsersRepository;
    private final ChatRepository chatRepository;

    public UserCustomDTO toDto(User user){
        UserCustomDTO userCustomDTO = userMapper.toCustomDTO(user);

        List<Integer> chatIds = chatUsersRepository.findAllByUserId(user.getId())
                .stream()
                .map(ChatUsers::getChatId)
                .collect(Collectors.toList());

        userCustomDTO.setChats(
                chatRepository.findAllByIdInOrderByCreatedAt(chatIds).stream()
                        .map(chatMapper::toDto)
                        .collect(Collectors.toList())
        );

        return userCustomDTO;
    }
}
