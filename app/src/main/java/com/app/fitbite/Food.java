package com.app.fitbite;

public class Food {

    private String id;
    private String foodName;
    private String calories;

    // Default constructor required for Firebase
    public Food() {
        // Empty constructor needed for Firebase
    }

    // constructors
    public Food(String id, String foodName, String calories) {
        this.id = id;
        this.foodName = foodName;
        this.calories = calories;
    }

    public Food(String foodName, String calories) {
        this.foodName = foodName;
        this.calories = calories;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }
}