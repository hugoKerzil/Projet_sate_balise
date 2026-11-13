package model.activity;

public interface Activity {

    void move();

    default void collect() {
        throw new UnsupportedOperationException("This operation is unsupported with this class.");
    }

    default void transmission() {
        throw new UnsupportedOperationException("This operation is unsupported with this class.");
    }
}
