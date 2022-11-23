package uz.nt.simplechatproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import uz.nt.simplechatproject.dto.ResponseDTO;
import uz.nt.simplechatproject.dto.UserDTO;
import uz.nt.simplechatproject.dto.ValidatorDTO;
import uz.nt.simplechatproject.dto.custom.UserCustomDTO;
import uz.nt.simplechatproject.helper.AppCode;
import uz.nt.simplechatproject.helper.AppMessages;
import uz.nt.simplechatproject.helper.StringHelper;
import uz.nt.simplechatproject.mapper.UserMapper;
import uz.nt.simplechatproject.mapper.custom.UserCustomMapper;
import uz.nt.simplechatproject.model.Authorities;
import uz.nt.simplechatproject.model.User;
import uz.nt.simplechatproject.repository.AuthorityRepository;
import uz.nt.simplechatproject.repository.ChatRepository;
import uz.nt.simplechatproject.repository.UserRepository;
import uz.nt.simplechatproject.repository.session.UserSession;
import uz.nt.simplechatproject.repository.session.UserSessionRedisRepository;
import uz.nt.simplechatproject.security.jwt.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static uz.nt.simplechatproject.helper.AppCode.OK;
import static uz.nt.simplechatproject.helper.AppCode.VALIDATOR_ERROR;
import static uz.nt.simplechatproject.helper.AppMessages.*;
import static uz.nt.simplechatproject.security.roles.UserPermissions.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ValidatorService validator;
    private final UserMapper userMapper;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserSessionRedisRepository userSessionRedisRepository;
    private final JwtUtil jwtUtil;
    private final ChatRepository chatRepository;
    private final UserCustomMapper userCustomMapper;

    public ResponseDTO<?> addUser(UserDTO userDTO) {
        List<ValidatorDTO> errors = validator.validateUser(userDTO);
        if (!errors.isEmpty())
            return new ResponseDTO<>(
                    false,
                    VALIDATOR_ERROR,
                    VALIDATOR_MESSAGE,
                    userDTO,
                    errors);

        //get authorities as user role default
        Set<Authorities> authorities = authorityRepository
                .findAllByAuthorityIn(Set.of(
                        READ.getName(),
                        POST.getName(),
                        DELETE.getName(),
                        EDIT.getName()
                ));

        String salt = StringHelper.generateSalt();
        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword() + salt));
        user.setAuthorities(authorities);
        user.setSalt(salt);

        userRepository.save(user);

        return new ResponseDTO<>(true, OK, SAVED, user.getId());
    }

    public ResponseDTO<String> generateJWT(UserDTO userDTO, HttpServletRequest request) {
        //check if username is exists in DB
        Optional<User> _user = userRepository.findFirstByUsername(userDTO.getUsername());
        if (_user.isEmpty()){
            return new ResponseDTO<>(false, AppCode.NOT_FOUND, AppMessages.NOT_FOUND, "username");
        }
        User user = _user.get();
        if (!passwordEncoder.matches(userDTO.getPassword() + user.getSalt(), user.getPassword())) {
            return new ResponseDTO<>(false, VALIDATOR_ERROR, MISMATCH, "password");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );
        authenticationToken.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        String uuid = sysGuid();
        String userString = StringHelper.toJson(user);
        UserSession userSessionForCaching = UserSession
                .builder()
                .id(uuid)
                .userInfo(userString)
                .build();
        userSessionRedisRepository.save(userSessionForCaching);
        String token = jwtUtil.generateToken(uuid);
        return new ResponseDTO<>(true, OK, AppMessages.OK, token);
    }

    private String sysGuid(){
        return UUID.randomUUID().toString().toUpperCase().replace("-","");
    }

    public ResponseDTO<UserDTO> getById(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()){
            return new ResponseDTO<>(false, AppCode.NOT_FOUND, AppMessages.NOT_FOUND, null);
        }
        UserCustomDTO userDTO = userCustomMapper.toDto(user.get());

        return new ResponseDTO<>(true, OK, AppMessages.OK, userDTO);
    }
}
