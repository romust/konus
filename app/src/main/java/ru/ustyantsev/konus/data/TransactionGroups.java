package ru.ustyantsev.konus.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class TransactionGroups implements Parcelable{
    private List<String> students;
    private String points;
    private String info;
    private String estimateId;
    private String groupId;

    public TransactionGroups(List<String> students, String points, String info, String estimateId, String groupId) {
        this.students = students;
        this.points = points;
        this.info = info;
        this.estimateId = estimateId;
        this.groupId = groupId;
    }

    protected TransactionGroups(Parcel in) {
        students = in.createStringArrayList();
        points = in.readString();
        info = in.readString();
        estimateId = in.readString();
        groupId = in.readString();
    }

    public static final Creator<TransactionGroups> CREATOR = new Creator<TransactionGroups>() {
        @Override
        public TransactionGroups createFromParcel(Parcel in) {
            return new TransactionGroups(in);
        }

        @Override
        public TransactionGroups[] newArray(int size) {
            return new TransactionGroups[size];
        }
    };

    public List<String> getStudents() {
        return students;
    }

    public String getPoints() {
        return points;
    }

    public String getInfo() {
        return info;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getEstimateId() {
        return estimateId;
    }

    public void setStudents(List<String> students) {
        this.students = students;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringList(students);
        parcel.writeString(points);
        parcel.writeString(info);
        parcel.writeString(estimateId);
        parcel.writeString(groupId);
    }
}
