package ru.ustyantsev.konus.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Estimate implements Parcelable {
    private String title;
    private Long present;
    private Long missing;
    private String estimateId;
    private boolean approved;

    public Estimate(String title, Long present, Long missing, String estimateId, boolean approved) {
        this.title = title;
        this.present = present;
        this.missing = missing;
        this.estimateId = estimateId;
        this.approved = approved;
    }


    protected Estimate(Parcel in) {
        title = in.readString();
        if (in.readByte() == 0) {
            present = null;
        } else {
            present = in.readLong();
        }
        if (in.readByte() == 0) {
            missing = null;
        } else {
            missing = in.readLong();
        }
        estimateId = in.readString();
        approved = in.readByte() != 0;
    }

    public static final Creator<Estimate> CREATOR = new Creator<Estimate>() {
        @Override
        public Estimate createFromParcel(Parcel in) {
            return new Estimate(in);
        }

        @Override
        public Estimate[] newArray(int size) {
            return new Estimate[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public Long getPresent() {
        return present;
    }

    public Long getMissing() {
        return missing;
    }

    public String getEstimateId() {
        return estimateId;
    }

    public boolean isApproved() {
        return approved;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(title);
        if (present == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(present);
        }
        if (missing == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(missing);
        }
        parcel.writeString(estimateId);
        parcel.writeByte((byte) (approved ? 1 : 0));
    }


}
