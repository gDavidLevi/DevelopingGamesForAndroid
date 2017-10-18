package ru.davidlevi.gspaceshooter.game;

import com.badlogic.gdx.Gdx;

/**
 * Обработчик ввода
 */
public class InputHandler {
    /**
     * Было ли касание?
     *
     * @return boolean
     */
    public static boolean isTouched() {
        return Gdx.input.isTouched();
    }

    /**
     * Возвращает экранную координату по оси Х
     *
     * @return int
     */
    public static int getX() {
        return Gdx.input.getX();
    }

    /**
     * Возвращает экранную координату по оси Y
     *
     * @return int
     */
    public static int getY() {
        return 720 - Gdx.input.getY();
    }
}
