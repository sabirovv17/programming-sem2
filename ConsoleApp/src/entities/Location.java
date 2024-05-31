package entities;

/**
 * Class used to specify the location of a Person object.
 * @see Person
 */
public class Location implements Comparable<Location>{
    private float x;
    private Double y; //Поле не может быть null
    private Long z; //Поле не может быть null

    /**
     * Class constructor setting the location values
     * @param x value of the X axis coordinate.
     * @param y value of the Y axis coordinate.
     * @param z value of the Z axis coordinate.
     */
    public Location(float x, Double y, Long z) {
        this.x = x;
        this.y = (y != null) ? y : 0.0;
        this.z = (z != null) ? z : 0L;
    }

    /**
     * Sets location values from a CSV string.
     * @param input CSV string with location values
     * @throws NumberFormatException if the input string is not formatted correctly
     */
    public void setValFromString(String input) throws NumberFormatException{
        String[] locs = input.split(";");
        x = Float.parseFloat(locs[0]);
        y = Double.parseDouble(locs[1]);
        z = Long.parseLong(locs[2]);
    }
    /**
     * @return value of the x coordinate
     */
    public float getX() {
        return x;
    }

    /**
     * @return value of the y coordinate
     */
    public Double getY() {
        return y;
    }

    /**
     * @return value of the z coordinate
     */
    public Long getZ() {
        return z;
    }

    /**
     * Compares two Location objects.
     * @param loc the Location object to be compared.
     * @return integer value of the sum of all the axis coordinate comparisons between two Location objects.
     */
    @Override
    public int compareTo(Location loc) {
        return Float.compare(x, loc.getX()) + Double.compare(y, loc.getY() + Long.compare(z, loc.getZ()));
    }

    /**
     * @return String representation of a Location object.
     */
    @Override
    public String toString(){
        return this.getX() + ";" + this.getY() + ";" + this.getZ();
    }
}
