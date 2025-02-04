package com.itmo.lab6.common.interfaces;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentSkipListMap;

import com.itmo.lab6.common.models.Person;

public interface InCollectionManager {
    ConcurrentSkipListMap<Long, Person> getCollection();
    void setCollection(ConcurrentSkipListMap<Long, Person> collection); 
    LocalDateTime getLastInitTime();
    LocalDateTime getLastSaveTime();
    void setLastInitTime();
    void setLastSaveTime();
    void addElement(Person person);
    boolean update(long id, Person person);
    void clear(); 
    boolean remove(long id);
}
