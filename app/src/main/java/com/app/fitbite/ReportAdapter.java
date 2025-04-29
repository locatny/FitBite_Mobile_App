package com.app.fitbite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private List<Food> foodList;

    public ReportAdapter(List<Food> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_report, parent, false);
        return new ReportViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Food food = foodList.get(position);
        holder.foodNameTextView.setText(food.getFoodName()); // <--- Tambahkan baris ini
        holder.caloriesTextView.setText("Calories: " + food.getCalories());
    }


    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {

        TextView foodNameTextView, caloriesTextView;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            foodNameTextView = itemView.findViewById(R.id.textFoodName);
            caloriesTextView = itemView.findViewById(R.id.textCalories);
        }
    }
}
