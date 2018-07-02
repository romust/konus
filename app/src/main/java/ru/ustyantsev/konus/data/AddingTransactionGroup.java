package ru.ustyantsev.konus.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class AddingTransactionGroup implements Parcelable{
    private List<String> students;
    private Long points;
    private String info;

    public AddingTransactionGroup(List<String> students, Long points, String info) {
        this.students = students;
        this.points = points;
        this.info = info;
    }

    protected AddingTransactionGroup(Parcel in) {
        students = in.createStringArrayList();
        points = in.readLong();
        info = in.readString();
    }

    public static final Parcelable.Creator<AddingTransactionGroup> CREATOR = new Parcelable.Creator<AddingTransactionGroup>() {
        @Override
        public AddingTransactionGroup createFromParcel(Parcel in) {
            return new AddingTransactionGroup(in);
        }

        @Override
        public AddingTransactionGroup[] newArray(int size) {
            return new AddingTransactionGroup[size];
        }
    };

    public List<String> getStudents() {
        return students;
    }

    public Long getPoints() {
        return points;
    }

    public String getInfo() {
        return info;
    }

    public void setStudents(List<String> students) {
        this.students = students;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringList(students);
        parcel.writeLong(points);
        parcel.writeString(info);
    }
}