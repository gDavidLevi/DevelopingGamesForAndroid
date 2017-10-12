package ru.davidlevy.gspaceshooter.game;

/**
 * Интерфейс Poolable
 * Помечает классы помещаемые в пул.
 */
public interface Poolable {
    /**
     * Активен ли объект?
     *
     * @return boolean
     */
    boolean isActive();
}
