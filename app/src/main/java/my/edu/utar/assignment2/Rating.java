package my.edu.utar.assignment2;

public class Rating {
    private String userId;
    private int rating;
    private String review;
    private String gameId;

    public Rating() {
        // Default constructor required for Firestore
    }

    public Rating(String userId, int rating, String reviewd) {
        this.userId = userId;
        this.rating = rating;
        this.review = review;

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
