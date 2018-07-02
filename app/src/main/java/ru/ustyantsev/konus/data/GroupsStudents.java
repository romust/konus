package ru.ustyantsev.konus.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.SectionIndexer;

public class GroupsStudents implements Parcelable {
    String name;
    String group;
    boolean select;
    String docId;

    public GroupsStudents(String name, String group, boolean select, String docId) {
        this.name = name;
        this.group = group;
        this.select = select;
    }

    protected GroupsStudents(Parcel in) {
        name = in.readString();
        group = in.readString();
        select = in.readByte() != 0;
    }

    public static final Creator<GroupsStudents> CREATOR = new Creator<GroupsStudents>() {
        @Override
        public GroupsStudents createFromParcel(Parcel in) {
            return new GroupsStudents(in);
        }

        @Override
        public GroupsStudents[] newArray(int size) {
            return new GroupsStudents[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(group);
        parcel.writeByte((byte) (select ? 1 : 0));
    }
}
