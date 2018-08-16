package com.munenendereba.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class RecipeIngredient implements Parcelable {
    public static final Parcelable.Creator<RecipeIngredient> CREATOR = new Parcelable.Creator<RecipeIngredient>() {
        @Override
        public RecipeIngredient createFromParcel(Parcel source) {
            return new RecipeIngredient(source);
        }

        @Override
        public RecipeIngredient[] newArray(int size) {
            return new RecipeIngredient[size];
        }
    };
    private String ingredientName;
    private String measure;
    private String quantity;

    public RecipeIngredient(String ingredientName, String measure, String quantity) {
        this.ingredientName = ingredientName;
        this.measure = measure;
        this.quantity = quantity;
    }

    protected RecipeIngredient(Parcel in) {
        this.ingredientName = in.readString();
        this.measure = in.readString();
        this.quantity = in.readString();
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public String getMeasure() {
        return measure;
    }

    public String getQuantity() {
        return quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ingredientName);
        dest.writeString(this.measure);
        dest.writeString(this.quantity);
    }
}
