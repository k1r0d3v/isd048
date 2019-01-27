package es.udc.ws.app.socialnetwork;

import java.util.Calendar;

public class SocialNetworkShowPost
{
    private Long postId;
    private long likes; // Required by user

    private Long showId;
    private String showName;
    private String showDescription;
    private Calendar showStartDate;
    private long showDuration;
    private Calendar showLimitDate;
    private long showMaxTickets;
    private Long showTickets;
    private float showPrice;
    private float showDiscountedPrice;


    public SocialNetworkShowPost() { }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getShowDescription() {
        return showDescription;
    }

    public void setShowDescription(String showDescription) {
        this.showDescription = showDescription;
    }

    public Calendar getShowStartDate() {
        return showStartDate;
    }

    public void setShowStartDate(Calendar showStartDate) {
        this.showStartDate = showStartDate;
    }

    public long getShowDuration() {
        return showDuration;
    }

    public void setShowDuration(long showDuration) {
        this.showDuration = showDuration;
    }

    public Calendar getShowLimitDate() {
        return showLimitDate;
    }

    public void setShowLimitDate(Calendar showLimitDate) {
        this.showLimitDate = showLimitDate;
    }

    public long getShowMaxTickets() {
        return showMaxTickets;
    }

    public void setShowMaxTickets(long showMaxTickets) {
        this.showMaxTickets = showMaxTickets;
    }

    public Long getShowTickets() {
        return showTickets;
    }

    public void setShowTickets(Long showTickets) {
        this.showTickets = showTickets;
    }

    public float getShowPrice() {
        return showPrice;
    }

    public void setShowPrice(float showPrice) {
        this.showPrice = showPrice;
    }

    public float getShowDiscountedPrice() {
        return showDiscountedPrice;
    }

    public void setShowDiscountedPrice(float showDiscountedPrice) {
        this.showDiscountedPrice = showDiscountedPrice;
    }
}
