package com.school.starter;

import lombok.Data;

import java.util.List;

@Data
public class MySchool {

    private List<MyClass> myClasses;

    public MySchool(List<MyClass> myClasses) {
        this.myClasses = myClasses;
    }
}
