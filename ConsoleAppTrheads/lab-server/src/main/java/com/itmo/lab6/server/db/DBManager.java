package com.itmo.lab6.server.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.ConcurrentSkipListMap;

import org.postgresql.core.SqlCommand;
import org.postgresql.util.PGobject;
import java.util.Date;
import java.time.LocalDateTime;

import com.itmo.lab6.common.interfaces.InDBManager;
import com.itmo.lab6.common.models.Color;
import com.itmo.lab6.common.models.Coordinates;
import com.itmo.lab6.common.models.Location;
import com.itmo.lab6.common.models.Person;
import com.itmo.lab6.common.util.Request;

public class DBManager implements InDBManager {
    private Connection connection;

    private final String ALL_ELEMENTS = "SELECT person.person_id, person_name, coordinates.coord_x, coordinates.coord_y, person.creationDate, person.height, person.weight, person.passportID, person.color, location.x, location.y, location.z " + 
    "FROM person INNER JOIN coordinates on person.coordinates_id = coordinates.coordinates_id INNER JOIN location on person.location_id=location.location_id";
    private final String USER_COLLECTION = "SELECT person.person_id, person_name, coordinates.coord_x, coordinates.coord_y, person.creationDate, person.height, person.weight, person.passportID, person.color, location.x, location.y, location.z " + 
    "FROM person INNER JOIN coordinates on person.coordinates_id = coordinates.coordinates_id INNER JOIN location on person.location_id=location.location_id where person.user_id = ?";
    private final String GET_USER_ID = "SELECT user_id from users WHERE login = ? and password = ?";
    private final String NEW_USER = "INSERT INTO users(login, password) VALUES (?, ?)";
    private final String INSERT_PERSON = "INSERT INTO person(person_name, coordinates_id, height, weight, passportID, color, location_id, user_id) values (?, ?, ?, ?, ?, ?, ?, ?) returning person_id";
    private final String INSERT_COORDINATES = "INSERT INTO coordinates(coord_x, coord_y) values (?, ?) returning coordinates_id"; 
    private final String INSERT_LOCATION = "INSERT INTO location(x, y, z) values (?, ?, ?) returning location_id";
    private final String CLEAR = "DELETE FROM person where user_id = ?";
    private final String DELETE_COORDINATES = "DELETE FROM coordinates where coordinates_id IN (SELECT coordinates_id FROM person WHERE user_id = ?)";
    private final String DELETE_LOCATION = "DELETE FROM location where location_id IN (SELECT location_id FROM person WHERE user_id = ?)";
    private final String REMOVE_BY_KEY = "DELETE FROM person WHERE user_id = ? AND person_id = ?";
    private final String DELETE_COORDINATES_BY_ID = "DELETE FROM coordinates WHERE coordinates_id IN (SELECT coordinates_id FROM person WHERE user_id = ? and person_id = ?)";
    private final String DELETE_LOCATION_BY_ID = "DELETE FROM location WHERE location_id IN (SELECT location_id FROM person WHERE user_id = ? and person_id = ?)";
    private final String REMOVE_LOWER = "DELETE from person WHERE person_id < ?  AND user_id = ";
    
    public Connection connectToDatabase() {
        Properties info = new Properties();
        try {
            info.load(new FileInputStream("db.cfg")); 
            return DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/studs", info);
        } catch (SQLException exception) {
            exception.printStackTrace(); //TODO add logs;
        } catch (IOException exception) {
            exception.printStackTrace(); //TODO add logss
        }
        return null;
    }

    public ConcurrentSkipListMap<Long, Person> getAllElementsFromDB() {
        ConcurrentSkipListMap<Long, Person> collection = new ConcurrentSkipListMap<>();
        connection = connectToDatabase();
        if (connection == null) {
            return collection;
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(ALL_ELEMENTS); 
            ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                long id = resultSet.getLong("person_id");
                String name = resultSet.getString("person_name");
                Coordinates coordinates = new Coordinates(resultSet.getDouble("coord_x"), resultSet.getInt("coord_y"));
                Date creationDate = resultSet.getDate("creationDate"); 
                long height = resultSet.getLong("height"); 
                Long weight = resultSet.getLong("weight");
                String passportID = resultSet.getString("passportID"); 
                Color color = Color.valueOf(resultSet.getString("color"));
                Location location = new Location(resultSet.getFloat("x"), resultSet.getDouble("y"), resultSet.getLong("z"));
                Person person = new Person(id,name, coordinates, toLocalDateTime(creationDate), height, weight, passportID, color, location);
                collection.put(id, person);

            }
        } catch (SQLException exception) {
            exception.printStackTrace(); //TODO add logs
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return collection;
    }  



