package com.itmo.lab6.server.managers;

import com.itmo.lab6.common.models.Person;
import com.itmo.lab6.common.interfaces.InCollectionManager;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentSkipListMap;


public class CollectionManager implements InCollectionManager {
    private ConcurrentSkipListMap<Long, Person> collection = new ConcurrentSkipListMap<>(); 
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;


    public ConcurrentSkipListMap<Long, Person> getCollection() {
        return collection;
    }
    
    public void setLastInitTime() {
        lastInitTime = LocalDateTime.now();
    }
    public void setLastSaveTime() {
        lastSaveTime = LocalDateTime.now();
    }

    public void setCollection(ConcurrentSkipListMap<Long, Person> collection) {
        this.collection = collection;
        setLastInitTime();
    }
    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }
    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }
    public void addElement(Person person) {
        collection.put(person.getId(), person);
    }
    public boolean update(long id, Person person) {
        if (collection.containsKey(id)) {
            collection.put(id, person); 
            return true;
        } else return false;
    }

    public void clear() {
        collection.clear();
    }
    public boolean remove(long id) {
        if (collection.containsKey(id)) {
            collection.remove(id); 
            return true;
        } else return false;
    }
}
