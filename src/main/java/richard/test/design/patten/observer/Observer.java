package richard.test.design.patten.observer;

/**
 * Created by Richard on 1/9/18.
 */
public interface Observer {

    /**
     * Get observer id.
     * @return
     */
    String getId();

    /**
     * Update temperature observed.
     * @param temperature
     */
    void update(float temperature);
}
