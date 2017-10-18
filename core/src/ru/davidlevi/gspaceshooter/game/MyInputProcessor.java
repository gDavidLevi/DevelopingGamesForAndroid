package ru.davidlevi.gspaceshooter.game;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс MyInputProcessor (процессор ввода)
 *
 * @see InputProcessor receive input events from the keyboard and the touch screen (mouse on the desktop).
 */
public class MyInputProcessor implements InputProcessor {
    /* Класс TouchInfo (информация о касании) */
    private class TouchInfo {
        int x;
        int y;
        boolean touched;

        /* Установить координаты касания и есть ли касание */
        void set(int x, int y, boolean touched) {
            this.x = x;
            this.y = y;
            this.touched = touched;
        }
    }
    /* -====================================- */

    /* Игровое пространство */
    private SpaceGame spaceGame;

    /* Словарь */
    private HashMap<Integer, TouchInfo> map;

    /* Экранные координаты */
    private Vector2 screenCoord;

    {
        map = new HashMap<Integer, TouchInfo>();
        screenCoord = new Vector2(0, 0);
    }

    public MyInputProcessor(SpaceGame spaceGame) {
        this.spaceGame = spaceGame;
        /* Будем следить за 5 касаниями максимум */
        for (int touchId = 0; touchId < 5; touchId++)
            map.put(touchId, new TouchInfo());
    }

    /**
     * Очистка словаря
     */
    public void clear() {
        for (int touchId = 0; touchId < 5; touchId++)
            map.put(touchId, new TouchInfo());
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int touchId, int button) {
        /* Экранные координаты преобразуем в игровые (мировые) */
        spaceGame.getViewport().unproject(screenCoord.set(screenX, screenY));
        map.get(touchId).set((int) screenCoord.x, (int) screenCoord.y, true);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int touchId) {
        /* Экранные координаты преобразуем в игровые (мировые) */
        spaceGame.getViewport().unproject(screenCoord.set(screenX, screenY));
        map.get(touchId).set((int) screenCoord.x, (int) screenCoord.y, true);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int touchId, int button) {
        map.get(touchId).x = 0;
        map.get(touchId).y = 0;
        map.get(touchId).touched = false;
        return false;
    }

    public boolean isTouched(int touchId) {
        return map.get(touchId).touched;
    }

    public int getX(int touchId) {
        return map.get(touchId).x;
    }

    public int getY(int touchId) {
        return map.get(touchId).y;
    }

    /**
     * Возвращает экранные координаты нажания в области прямоугольника
     *
     * @param rectangle прямоугольник
     * @return touchId
     */
    public int isTouchedInArea(Rectangle rectangle) {
        return isTouchedInArea((int) rectangle.x, (int) rectangle.y, (int) rectangle.width, (int) rectangle.height);
    }

    /**
     * Возвращает экранные координаты нажания в область касания
     *
     * @param x      позиция
     * @param y      позиция
     * @param width  ширина
     * @param height высота
     * @return touchId
     */
    public int isTouchedInArea(int x, int y, int width, int height) {
        for (Map.Entry<Integer, TouchInfo> oneTouch : map.entrySet()) {
            if (oneTouch.getValue().touched) {
                int touchId = oneTouch.getKey();
                TouchInfo info = oneTouch.getValue();
                if (info.x > x && info.x < x + width && info.y > y && info.y < y + height)
                    return touchId;
            }
        }
        return -1;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
