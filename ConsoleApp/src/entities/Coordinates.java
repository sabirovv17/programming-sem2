package entities;

/**
 * Class used to specify the coordinates of a Person object.
 * @see Person
 */
public class Coordinates {
    private static final double MIN_X = -442;
    private static final int MIN_Y = -258;

    private Double x;
    private Integer y;

    /**
     * Class constructor setting the coordinate values
     * @param x value of the X axis coordinate.
     * @param y value of the Y axis coordinate.
     */
    public Coordinates(Double x, Integer y) {
        this.x = (x != null && x > MIN_X) ? x : MIN_X;
        this.y = (y != null && y > MIN_Y) ? y : MIN_Y;
    }

    /**
     * Sets coordinate values from a CSV string.
     * @param input CSV string with coordinate values
     * @throws NumberFormatException if the input string is not formatted correctly
     */
    public void setValFromString(String input) throws NumberFormatException{
        String[] coords = input.split(";");
        this.x = Double.parseDouble(coords[0]);
        this.y = Integer.parseInt(coords[1]);
    }

    /**
     * @return value of the x coordinate
     */
    public Double getX() {
        return x;
    }

    /**
     * @return value of the y coordinate
     */
    public Integer getY() {
        return y;
    }
}
