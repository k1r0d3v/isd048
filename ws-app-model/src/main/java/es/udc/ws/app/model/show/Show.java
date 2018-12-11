package es.udc.ws.app.model.show;

import java.util.Calendar;
import java.util.Objects;

public class Show
{
	private Long id;
	private String name;
	private String description;
	private Calendar startDate;
	private long duration;
	private Calendar limitDate;
	private long maxTickets;
	private Long tickets;
	private float price;
	private float discountedPrice;
	private float commission;

	public Show() { }

	public Show(Long id, String name, String description, Calendar startDate,
				long duration, Calendar limitDate, long maxTickets, Long tickets,
				float price, float discountedPrice, float commission) {
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
		this.startDate.set(Calendar.MILLISECOND, 0);
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
		this.limitDate.set(Calendar.MILLISECOND, 0);
	}

	public long getMaxTickets() {
		return maxTickets;
	}

	public void setMaxTickets(long maxTickets) {
		this.maxTickets = maxTickets;
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

	public float getCommission() {
		return commission;
	}

	public void setCommission(float commission) {
		this.commission = commission;
	}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Show show = (Show) o;
        return duration == show.duration &&
                maxTickets == show.maxTickets &&
                Float.compare(show.price, price) == 0 &&
                Float.compare(show.discountedPrice, discountedPrice) == 0 &&
                Float.compare(show.commission, commission) == 0 &&
                Objects.equals(id, show.id) &&
                Objects.equals(name, show.name) &&
                Objects.equals(description, show.description) &&
                Objects.equals(startDate, show.startDate) &&
                Objects.equals(limitDate, show.limitDate) &&
                Objects.equals(tickets, show.tickets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, startDate, duration, limitDate, maxTickets, tickets, price, discountedPrice, commission);
    }

    @Override
    public String toString() {
        return "Show{" +
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
                '}';
    }
}