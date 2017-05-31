package cs4326.cook4me;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * A class to carry all of the data stored in Firebase
 */
@IgnoreExtraProperties
public class Recipe implements Parcelable {
    /**
     * Title of recipe to be displayed
     */
    private String title;

    /**
     * Time it takes to cook this recipe
     */
    private String timeToCook;
    private ArrayList<String> steps;
    private ArrayList<String> ingredients;


    public Recipe() {
        //steps = new ArrayList<String>();
        //ingredients = new ArrayList<String>();
    }

    public String getTitle() {
        return title;
    }

    public String getTimeToCook() {
        return timeToCook;
    }

    public ArrayList<String> getSteps() {
        return steps;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    protected Recipe(Parcel in) {
        title = in.readString();
        timeToCook = in.readString();
        if (in.readByte() == 0x01) {
            steps = new ArrayList<String>();
            in.readList(steps, String.class.getClassLoader());
        } else {
            steps = null;
        }
        if (in.readByte() == 0x01) {
            ingredients = new ArrayList<String>();
            in.readList(ingredients, String.class.getClassLoader());
        } else {
            ingredients = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(timeToCook);
        if (steps == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(steps);
        }
        if (ingredients == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(ingredients);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public void setTitle(String title) {
        this.title = toTitleCase(title);
    }

    public void setTimeToCook(String timeToCook) {
        this.timeToCook = timeToCook;
    }

    public void setSteps(ArrayList<String> steps) {
        this.steps = steps;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    private static String toTitleCase(String givenString) {
        givenString = givenString.trim();
        String[] arr = givenString.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }
}