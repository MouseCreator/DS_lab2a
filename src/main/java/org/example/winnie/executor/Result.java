package org.example.winnie.executor;

public class Result<V> {
    V value;

    public Result() {
        this.value = null;
    }
    public void set(V val) {
        value = val;
        notifyAll();
    }
    public V get() {
        while (value == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return value;
    }
}
