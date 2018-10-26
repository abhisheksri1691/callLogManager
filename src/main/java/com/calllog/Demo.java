package com.calllog;

import com.calllog.dbhelper.DatabaseManager;

import java.util.Date;
import java.util.logging.Logger;


//import java.util.Date;

public class Demo {

int y=10;

    public static void main(String[] er)
    {

        Age age = new Age() {
            @Override
            public int getAge() {
                return x;
            }
        };
        System.out.println(age.getAge());

        System.out.println(age.getClass());
    }

    public void ownFunction()
    {
        System.out.println("i am method of Demo class");
    }
}
