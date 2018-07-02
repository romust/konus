package ru.ustyantsev.konus.data;

public class Transaction {
    private String title;
    private String info;
    private String points;

    public Transaction(String title, String info, String points) {
        this.title = title;
        this.info = info;
        this.points = points;
    }

    public String getTitle() {
        return title;
    }

    public String getInfo() {
        return info;
    }

    public String getPoints() {
        return points;
    }
}
