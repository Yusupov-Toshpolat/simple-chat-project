package uz.nt.simplechatproject.dto.custom;

import lombok.Data;
import lombok.EqualsAndHashCode;
import uz.nt.simplechatproject.dto.UserDTO;

import java.util.List;
@EqualsAndHashCode(callSuper = true)
@Data
public class UserCustomDTO extends UserDTO {
    private List<ChatCustomDTO> chats;
}
