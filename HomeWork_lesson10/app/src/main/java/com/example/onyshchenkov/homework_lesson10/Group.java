package com.example.onyshchenkov.homework_lesson10;

import java.util.ArrayList;

public class Group {
    public String group_number;
    public ArrayList<Student> students;

    public Group(){
    }

    public Group(String number, ArrayList<Student> students) {
        this.group_number = number;
        this.students = students;
    }
}
