package my.edu.utar.assignment2.LearningPage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import my.edu.utar.assignment2.R;
import my.edu.utar.assignment2.RatingPage;

public class LearningPage extends AppCompatActivity {

    private GridView gridView;
    private List<Map<String, Object>> sports;
    private int[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_page);

        gridView = findViewById(R.id.gridView);

        // Define sports data
        sports = new ArrayList<>();
//        sports.add(createSport("Display Rating", R.drawable.tabletennis, DisplayRatingActivity.class, colors[4]));
        sports.add(createSport("Badminton", R.drawable.badminton, Badminton.class, colors[0]));
        sports.add(createSport("Volleyball", R.drawable.volleyball, Volleyball.class, colors[1]));
        sports.add(createSport("Basketball", R.drawable.basketball, Basketball.class, colors[2]));
        sports.add(createSport("Table Tennis", R.drawable.tabletennis, Tabletennis.class, colors[3]));
        sports.add(createSport("Rating", R.drawable.tabletennis, RatingPage.class, colors[4]));

        // Set adapter for the GridView
        SimpleAdapter adapter = new SimpleAdapter(this, sports,
                R.layout.grid_item,
                new String[]{"name", "image"},
                new int[]{R.id.sportName, R.id.sportImage});
        gridView.setAdapter(adapter);

        // Handle item click
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the class of the activity to start
                Class<?> activityClass = (Class<?>) sports.get(position).get("activity");

                // Start the activity
                Intent intent = new Intent(LearningPage.this, activityClass);
                startActivity(intent);
            }
        });
    }

    private Map<String, Object> createSport(String name, int imageResId, Class<?> activityClass, int color) {
        Map<String, Object> sport = new HashMap<>();
        sport.put("name", name);
        sport.put("image", imageResId);
        sport.put("activity", activityClass);
        sport.put("color", color);
        return sport;
    }
}