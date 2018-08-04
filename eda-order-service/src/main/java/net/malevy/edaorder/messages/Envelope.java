package net.malevy.edaorder.messages;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Data
public class Envelope<TPAYLOAD> {

    private String id = UUID.randomUUID().toString();
    private String timestamp =  DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());
    private String messageType;
    private TPAYLOAD payload;

    public Envelope(){};

    public Envelope(String messageType, TPAYLOAD payload) {
        this.messageType = messageType;
        this.payload = payload;
    }
}
