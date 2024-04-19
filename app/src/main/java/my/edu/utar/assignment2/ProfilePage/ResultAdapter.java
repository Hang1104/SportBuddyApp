package my.edu.utar.assignment2.ProfilePage;

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

public class ResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_GAME_RECORD = 0;
    private static final int VIEW_TYPE_SPORT_RECORD = 1;

    private List<GameRecord> mData;
    private List<String> sportList;

    public ResultAdapter() {
        mData = new ArrayList<>();
        sportList = new ArrayList<>();
    }

    public void setSportList(List<String> sportList) {
        this.sportList = sportList;
        notifyDataSetChanged();
    }

    public void addGamesRecord(GameRecord record) {
        mData.add(record);
    }

    public void clearGamesRecords() {
        mData.clear();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_GAME_RECORD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game_record, parent, false);
            return new GameRecordViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sport_record, parent, false);
            return new SportRecordViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GameRecordViewHolder) {
            GameRecordViewHolder gameHolder = (GameRecordViewHolder) holder;
            GameRecord record = mData.get(position);
            gameHolder.sportTypeTextView.setText("Sport Type: " + record.getSportType());
            gameHolder.gameSkillTextView.setText("Game Skill: " + record.getGameSkill());
            gameHolder.locationTextView.setText("Location: " + record.getLocation());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            gameHolder.dateTextView.setText("Date: " + dateFormat.format(record.getDate()));
            gameHolder.startTimeTextView.setText("Start Time: " + record.getStartTime());
            gameHolder.endTimeTextView.setText("End Time: " + record.getEndTime());

            if (record.hasGameDatePassed()) {
                gameHolder.rankButton.setVisibility(View.VISIBLE);
                gameHolder.rankButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Navigate to ranking page
                    }
                });
            } else {
                gameHolder.rankButton.setVisibility(View.GONE);
            }

        } else if (holder instanceof SportRecordViewHolder) {
            SportRecordViewHolder sportHolder = (SportRecordViewHolder) holder;
            // Bind data to the ViewHolder
            String sportInfo = sportList.get(position - mData.size());
            sportHolder.sportTextView.setText(sportInfo);

        }
    }

    @Override
    public int getItemCount() {
        return mData.size() + sportList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mData.size()) {
            return VIEW_TYPE_GAME_RECORD;
        } else {
            return VIEW_TYPE_SPORT_RECORD;
        }
    }

    private static class GameRecordViewHolder extends RecyclerView.ViewHolder {
        TextView sportTypeTextView;
        TextView gameSkillTextView;
        TextView locationTextView;
        TextView dateTextView;
        TextView startTimeTextView;
        TextView endTimeTextView;
        Button rankButton;

        public GameRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            sportTypeTextView = itemView.findViewById(R.id.sportTypeTextView);
            gameSkillTextView = itemView.findViewById(R.id.gameSkillTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            startTimeTextView = itemView.findViewById(R.id.startTimeTextView);
            endTimeTextView = itemView.findViewById(R.id.endTimeTextView);
            rankButton = itemView.findViewById(R.id.rankButton);
        }
    }

    private static class SportRecordViewHolder extends RecyclerView.ViewHolder {
        TextView sportTextView;

        public SportRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            sportTextView = itemView.findViewById(R.id.sportTextView);
        }
    }
}
