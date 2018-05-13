package com.example.kevinbarbian14.dispatchaces;

/**
 * Custom Ride Object class used for holding all information about a user's ride.
 * Date: 5/13/2018
 *
 * @author Tyler May, Kevin Barbian, Megan Janssen, Tan Nguyen
 */

public class RideInfo {
    public String email; //the users email
    public String start; //start location
    public String end; //destination
    public int numRiders; //number of riders
    public String endTime; //timestamp of when the ride was fulfilled
    public String time; //start time
    public int waitTime; //wait time

    public String getEmail() {
        return email;
    }

    public void setEndTime(String time) {
        endTime = time;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public int getNumRiders() {
        return numRiders;
    }

    public String getTime() {
        return time;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setNumRiders(int numRiders) {
        this.numRiders = numRiders;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public RideInfo(String email, String start, String end, int numRiders, String time, int waitTime, String endTime) {
        this.email = email;
        this.start = start;
        this.end = end;
        this.numRiders = numRiders;
        this.endTime = endTime;
        this.time = time;
        this.waitTime = waitTime;
    }

    public RideInfo() {

    }

}
