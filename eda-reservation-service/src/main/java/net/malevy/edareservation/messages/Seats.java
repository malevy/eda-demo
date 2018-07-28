package net.malevy.edareservation.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seats {

    public static class Statuses {
        public final static String RESERVED = "reserved";
        public final static String SOLD = "sold";
    }

    private String orderId;
    private String showId;
    private String status;
    private List<String> seats;
}

