package net.malevy.edareservation.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {

    public static class Statuses {
        public final static String PENDING = "pending";
        public final static String COMPLETED = "completed";
    }

    private String id;
    private String showId;
    private String status;
    private List<String> seats;
}

