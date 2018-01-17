package thedoctors05.tentfinder;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Gregory on 2017-10-26.
 */

public class Tent implements Parcelable {

    @SerializedName("Title")
    public String Title;
    @SerializedName("Latitude")
    public String Latitude;
    @SerializedName("Longitude")
    public String Longitude;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }



    public Tent() {

    }

    public Tent(String title, String longitude, String latitude) {
        Title = title;
        Longitude = longitude;
        Latitude = latitude;
    }

    protected Tent(Parcel in) {
        Title = in.readString();
        Longitude = in.readString();
        Latitude = in.readString();
    }

    public static final Creator<Tent> CREATOR = new Creator<Tent>() {
        @Override
        public Tent createFromParcel(Parcel in) {
            return new Tent(in);
        }

        @Override
        public Tent[] newArray(int size) {
            return new Tent[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Title);
        parcel.writeString(Longitude);
        parcel.writeString(Latitude);
    }
}
