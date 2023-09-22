package org.example.winnie.executor;

public class Result<V> {
    V value;

    private final Object lock = new Object();

    public Result() {
        this.value = null;
    }
    public void set(V val) {
        synchronized (lock) {
            value = val;
            lock.notifyAll();
        }
    }
    public V get() throws InterruptedException {
        synchronized (lock) {
            while (value == null) {
                lock.wait();
            }
            return value;
        }
    }
}
