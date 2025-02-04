package com.itmo.lab6.common.models;

import java.io.Serializable;

public class Coordinates implements Comparable<Coordinates>, Serializable {

    private Double x; //min x -442 
    private Integer y; //min y -258

    public Coordinates(Double x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public void setX(Double x) {
        this.x = x;
    }
    public void setY(Integer y) {
        this.y= y;
    }

    public Double getX() {
        return x;
    }
    public Integer getY() {
        return y;
    }
    @Override 
    public String toString() {
        return String.format("%f:%d", x, y);
    }
    @Override 
    public int compareTo(Coordinates coordinates) {
        if (coordinates == null) 
            return 1;
        int result = Double.compare(this.x, coordinates.getX()); 
        if (result == 0) 
            return Integer.compare(this.y, coordinates.getY());
        return result;
    }
}