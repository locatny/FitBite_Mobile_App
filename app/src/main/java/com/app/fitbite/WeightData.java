package com.app.fitbite;

public class WeightData {
    private double initialWeight;
    private double currentWeight;
    private double targetWeight;
    private String date;

    // Diperlukan untuk Firebase
    public WeightData() {}

    public WeightData(double initialWeight, double currentWeight, double targetWeight, String date) {
        this.initialWeight = initialWeight;
        this.currentWeight = currentWeight;
        this.targetWeight = targetWeight;
        this.date = date;
    }

    public double getInitialWeight() {
        return initialWeight;
    }

    public void setInitialWeight(double initialWeight) {
        this.initialWeight = initialWeight;
    }

    public double getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(double currentWeight) {
        this.currentWeight = currentWeight;
    }

    public double getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(double targetWeight) {
        this.targetWeight = targetWeight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
