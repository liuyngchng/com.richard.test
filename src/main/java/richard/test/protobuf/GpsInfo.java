package richard.test.protobuf;

import java.io.Serializable;

/**
 * Created by richard on 3/21/18.
 */
public class GpsInfo  implements Serializable{

    private long id;

    private float direction;

    private int altitude;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }
}
