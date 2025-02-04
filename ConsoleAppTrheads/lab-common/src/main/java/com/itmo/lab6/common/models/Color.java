package com.itmo.lab6.common.models;


public enum Color {
    RED,
    BLACK,
    BLUE,
    WHITE,
    BROWN;
    public static String getNames() {
        StringBuilder nameList = new StringBuilder(); 
        for (Color color : Color.values()) {
            nameList.append(color.name()).append(", ");
        }
        return nameList.substring(0, nameList.length() - 1);
    }
    public static boolean validateColor(String value) {
        for (Color color : Color.values()) {
            if (color.name().equalsIgnoreCase(value)) 
                return true;
        }
        return false;
    }
}


