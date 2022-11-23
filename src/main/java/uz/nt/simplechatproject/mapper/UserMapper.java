package uz.nt.simplechatproject.mapper;

import org.mapstruct.Mapper;
import uz.nt.simplechatproject.dto.UserDTO;
import uz.nt.simplechatproject.dto.custom.UserCustomDTO;
import uz.nt.simplechatproject.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);
    User toEntity(UserDTO userDTO);
    UserCustomDTO toCustomDTO(User user);
}
