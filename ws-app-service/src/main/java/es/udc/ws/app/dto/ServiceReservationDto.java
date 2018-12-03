package es.udc.ws.app.dto;

import java.util.Calendar;

public class ServiceReservationDto {
    private long id;
    private long showId;
    private String email;
    private String cardNumber;
    private int tickets;
    private boolean isValid;
    private String code;
    private Calendar reservationDate;
    private float price;
}
