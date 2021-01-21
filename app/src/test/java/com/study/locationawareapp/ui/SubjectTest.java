package com.study.locationawareapp.ui;

import org.junit.Before;
import org.junit.Test;

import java.util.Observable;
import java.util.Observer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class SubjectTest implements Observer {

    private Subject subject;
    private boolean connected;

    @Before
    public void setUp() {
        this.subject = new Subject(0);
        connected = false;
    }


    @Test
    public void subjectTester() {

        subject.attachObserver(this);

        assertTrue(!connected);

        subject.notifyObservers();

        assertTrue(connected);

        connected = false;

        subject.detachObserver(this);

        subject.notifyObservers();

        assertTrue(!connected);
    }

    public void connect(){
        connected = true;
    }

    @Override
    public void update(Observable o, Object arg) {
        connect();
    }
}