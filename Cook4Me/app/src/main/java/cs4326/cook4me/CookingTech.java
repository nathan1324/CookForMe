package cs4326.cook4me;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A class by which to fetch and store the Cooking Techniques stored in the Firebase database
 */

public class CookingTech implements Parcelable {
    private String title;
    private String description;

    public CookingTech() {
        //empty
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    protected CookingTech(Parcel in) {
        title = in.readString();
        description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CookingTech> CREATOR = new Parcelable.Creator<CookingTech>() {
        @Override
        public CookingTech createFromParcel(Parcel in) {
            return new CookingTech(in);
        }

        @Override
        public CookingTech[] newArray(int size) {
            return new CookingTech[size];
        }
    };
}