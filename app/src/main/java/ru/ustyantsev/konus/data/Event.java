package ru.ustyantsev.konus.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;

public class Event implements Parcelable{
    String docId;
    String title;
    String place;
    String info;
    String date;
    String time;
    Long dateTime;

    public Event(String docId, String title, String place, String info, String date, String time, Long dateTime) {
        this.docId = docId;
        this.title = title;
        this.place = place;
        this.info = info;
        this.date = date;
        this.time = time;
        this.dateTime = dateTime;
    }

    protected Event(Parcel in) {
        docId = in.readString();
        title = in.readString();
        place = in.readString();
        info = in.readString();
        date = in.readString();
        time = in.readString();
        dateTime = in.readLong();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public String getDocId() {
        return docId;
    }

    public String getTitle() {
        return title;
    }

    public String getPlace() {
        return place;
    }

    public String getInfo() {
        return info;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public Long getDateTime() {
        return dateTime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(docId);
        parcel.writeString(title);
        parcel.writeString(place);
        parcel.writeString(info);
        parcel.writeString(date);
        parcel.writeString(time);
        parcel.writeLong(dateTime);
    }
}
