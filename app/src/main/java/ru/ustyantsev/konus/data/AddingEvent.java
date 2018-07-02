package ru.ustyantsev.konus.data;


import com.google.firebase.Timestamp;

public class AddingEvent {

        String title;
        String place;
        String info;
        Timestamp dateTime;

    public AddingEvent(String title, String place, String info, Timestamp dateTime) {
        this.title = title;
        this.place = place;
        this.info = info;
        this.dateTime = dateTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
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

    public Timestamp getDateTime() {
        return dateTime;
    }
}
