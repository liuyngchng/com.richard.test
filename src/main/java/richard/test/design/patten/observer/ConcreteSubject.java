package richard.test.design.patten.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Richard on 1/9/18.
 */
public class ConcreteSubject implements Subject {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(ConcreteSubject.class);

    private final List<Observer> observers;

    private float temperature;

    public float getTemperature() {
        return temperature;
    }

    private void temperatureChanged() {
        this.notifyObservers();
    }

    public void setTemperature(final float temperature) {
        this.temperature = temperature;
        this.temperatureChanged();
    }

    public ConcreteSubject() {
        this.observers = new ArrayList<Observer>();
    }

//    @Override
    public void registerObserver(final Observer o) {
        observers.add(o);
    }

//    @Override
    public void removeObserver(final Observer o) {
        if (observers.indexOf(o) >= 0) {
            observers.remove(o);
        }
    }

//    @Override
    public void notifyObservers() {
        for (final Observer o : observers) {
            LOGGER.info("notify observer {}", o.getId());
            o.update(temperature);
        }
    }
}
