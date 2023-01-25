package ma.inpt.rentingCarApp.entities;

import javax.persistence.*;

@Entity
@Table(name = "UserRating")
public class UserRating {

    // class attributes :
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Primary key of the USERRATING table.
    private long userRatingId;
    private long carId; // one to one
    private long userId; // one to one
    private int ratingValue;
    private String comment;
    private boolean done;

    // class constructors :
    public UserRating() {

    }

    public UserRating(long carId, long userId, int ratingValue, String comment, boolean done) {
        super();
        this.carId = carId;
        this.userId = userId;
        this.ratingValue = ratingValue;
        this.comment = comment;
        this.done = done;
    }

    // class methods :
    public long getUserRatingId() {
        return userRatingId;
    }

    public void setUserRatingId(long userRatingId) {
        this.userRatingId = userRatingId;
    }

    public long getCarId() {
        return carId;
    }

    public void setCarId(long carId) {
        this.carId = carId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean getDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

}

