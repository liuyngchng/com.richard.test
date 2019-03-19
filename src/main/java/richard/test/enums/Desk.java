package richard.test.enums;

/**
 * Created by Richard on 11/15/16.
 */
public enum Desk {
    DINNER_DESK("dinner", "green"),
    BOOK_DESK("reading","yellow");

    private String type;

    private String color;
    Desk(){}

    Desk(String type, String color) {
        this.type = type;
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}