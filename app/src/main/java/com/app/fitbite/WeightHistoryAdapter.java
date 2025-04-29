package com.app.fitbite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class WeightHistoryAdapter extends RecyclerView.Adapter<WeightHistoryAdapter.WeightHistoryViewHolder> {

    private List<WeightData> weightDataList;

    public WeightHistoryAdapter(List<WeightData> weightDataList) {
        this.weightDataList = weightDataList;
    }

    @NonNull
    @Override
    public WeightHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weight_history, parent, false);
        return new WeightHistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WeightHistoryViewHolder holder, int position) {
        WeightData weightData = weightDataList.get(position);

        holder.textViewDate.setText("Tanggal: " + weightData.getDate());
        holder.textViewWeight.setText("Berat: " + weightData.getCurrentWeight() + " kg");
        double weightLost = weightData.getInitialWeight() - weightData.getCurrentWeight();
        holder.textViewWeightLost.setText("Turun: " + weightLost + " kg");
    }

    @Override
    public int getItemCount() {
        return weightDataList.size();
    }

    public static class WeightHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewWeight, textViewDate, textViewWeightLost;

        public WeightHistoryViewHolder(View itemView) {
            super(itemView);
            textViewWeight = itemView.findViewById(R.id.textWeight);
            textViewDate = itemView.findViewById(R.id.textDate);
            textViewWeightLost = itemView.findViewById(R.id.textWeightLost);
        }
    }
}
