package es.udc.ws.app.model.reservation;

import java.util.Calendar;
import java.util.Objects;

public class Reservation
{
    private long id;
    private long showId;
    private String email;
    private String cardNumber;
    private int tickets;
    private boolean isValid;
    private String code;
    private Calendar reservationDate;
    private float price;
    
	public Reservation(long id, long showId, String email, 
			String cardNumber, int tickets, boolean isValid, String code, 
			Calendar reservationDate, float price) {
		
		super();
		
		this.id = id;
		this.showId = showId;
		this.email = email;
		this.cardNumber = cardNumber;
		this.tickets = tickets;
		this.isValid = isValid;
		this.code = code;
		this.reservationDate = reservationDate;
		this.price = price;
	}


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getShowId() {
        return showId;
    }

    public void setShowId(long showId) {
        this.showId = showId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getTickets() {
        return tickets;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Calendar getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Calendar reservationDate) {
        this.reservationDate = reservationDate;
        this.reservationDate.set(Calendar.MILLISECOND, 0);
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return id == that.id &&
                showId == that.showId &&
                tickets == that.tickets &&
                isValid == that.isValid &&
                Float.compare(that.price, price) == 0 &&
                Objects.equals(email, that.email) &&
                Objects.equals(cardNumber, that.cardNumber) &&
                Objects.equals(code, that.code) &&
                Objects.equals(reservationDate, that.reservationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, showId, email, cardNumber, tickets, isValid, code, reservationDate, price);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", showId=" + showId +
                ", email='" + email + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", tickets=" + tickets +
                ", isValid=" + isValid +
                ", code='" + code + '\'' +
                ", reservationDate=" + reservationDate +
                ", price=" + price +
                '}';
    }
}