    private LocalDateTime toLocalDateTime(Date date) {
        return new java.sql.Timestamp(date.getTime()).toLocalDateTime();
    }

    public ConcurrentSkipListMap<Long, Person> getUserElements(int user_id) {
        ConcurrentSkipListMap<Long, Person> collection = new ConcurrentSkipListMap<>();
        connection = connectToDatabase();
        if (connection == null) {
            try {
                connection.close(); 
            }catch (SQLException exception) {}
            return collection;
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(USER_COLLECTION)) {
            preparedStatement.setInt(1, user_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    long id = resultSet.getLong("person_id");
                    String name = resultSet.getString("person_name");
                    Coordinates coordinates = new Coordinates(resultSet.getDouble("coord_x"), resultSet.getInt("coord_y"));
                    Date creationDate = resultSet.getDate("creationDate"); 
                    long height = resultSet.getLong("height"); 
                    Long weight = resultSet.getLong("weight");
                    String passportID = resultSet.getString("passportID"); 
                    Color color = Color.valueOf(resultSet.getString("color"));
                    Location location = new Location(resultSet.getFloat("x"), resultSet.getDouble("y"), resultSet.getLong("z"));
                    Person person = new Person(id,name, coordinates, toLocalDateTime(creationDate), height, weight, passportID, color, location);
                    collection.put(id, person);
                }
            }catch(SQLException exception) {
                exception.printStackTrace(); //TODO logs
            } finally {
                try {
                    connection.close();
                }catch (SQLException exc) {
                    exc.printStackTrace(); //TODO logs
                } 
            }
        } catch(SQLException exception) {
            exception.printStackTrace(); //TODO logs
        }
        return collection;
    }


    public Integer getUserId(Request request) {
        connection = connectToDatabase();
        if (connection == null) {
            try {
                connection.close(); 
            }catch (SQLException exception) {}
            return null;
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_ID)) {
            String login = request.getLogin();
            String password = Encryptor.md5(request.getPassword());
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("user_id");
                }
            } catch (SQLException exception) {
                exception.printStackTrace(); //TODO add logs
            } finally {
                try {
                    connection.close();
                } catch (SQLException exc) {
                    exc.printStackTrace();
                } 
            }
        } catch (SQLException exception) {
            exception.printStackTrace(); //TODO add logs
        }
        return null;
    }

    public boolean insertNewUser(Request request) {
        connection = connectToDatabase();
        if (connection == null) {
            try {
                connection.close(); 
            }catch (SQLException exception) {}
            return false;
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(NEW_USER)) {
            preparedStatement.setString(1, request.getLogin()); 
            preparedStatement.setString(2, Encryptor.md5(request.getPassword()));
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch(SQLException exception) {

        }finally {
            try {
                connection.close();
            } catch (SQLException exception2) {

            }
        }
        return false;
    }
    public Integer insertNewCoordinates(Coordinates coordinates) {
        connection = connectToDatabase();
        if (connection == null) {
            try {
                connection.close(); 
            }catch (SQLException exception) {}
            return null;
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_COORDINATES, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setDouble(1, coordinates.getX()); 
            preparedStatement.setInt(2, coordinates.getY());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    }
                }
            }
        } catch(SQLException exception) {
            exception.printStackTrace();
        } finally {
            try {
                connection.close(); 
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public Integer insertNewLocation(Location location) {
        connection = connectToDatabase();
        if (connection == null) {
            try {
                connection.close(); 
            }catch (SQLException exception) {}
            return null;
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_LOCATION, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setFloat(1, location.getX());
            preparedStatement.setDouble(2, location.getY());
            preparedStatement.setLong(3, location.getZ());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    }
                }
            }
        } catch(SQLException exception) {
            exception.printStackTrace();
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public Integer insertPerson(Person person, int coordinatesId, int locationId, int userId) {
        connection = connectToDatabase();
        if (connection == null) {
            try {
                connection.close(); 
            }catch (SQLException exception) {}
            return null;
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PERSON, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, person.getName()); 
            preparedStatement.setInt(2, coordinatesId); 
            preparedStatement.setLong(3, person.getHeight());
            preparedStatement.setLong(4, person.getWeight());
            preparedStatement.setString(5, person.getPassportID());
            PGobject colorObject = new PGobject();
            colorObject.setType("color"); 
            colorObject.setValue(person.getColor().toString());
            preparedStatement.setObject(6, colorObject); 
            preparedStatement.setInt(7, locationId); 
            preparedStatement.setInt(8, userId); 
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) 
                        return resultSet.getInt(1);
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            try {
                connection.close(); 
            } catch (SQLException e) {
                e.printStackTrace(); 
            }
        }
        return null;
    } 
    public boolean clearCollection(int userId) {
        connection = connectToDatabase();
        if (connection == null) {
            try {
                connection.close(); 
            }catch (SQLException exception) {}
            return false;
        }
        boolean isSuccess = false;
        try {
            connection.setAutoCommit(false); 
            try (PreparedStatement deleteCoordinates = connection.prepareStatement(DELETE_COORDINATES);
                PreparedStatement deleteLocation = connection.prepareStatement(DELETE_LOCATION); 
                PreparedStatement deletePerson = connection.prepareStatement(CLEAR)) {
                
                deletePerson.setInt(1, userId);
                int rowsAffected = deletePerson.executeUpdate();

                deleteCoordinates.setInt(1, userId);
                deleteCoordinates.executeUpdate();

                deleteLocation.setInt(1, userId);
                deleteLocation.executeUpdate(); 

                if (rowsAffected > 0)  
                    isSuccess = true;
                
                connection.commit();

            }catch (SQLException exception) {
                connection.rollback();
                exception.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }
    public boolean removeByKey(int userId, long personId) {
        connection = connectToDatabase();
        if (connection == null) {
            try {
                connection.close(); 
            }catch (SQLException exception) {}
            return false;
        }
        boolean isSuccess = false; 
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement deleteCoordinates = connection.prepareStatement(DELETE_COORDINATES_BY_ID); 
                PreparedStatement deleteLocation = connection.prepareStatement(DELETE_LOCATION_BY_ID); 
                PreparedStatement deletePerson = connection.prepareStatement(REMOVE_BY_KEY)) {
                
                deletePerson.setInt(1, userId);
                deletePerson.setLong(2, personId);
                int rowsAffected = deletePerson.executeUpdate();

                deleteCoordinates.setInt(1, userId);
                deleteCoordinates.setLong(2, personId);
                deleteCoordinates.executeUpdate();

                deleteLocation.setInt(1, userId);
                deleteLocation.setLong(2, personId);
                deleteLocation.executeUpdate();

                if (rowsAffected > 0) isSuccess = true;

                connection.commit(); 
            } catch(SQLException exception) {
                exception.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }catch (SQLException exception) {
            exception.printStackTrace();
        }finally {
            try {
                connection.close();
            }catch (SQLException exc) {
                exc.printStackTrace();
            } 
        }
        return isSuccess;
    }
    public boolean removeLower(int userId, long personId) {
        connection = connectToDatabase();
        if (connection == null) {
            try {
                connection.close(); 
            }catch (SQLException exception) {}
            return false;
        }
        boolean isSuccess = false;
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement deleteCoordinates = connection.prepareStatement(DELETE_COORDINATES_BY_ID); 
                PreparedStatement deleteLocation = connection.prepareStatement(DELETE_LOCATION_BY_ID); 
                PreparedStatement removeLower = connection.prepareStatement(REMOVE_LOWER)) {
                removeLower.setInt(1, userId);
                removeLower.setLong(2, personId);
                int rowsAffected = removeLower.executeUpdate();
                    
                deleteCoordinates.setInt(1, userId);
                deleteCoordinates.setLong(2, personId);
                deleteCoordinates.executeUpdate();

                deleteLocation.setInt(1, userId);
                deleteLocation.setLong(2, personId);
                deleteLocation.executeUpdate();

                if (rowsAffected > 0) isSuccess = true;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }
}
