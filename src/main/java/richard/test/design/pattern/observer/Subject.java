package richard.test.design.pattern.observer;

/**
 * Created by Richard on 1/9/18.
 */
public interface Subject {

    void registerObserver(Observer o);

    void removeObserver(Observer o);

    void notifyObservers();

}
