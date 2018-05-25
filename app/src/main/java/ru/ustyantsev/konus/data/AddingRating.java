package ru.ustyantsev.konus.data;

public class AddingRating {
    private String name;
    private String group;
    private Integer points;

    public AddingRating(String name, String group, Integer points){
        this.name = name;
        this.group = group;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public Integer getPoints() {
        return points;
    }
}
