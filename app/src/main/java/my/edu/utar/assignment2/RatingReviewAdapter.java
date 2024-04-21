package my.edu.utar.assignment2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RatingReviewAdapter extends RecyclerView.Adapter<RatingReviewAdapter.ViewHolder> {

    private List<RatingReviewItem> items;
    private int rating = 0;

    public RatingReviewAdapter() {
        items = new ArrayList<>();
    }

    public void setItems(List<RatingReviewItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void setRating(int rating) {
        this.rating = rating;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rating_review, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RatingReviewItem item = items.get(position);

        // Set star rating
        int ratingValue = Integer.parseInt(item.getRating());
        for (int i = 0; i < 5; i++) {
            if (i < ratingValue) {
                holder.starViews.get(i).setImageResource(android.R.drawable.star_on);
            } else {
                holder.starViews.get(i).setImageResource(android.R.drawable.star_off);
            }
        }

        // Set review text
        holder.reviewTextView.setText(item.getReview());
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ratingTextView;
        TextView reviewTextView;
        List<ImageView> starViews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            ratingTextView = itemView.findViewById(R.id.ratingTextView);
            reviewTextView = itemView.findViewById(R.id.reviewTextView);
            starViews = new ArrayList<>();
            starViews.add((ImageView) itemView.findViewById(R.id.star1));
            starViews.add((ImageView) itemView.findViewById(R.id.star2));
            starViews.add((ImageView) itemView.findViewById(R.id.star3));
            starViews.add((ImageView) itemView.findViewById(R.id.star4));
            starViews.add((ImageView) itemView.findViewById(R.id.star5));
        }
    }
}
