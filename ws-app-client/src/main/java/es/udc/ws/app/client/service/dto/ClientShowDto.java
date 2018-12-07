package es.udc.ws.app.client.service.dto;

import java.util.Calendar;
import java.util.Objects;

public class ClientShowDto
{
    private Long id;
    private String name;
    private String description;
    private Calendar startDate;
    private Calendar endDate;
    private Calendar limitDate;
    private long availableTickets;
    private float realPrice;
    private float discountedPrice;


    public ClientShowDto() { }

    public ClientShowDto(Long id, String name, String description, Calendar startDate, Calendar endDate, Calendar limitDate, long availableTickets, float realPrice, float discountedPrice) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.limitDate = limitDate;
        this.availableTickets = availableTickets;
        this.realPrice = realPrice;
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

    public Calendar getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(Calendar limitDate) {
        this.limitDate = limitDate;
    }

    public long getAvailableTickets() {
        return availableTickets;
    }

    public void setAvailableTickets(long availableTickets) {
        this.availableTickets = availableTickets;
    }

    public float getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(float realPrice) {
        this.realPrice = realPrice;
    }

    public float getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(float discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientShowDto that = (ClientShowDto) o;
        return availableTickets == that.availableTickets &&
                Float.compare(that.realPrice, realPrice) == 0 &&
                Float.compare(that.discountedPrice, discountedPrice) == 0 &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(limitDate, that.limitDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, startDate, endDate, limitDate, availableTickets, realPrice, discountedPrice);
    }

    @Override
    public String toString() {
        return "ClientShowDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", limitDate=" + limitDate +
                ", availableTickets=" + availableTickets +
                ", realPrice=" + realPrice +
                ", discountedPrice=" + discountedPrice +
                '}';
    }
}
