package ru.davidlevi.gspaceshooter.game;

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
