package uz.nt.simplechatproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.nt.simplechatproject.dto.ChatDTO;
import uz.nt.simplechatproject.dto.ResponseDTO;
import uz.nt.simplechatproject.dto.ValidatorDTO;
import uz.nt.simplechatproject.dto.custom.ChatCustomDTO;
import uz.nt.simplechatproject.helper.AppCode;
import uz.nt.simplechatproject.helper.AppMessages;
import uz.nt.simplechatproject.mapper.ChatMapper;
import uz.nt.simplechatproject.mapper.UserMapper;
import uz.nt.simplechatproject.mapper.custom.ChatCustomMapper;
import uz.nt.simplechatproject.model.Chat;
import uz.nt.simplechatproject.model.ChatUsers;
import uz.nt.simplechatproject.model.User;
import uz.nt.simplechatproject.repository.ChatRepository;
import uz.nt.simplechatproject.repository.ChatUsersRepository;
import uz.nt.simplechatproject.repository.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static uz.nt.simplechatproject.helper.AppCode.OK;
import static uz.nt.simplechatproject.helper.AppCode.VALIDATOR_ERROR;
import static uz.nt.simplechatproject.helper.AppMessages.VALIDATOR_MESSAGE;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ValidatorService validatorService;
    private final ChatRepository chatRepository;
    private final ChatUsersRepository chatUsersRepository;
    private final ChatMapper chatMapper;
    private final ChatCustomMapper chatCustomMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public ResponseDTO<Integer> addChat(ChatDTO chatDTO){
        List<User> users = userRepository.findAllById(chatDTO.getUserIds());

        List<ValidatorDTO> errors = validatorService.validateChat(chatDTO, users);
        if (!errors.isEmpty())
            return new ResponseDTO<>(false, VALIDATOR_ERROR, VALIDATOR_MESSAGE, null, errors);


        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Chat chat = chatMapper.toEntity(chatDTO);
        chat.setUsers(users);
        chat.setCreatedAt(new Date());
        chat.setAuthorId(currentUser.getId());

        chatRepository.save(chat);

        return new ResponseDTO<>(true, OK, AppMessages.OK, chat.getId());
    }

    public ResponseDTO<String> addMember(Integer chatId, Integer userId) {
        List<ValidatorDTO> errors = validatorService.checkUserAndChat(chatId, userId);
        if (!errors.isEmpty())
            return new ResponseDTO<>(false, VALIDATOR_ERROR, VALIDATOR_MESSAGE, null, errors);

        ChatUsers chatUsers = new ChatUsers();
        chatUsers.setChatId(chatId);
        chatUsers.setUserId(userId);

        chatUsersRepository.save(chatUsers);

        return new ResponseDTO<>(true, OK, AppMessages.OK, AppMessages.SAVED);
    }

    public ResponseDTO<List<ChatCustomDTO>> getAll() {
        List<Chat> chats = chatRepository.findAll();

        List<ChatCustomDTO> chatDTOS = chats.stream()
                .map(chatCustomMapper::toDto)
                .collect(Collectors.toList());

        return new ResponseDTO<>(true, OK, AppMessages.OK, chatDTOS);
    }

    public ResponseDTO<ChatDTO> getChatById(Integer chatId) {
        Optional<Chat> chat = chatRepository.findById(chatId);

        if (chat.isEmpty())
            return new ResponseDTO<>(false, AppCode.NOT_FOUND, AppMessages.NOT_FOUND, null);

        ChatDTO chatDTO = chatMapper.toDto(chat.get());
        List<Integer> users = chat.get().getUsers().stream()
                .map(User::getId)
                .collect(Collectors.toList());
        chatDTO.setUserIds(users);

        return new ResponseDTO<>(true, OK, AppMessages.OK, chatDTO);
    }

    public ResponseDTO<List<ChatCustomDTO>> getChatsByUserId(Integer userId) {
        List<ChatUsers> chatUsers = chatUsersRepository.findAllByUserId(userId);

        List<ChatCustomDTO> chats = chatRepository.findAllByIdInOrderByCreatedAt(chatUsers.stream()
                        .map(ChatUsers::getChatId)
                        .collect(Collectors.toList()))
                .stream()
                .map(chatCustomMapper::toDto)
                .collect(Collectors.toList());

        return new ResponseDTO<>(true, OK, AppMessages.OK, chats);
    }
}
