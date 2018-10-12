package com.example.onyshchenkov.homework_lesson9;

import java.util.ArrayList;

public class Group {
    public int number;
    public ArrayList<Student> students;

    public Group(){
    }

    public Group(int number, ArrayList<Student> students) {
        this.number = number;
        this.students = students;
    }
}
