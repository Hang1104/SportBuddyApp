package my.edu.utar.assignment2;

public class RatingReviewItem {

    private String rating;
    private String review;

    public RatingReviewItem(String rating, String review) {
        this.rating = rating;
        this.review = review;
    }

    public String getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }
}
