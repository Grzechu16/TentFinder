package thedoctors05.tentfinder;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gregory on 2017-10-26.
 */

public class RowBean implements Parcelable {

    public String Title;
    public String Longitude;
    public String Latitude;

    public RowBean() {

    }

    public RowBean(String title, String longitude, String latitude) {
        Title = title;
        Longitude = longitude;
        Latitude = latitude;
    }

    protected RowBean(Parcel in) {
        Title = in.readString();
        Longitude = in.readString();
        Latitude = in.readString();
    }

    public static final Creator<RowBean> CREATOR = new Creator<RowBean>() {
        @Override
        public RowBean createFromParcel(Parcel in) {
            return new RowBean(in);
        }

        @Override
        public RowBean[] newArray(int size) {
            return new RowBean[size];
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
