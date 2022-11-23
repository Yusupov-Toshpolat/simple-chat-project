package uz.nt.simplechatproject.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

import static uz.nt.simplechatproject.helper.AppMessages.EMPTY_FIELD;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatDTO {
    @ApiModelProperty(notes = "ID of the chat")
    private Integer id;

    @ApiModelProperty(notes = "Name of the chat", required = true)
    @NotBlank(message = EMPTY_FIELD)
    private String name;

    @ApiModelProperty(notes = "List of IDs of users who are members of the chat. " +
            "Note that members must be at least one",
            required = true)
    @Size(min = 1, message = "Chat members must be at least one")
    private List<Integer> userIds;

    @ApiModelProperty(notes = "Date of the chat was created")
    private String createdAt;
}
