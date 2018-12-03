package es.udc.ws.app.dto;

import java.util.Calendar;
import java.util.Objects;

public class ServiceShowDto
{
    private long id;
    private String name;
    private String description;
    private Calendar startDate;
    private long duration;
    private Calendar limitDate;
    private long availableTickets;
    private float realPrice;
    private float discountedPrice;


    public ServiceShowDto() { }

    public ServiceShowDto(long id, String name, String description, Calendar startDate, long duration, Calendar limitDate, long availableTickets, float realPrice, float discountedPrice) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.duration = duration;
        this.limitDate = limitDate;
        this.availableTickets = availableTickets;
        this.realPrice = realPrice;
        this.discountedPrice = discountedPrice;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceShowDto that = (ServiceShowDto) o;
        return id == that.id &&
                duration == that.duration &&
                availableTickets == that.availableTickets &&
                Float.compare(that.realPrice, realPrice) == 0 &&
                Float.compare(that.discountedPrice, discountedPrice) == 0 &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(limitDate, that.limitDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, startDate, duration, limitDate, availableTickets, realPrice, discountedPrice);
    }
}
