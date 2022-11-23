package uz.nt.simplechatproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.nt.simplechatproject.dto.ChatDTO;
import uz.nt.simplechatproject.dto.custom.ChatCustomDTO;
import uz.nt.simplechatproject.model.Chat;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
    ChatDTO toDto(Chat chat);

    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
    Chat toEntity(ChatDTO chatDTO);

    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
    ChatCustomDTO toCustomDto(Chat chat);
}
