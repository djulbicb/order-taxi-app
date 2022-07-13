package com.djulb;

import org.springframework.stereotype.Service;

@Service
public class ApplicationPropertyService {

    int delay = 5000;

    public String getApplicationProperty(){
        //get your value here
        return delay + "";
    }

    public void set(int value) {
        delay = value;
    }
}