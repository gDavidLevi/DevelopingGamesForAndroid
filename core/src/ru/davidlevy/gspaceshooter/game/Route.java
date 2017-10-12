package ru.davidlevy.gspaceshooter.game;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс Route (маршрут для ботов)
 *
 * @see BotEmitter эмиттер ботов
 */
public class Route {
    /* Список скоростей */
    private List<Vector2> velocityList;

    /* Список координат по оси Х */
    private List<Integer> coordXList;

    /* Начальная позиция */
    private Vector2 initialPosition;

    {
        this.velocityList = new ArrayList<Vector2>();
        this.coordXList = new ArrayList<Integer>();
    }

    public Route(Vector2 initialPosition) {
        this.initialPosition = initialPosition;
    }

    /**
     * Добавить точку изменения маршрута
     *
     * @param x        точка на оси Х
     * @param velocity скорость
     * @return Route
     */
    public Route addPoint(int x, Vector2 velocity) {
        coordXList.add(x);
        velocityList.add(velocity);
        return this;
    }

    /**
     * Возвращет скорость боту в зависомости от значения Х
     *
     * @param x точка на оси Х
     * @return Vector2
     */
    public Vector2 getMyVelocity(int x) {
        for (int i = coordXList.size() - 1; i >= 0; i--)
            if (x < coordXList.get(i)) return velocityList.get(i);
        throw new RuntimeException("Route Exception");
    }

    /**
     * Возвращает начальную позицию
     *
     * @return Vector2
     */
    public Vector2 getInitialPosition() {
        return this.initialPosition;
    }
}
