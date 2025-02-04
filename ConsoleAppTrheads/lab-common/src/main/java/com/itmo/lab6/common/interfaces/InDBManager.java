package com.itmo.lab6.common.interfaces;


import java.util.concurrent.ConcurrentSkipListMap;
import com.itmo.lab6.common.models.Coordinates;
import com.itmo.lab6.common.models.Location;
import com.itmo.lab6.common.models.Person;
import com.itmo.lab6.common.util.Request;

public interface InDBManager {
    ConcurrentSkipListMap<Long, Person> getAllElementsFromDB(); 
    ConcurrentSkipListMap<Long, Person> getUserElements(int user_id);
    Integer getUserId(Request request);
    boolean insertNewUser(Request request);
    Integer insertNewLocation(Location location); 
    Integer insertNewCoordinates(Coordinates coordinates);
    Integer insertPerson(Person person, int coordinatesId, int locationId, int userId);
    boolean clearCollection(int userId);
    boolean removeByKey(int userId, long personId);
    boolean removeLower(int userId, long personId);
}
