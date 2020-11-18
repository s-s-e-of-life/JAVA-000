package com.school.starter;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MyClass {
    private int id;
    private String name;

    private List<Student> students;

    public MyClass(int id, String name) {
        this.id = id;
        this.name = name;
        this.students = new ArrayList<>(5);
    }

    public void addStudent(Student student) {
        students.add(student);
    }
}
