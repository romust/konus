package ru.ustyantsev.konus.ui.Fragments;


public class Student {

    private String name;
    private String group;
    private int points;
    private String id;

    public Student(){}
    public Student(String name, String group, int points){}

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return id;
    }
    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public int getPoints() {
        return points;
    }

}
