package my.edu.utar.assignment2.ProfilePage;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import my.edu.utar.assignment2.R;

public class HorizontalScrollAdapter extends RecyclerView.Adapter<HorizontalScrollAdapter.ViewHolder> {

    private List<String> itemList;
    private String[] skillLevels = {"Beginner", "Intermediate", "Advanced"};

    public HorizontalScrollAdapter(List<String> itemList) {
        this.itemList = itemList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView sportText, skillLevelText;
        public ImageView editIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            sportText = itemView.findViewById(R.id.sportText);
            skillLevelText = itemView.findViewById(R.id.skillLevelText);
            editIcon = itemView.findViewById(R.id.editIcon);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal_scroll, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int dataIndex = position * 2;
        if (dataIndex < itemList.size()) {
            String selectedSport = itemList.get(dataIndex);
            holder.sportText.setText(selectedSport);
        }

        if (dataIndex + 1 < itemList.size()) {
            final String selectedSkillLevel = itemList.get(dataIndex + 1);
            holder.skillLevelText.setText(selectedSkillLevel);

            // Add OnClickListener to the edit icon
            holder.editIcon.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Edit Skill Level");

                // Set up the input (Spinner)
                final Spinner spinner = new Spinner(v.getContext());
                ArrayAdapter<String> adapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_item, skillLevels);
                spinner.setAdapter(adapter);
                // Remove "Advanced" if selected sport is already advanced
                if (selectedSkillLevel.equals("Advanced")) {
                    List<String> skillList = new ArrayList<>(Arrays.asList(skillLevels));
                    skillList.remove("Advanced");
                    adapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_item, skillList);
                    spinner.setAdapter(adapter);
                }
                builder.setView(spinner);


                // Set up the buttons
                builder.setPositiveButton("OK", (dialog, which) -> {
                    String newSkillLevel = spinner.getSelectedItem().toString();
                    // Update the skill level in your data list
                    if (dataIndex + 1 < itemList.size()) {
                        itemList.set(dataIndex + 1, newSkillLevel);
                        notifyDataSetChanged();

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        Map<String, Object> newData = new HashMap<>();
                        newData.put("selectedSkillLevel", newSkillLevel);

                        db.collection("skill_levels")
                                .whereEqualTo("userId", userId)
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        // 获取文档引用并更新文档
                                        db.collection("skill_levels").document(document.getId())
                                                .update("selectedSkillLevel", newSkillLevel)
                                                .addOnSuccessListener(aVoid -> Log.d(TAG, "Skill level updated successfully"))
                                                .addOnFailureListener(e -> Log.e(TAG, "Error updating skill level", e));
                                    }
                                })
                                .addOnFailureListener(e -> Log.e(TAG, "Error getting documents: ", e));
                    }
                });
                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

                builder.show();
            });
        }
    }

    @Override
    public int getItemCount() {
        return (int) Math.ceil((double) itemList.size() / 2);
    }
}
