package org.example.request;

import lombok.*;
import org.example.request.MessageResponse;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageResult {

    private MessageResponse message;

    private String receiverUsername;

}