package uz.nt.simplechatproject.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "chat_users")
@Data
public class ChatUsers {
    @Id
    @SequenceGenerator(name = "chat_users_seq", sequenceName = "chat_users_seq", allocationSize = 1)
    @GeneratedValue(generator = "chat_users_seq")
    private Integer id;

    @Column(name = "chats_id")
    private Integer chatId;

    @Column(name = "users_id")
    private Integer userId;
}
