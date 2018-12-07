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
	private long tickets;
	private float price;
	private float discountedPrice;
	private float commission;

	public Show() { }

	public Show(Long id, String name, String description, Calendar startDate,
				long duration, Calendar limitDate, long maxTickets, long tickets,
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

	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;

		Show show = (Show) object;
		return id == show.id &&
				duration == show.duration &&
				maxTickets == show.maxTickets && 
				tickets == show.tickets &&
				Float.compare(show.price, price) == 0 &&
				Float.compare(show.discountedPrice, discountedPrice) == 0 &&
				Float.compare(show.commission, commission) == 0 &&
				java.util.Objects.equals(name, show.name) &&
				java.util.Objects.equals(description, show.description) &&
				java.util.Objects.equals(startDate, show.startDate) &&
				java.util.Objects.equals(limitDate, show.limitDate);
	}

	public int hashCode() {
		return Objects.hash(super.hashCode(), id, name, description, startDate, duration, limitDate, maxTickets, tickets, price, discountedPrice, commission);
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