package net.malevy.edareservation.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentApproved {

    private String orderId;
    private String showId;
    private String confirmationNumber;
    private String message;

}
