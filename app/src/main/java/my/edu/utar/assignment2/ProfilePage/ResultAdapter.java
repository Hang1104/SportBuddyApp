package my.edu.utar.assignment2.ProfilePage;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import my.edu.utar.assignment2.R;
import my.edu.utar.assignment2.RatingPage;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {
    private List<GameRecord> mData;

    public ResultAdapter() {
        mData = new ArrayList<>();
    }

    public void addGamesRecord(GameRecord record) {
        mData.add(record);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GameRecord record = mData.get(position);
        holder.sportTypeTextView.setText("Sport Type: " + record.getSportType());
        holder.gameSkillTextView.setText("Game Skill: " + record.getGameSkill());
        holder.locationTextView.setText("Location: " + record.getLocation());
        holder.locationTextView.setText("Address: " + record.getAddress());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        holder.dateTextView.setText("Date: " + dateFormat.format(record.getDate()));
        holder.startTimeTextView.setText("Start Time: " + record.getStartTime());
        holder.endTimeTextView.setText("End Time: " + record.getEndTime());


        if (record.hasGameDatePassed()) {
            holder.rankButton.setVisibility(View.VISIBLE);
//            holder.viewRatingButton.setVisibility(View.VISIBLE);
            holder.rankButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Navigate to RatingPage
                    Intent intent = new Intent(v.getContext(), RatingPage.class);
                    v.getContext().startActivity(intent);
                }
            });
//            holder.viewRatingButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // Navigate to View Rating Page
//                }
//            });
        } else {
            holder.rankButton.setVisibility(View.GONE);
//            holder.viewRatingButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView sportTypeTextView;
        TextView gameSkillTextView;
        TextView locationTextView;
        TextView dateTextView;
        TextView startTimeTextView;
        TextView endTimeTextView;
        Button rankButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sportTypeTextView = itemView.findViewById(R.id.sportTypeTextView);
            gameSkillTextView = itemView.findViewById(R.id.gameSkillTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            startTimeTextView = itemView.findViewById(R.id.startTimeTextView);
            endTimeTextView = itemView.findViewById(R.id.endTimeTextView);
            rankButton = itemView.findViewById(R.id.rankButton);
//            viewRatingButton = itemView.findViewById(R.id.viewratingbutton);
        }
    }
}