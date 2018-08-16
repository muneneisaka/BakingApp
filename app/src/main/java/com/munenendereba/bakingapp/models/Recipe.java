package com.munenendereba.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Recipe implements Parcelable {
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
    private String recipeName;
    private int recipeId;
    private int servesNumPeople;
    private ArrayList<RecipeIngredient> recipeIngredient;
    private ArrayList<RecipeStep> recipeStep;

    public Recipe(int recipeId, String recipeName, int servesNumPeople, ArrayList<RecipeIngredient> recipeIngredient, ArrayList<RecipeStep> recipeStep) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.servesNumPeople = servesNumPeople;
        this.recipeIngredient = recipeIngredient;
        this.recipeStep = recipeStep;
    }

    protected Recipe(Parcel in) {
        this.recipeName = in.readString();
        this.recipeId = in.readInt();
        this.servesNumPeople = in.readInt();
        this.recipeIngredient = new ArrayList<RecipeIngredient>();
        in.readList(this.recipeIngredient, RecipeIngredient.class.getClassLoader());
        this.recipeStep = new ArrayList<RecipeStep>();
        in.readList(this.recipeStep, RecipeStep.class.getClassLoader());
    }

    public String getRecipeName() {
        return recipeName;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public int getServesNumPeople() {
        return servesNumPeople;
    }

    public ArrayList<RecipeIngredient> getRecipeIngredient() {
        return recipeIngredient;
    }

    public ArrayList<RecipeStep> getRecipeStep() {
        return recipeStep;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.recipeName);
        dest.writeInt(this.recipeId);
        dest.writeInt(this.servesNumPeople);
        dest.writeList(this.recipeIngredient);
        dest.writeList(this.recipeStep);
    }
}
