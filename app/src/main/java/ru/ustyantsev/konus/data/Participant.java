package ru.ustyantsev.konus.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Participant implements Parcelable{
    private String name;
    private String group;
    private boolean present;
    private String docId;

    public Participant(String name, String group, boolean present, String docId) {
        this.name = name;
        this.group = group;
        this.present = present;
        this.docId = docId;
    }

    protected Participant(Parcel in) {
        name = in.readString();
        group = in.readString();
        present = in.readByte() != 0;
        docId = in.readString();
    }

    public static final Creator<Participant> CREATOR = new Creator<Participant>() {
        @Override
        public Participant createFromParcel(Parcel in) {
            return new Participant(in);
        }

        @Override
        public Participant[] newArray(int size) {
            return new Participant[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public boolean isPresent() {
        return present;
    }

    public String getDocId() {
        return docId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(group);
        parcel.writeByte((byte) (present ? 1 : 0));
        parcel.writeString(docId);
    }
}

