package es.udc.ws.app.client.service.dto;

import java.util.Calendar;
import java.util.Objects;

public class ClientAdminShowDto {
    private Long id;
    private String name;
    private String description;
    private Calendar startDate;
    private long duration;
    private Calendar limitDate;
    private long maxTickets;
    private long tickets;
    private float price;
    private float discountedPrice;
    private float commission;

    private Long likes;

    public ClientAdminShowDto() { }

    public ClientAdminShowDto(Long id, String name, String description, Calendar startDate, long duration, Calendar limitDate, long maxTickets, long tickets, float price, float discountedPrice, float commission, Long likes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.duration = duration;
        this.limitDate = limitDate;
        this.maxTickets = maxTickets;
        this.tickets = tickets;
        this.price = price;
        this.discountedPrice = discountedPrice;
        this.commission = commission;

        this.likes = likes;
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

    public long getTickets() {
        return tickets;
    }

    public void setTickets(long tickets) {
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

    public float getCommission() {
        return commission;
    }

    public void setCommission(float commission) {
        this.commission = commission;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientAdminShowDto that = (ClientAdminShowDto) o;
        return duration == that.duration &&
                maxTickets == that.maxTickets &&
                tickets == that.tickets &&
                Float.compare(that.price, price) == 0 &&
                Float.compare(that.discountedPrice, discountedPrice) == 0 &&
                Float.compare(that.commission, commission) == 0 &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(limitDate, that.limitDate) &&
                Objects.equals(likes, that.likes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, startDate, duration, limitDate, maxTickets, tickets, price, discountedPrice, commission, likes);
    }

    @Override
    public String toString() {
        return "ClientAdminShowDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", duration=" + duration +
                ", limitDate=" + limitDate +
                ", maxTickets=" + maxTickets +
                ", tickets=" + tickets +
                ", price=" + price +
                ", discountedPrice=" + discountedPrice +
                ", commission=" + commission +
                ", likes=" + likes +
                '}';
    }
}
