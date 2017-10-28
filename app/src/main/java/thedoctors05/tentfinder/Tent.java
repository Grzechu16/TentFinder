package thedoctors05.tentfinder;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gregory on 2017-10-26.
 */

public class Tent implements Parcelable {

    public String Title;
    public String Longitude;
    public String Latitude;

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
