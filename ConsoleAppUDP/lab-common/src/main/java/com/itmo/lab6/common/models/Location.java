package com.itmo.lab6.common.models;

import java.io.Serializable;

public class Location implements Comparable<Location>, Serializable{
    private float x;
    private Double y; //Поле не может быть null
    private Long z; //Поле не может быть null

    public Location(float x, Double y, long z) {
        this.x = x; 
        this.y = y; 
        this.z = z;
    }   
    public void setX(float x) {
        this.x = x;
    }
    public void setY(Double y) {
        this.y = y;
    }
    public void setZ(Long z) {
        this.z = z;
    }   
    
    public float getX() {
        return x;
    }
    public Double getY() {
        return y;
    }
    public Long getZ() {
        return z;
    }
    @Override
    public int compareTo(Location location) {
        if (location == null) return 1;
        return Float.compare(this.x, location.getX()) + Double.compare(this.y, location.getY()) + Long.compare(this.z, location.getZ());
    }

    @Override
    public String toString() {
        return String.format("x:%f\ny:%f\nz:%d", x, y, z);
    }
    public String forCSV() {
        return String.format("%f:%f:%d", x, y, z);
    }

}
