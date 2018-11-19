package es.udc.ws.app.model.show;

import java.util.Calendar;
import java.util.Objects;

public class Show
{
	private long id;
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

	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;

		Show show = (Show) object;
		return id == show.id &&
				duration == show.duration &&
				maxTickets == show.maxTickets && 
				availableTickets == show.availableTickets &&
				Float.compare(show.realPrice, realPrice) == 0 &&
				Float.compare(show.discountedPrice, discountedPrice) == 0 &&
				Float.compare(show.salesCommission, salesCommission) == 0 &&
				java.util.Objects.equals(name, show.name) &&
				java.util.Objects.equals(description, show.description) &&
				java.util.Objects.equals(startDate, show.startDate) &&
				java.util.Objects.equals(limitDate, show.limitDate);
	}

	public int hashCode() {
		return Objects.hash(super.hashCode(), id, name, description, startDate, duration, limitDate, maxTickets, availableTickets, realPrice, discountedPrice, salesCommission);
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
				", availableTickets=" + availableTickets +
				", realPrice=" + realPrice +
				", discountedPrice=" + discountedPrice +
				", salesCommission=" + salesCommission +
				'}';
	}
}