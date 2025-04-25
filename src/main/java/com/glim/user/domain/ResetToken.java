
package com.glim.user.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "reset_tokens")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetToken {

    @Id
    private String token;

    private String username;

    private LocalDateTime createdAt;
}
