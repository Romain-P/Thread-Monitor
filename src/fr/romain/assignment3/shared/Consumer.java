package fr.romain.assignment3.shared;

/**
 * Throwable Consumer
 * No implementation provided by JDK
 *
 * @param <T>   object to consume
 */
@FunctionalInterface
public interface Consumer<T> {
    void accept(T obj) throws InterruptedException;
}
