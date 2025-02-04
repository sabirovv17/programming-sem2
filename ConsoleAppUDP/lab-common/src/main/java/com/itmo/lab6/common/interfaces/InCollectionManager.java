package com.itmo.lab6.common.interfaces;

import java.util.TreeMap;
import java.time.LocalDateTime;

import com.itmo.lab6.common.models.Person;

public interface InCollectionManager {
    TreeMap<Long, Person> getCollection();
    void setCollection(TreeMap<Long, Person> collection); 
    LocalDateTime getLastInitTime();
    LocalDateTime getLastSaveTime();
    void setLastInitTime();
    void setLastSaveTime();
    void addElement(Person person);
    boolean update(long id, Person person);
    void clear(); 
    boolean remove(long id);
}
