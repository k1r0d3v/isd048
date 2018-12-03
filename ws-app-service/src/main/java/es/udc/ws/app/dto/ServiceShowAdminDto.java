package es.udc.ws.app.dto;

import java.util.Calendar;
import java.util.Objects;

public class ServiceShowAdminDto
{
    private Long id;
    private String name;
    private String description;
    private Calendar startDate;
    private long duration;
    private Calendar limitDate;
    private long maxTickets;
    private long availableTickets;
    private float realPrice;
    private float discountedPrice;
    private float salesCommission;

    public ServiceShowAdminDto() { }

    public ServiceShowAdminDto(Long id, String name, String description, Calendar startDate, long duration, Calendar limitDate, long maxTickets, long availableTickets, float realPrice, float discountedPrice, float salesCommission) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.duration = duration;
        this.limitDate = limitDate;
        this.maxTickets = maxTickets;
        this.availableTickets = availableTickets;
        this.realPrice = realPrice;
        this.discountedPrice = discountedPrice;
        this.salesCommission = salesCommission;
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

    public long getMaxTickets() {
        return maxTickets;
    }

    public void setMaxTickets(long maxTickets) {
        this.maxTickets = maxTickets;
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

    public float getSalesCommission() {
        return salesCommission;
    }

    public void setSalesCommission(float salesCommission) {
        this.salesCommission = salesCommission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceShowAdminDto that = (ServiceShowAdminDto) o;
        return duration == that.duration &&
                maxTickets == that.maxTickets &&
                availableTickets == that.availableTickets &&
                Float.compare(that.realPrice, realPrice) == 0 &&
                Float.compare(that.discountedPrice, discountedPrice) == 0 &&
                Float.compare(that.salesCommission, salesCommission) == 0 &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(limitDate, that.limitDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, startDate, duration, limitDate, maxTickets, availableTickets, realPrice, discountedPrice, salesCommission);
    }

    @Override
    public String toString() {
        return "ServiceShowAdminDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", duration=" + duration +
                ", limitDate=" + limitDate +
                ", maxTickets=" + maxTickets +
                ", availableTickets=" + availableTickets +
                ", realPrice=" + realPrice +
                ", discountedPrice=" + discountedPrice +
                ", salesCommission=" + salesCommission +
                '}';
    }
}
