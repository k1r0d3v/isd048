package es.udc.ws.app.dto;

import java.util.Calendar;
import java.util.Objects;

public class ServiceShowDto
{
    private Long id;
    private String name;
    private String description;
    private Calendar startDate;
    private long duration;
    private Calendar limitDate;
    private Long tickets;
    private float price;
    private float discountedPrice;


    public ServiceShowDto() { }

    public ServiceShowDto(Long id, String name, String description, Calendar startDate, long duration, Calendar limitDate, Long tickets, float price, float discountedPrice) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.duration = duration;
        this.limitDate = limitDate;
        this.tickets = tickets;
        this.price = price;
        this.discountedPrice = discountedPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Calendar getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(Calendar limitDate) {
        this.limitDate = limitDate;
    }

    public Long getTickets() {
        return tickets;
    }

    public void setTickets(Long tickets) {
        this.tickets = tickets;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(float discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceShowDto that = (ServiceShowDto) o;
        return duration == that.duration &&
                Float.compare(that.price, price) == 0 &&
                Float.compare(that.discountedPrice, discountedPrice) == 0 &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(limitDate, that.limitDate) &&
                Objects.equals(tickets, that.tickets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, startDate, duration, limitDate, tickets, price, discountedPrice);
    }

    @Override
    public String toString() {
        return "ServiceShowDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", duration=" + duration +
                ", limitDate=" + limitDate +
                ", tickets=" + tickets +
                ", price=" + price +
                ", discountedPrice=" + discountedPrice +
                '}';
    }
}
