package ru.ustyantsev.konus.data;

import java.util.ArrayList;
import java.util.List;

public class Rating {
    private String ratingPosition;
    private String name;
    private String group;
    private String points;

    public Rating(String ratingPosition, String name, String group, String points){
        this.ratingPosition = ratingPosition;
        this.name = name;
        this.group = group;
        this.points = points;
    }

    public String getRatingPosition() {
        return ratingPosition;
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public String getPoints() {
        return points;
    }
}
