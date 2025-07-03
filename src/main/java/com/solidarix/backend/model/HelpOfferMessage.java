package com.solidarix.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity @Data
@Table(name = "help_offer_messages")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class HelpOfferMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "help_offer_id")
    private HelpOffer helpOffer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sender_id")
    private User sender;

    @Column(nullable = false, length = 1000)
    private String message;

    private LocalDateTime sentAt;
    private LocalDateTime seenAt;

    public User getReceiver(){
        return (sender.equals(helpOffer.getHelper()))
                ? helpOffer.getHelpRequest().getRequester()
                : this.helpOffer.getHelper();
    }

}
