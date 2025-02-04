package com.itmo.lab6.common.util;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class IdGenerator {
    private static final Random RANDOM = new Random();
    private static final int ID_BOUND = 10000;
    private static Set<Integer> ids = new HashSet<>(); 
    public static int generateUniqueRandomId() {
        int uniqueId; 
        do {
            uniqueId = RANDOM.nextInt(ID_BOUND); 
        } while (!ids.add(uniqueId)); 
        return uniqueId;
    }
    public static Set<Integer> getIds() {
        return ids;
    }
}
