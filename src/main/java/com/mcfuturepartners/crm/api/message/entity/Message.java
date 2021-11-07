package com.mcfuturepartners.crm.api.message.entity;


import com.mcfuturepartners.crm.api.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@Entity
@Data
@Builder
@Table(name = "message")
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    @Column(name = "message_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @Column(name = "message_title")
    private String title;

    @Column(name = "message_content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Message updateModified(Message modifiedMessage){
        if(StringUtils.hasText(modifiedMessage.getTitle())){
            this.title = modifiedMessage.getTitle();
        }
        if(StringUtils.hasText(modifiedMessage.getContent())){
            this.content = modifiedMessage.getContent();
        }
        if(!ObjectUtils.isEmpty(modifiedMessage.getUser())){
            this.user = modifiedMessage.getUser();
        }
        return this;
    }
}
