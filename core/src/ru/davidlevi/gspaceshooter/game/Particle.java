package ru.davidlevi.gspaceshooter.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Клас Particle (частицы)
 *
 * @see Poolable помещается в пул объектов
 */
public class Particle implements Poolable {
    private Vector2 position;
    private Vector2 velocity;

    /* Цвет первой и второй частицы */
    private float r1, g1, b1, a1;
    private float r2, g2, b2, a2;

    /* Время частиц */
    private float currentTime;
    private float maxTimeOfLife;

    /* Размер первой и второй частицы */
    private float size1, size2;

    /* Активна ли частица? */
    private boolean active;

    public Particle() {
        position = new Vector2(0, 0);
        velocity = new Vector2(0, 0);
        size1 = 1.0f;
        size2 = 1.0f;
    }

    /**
     * @param x             позиция
     * @param y             позиция
     * @param vx            скорость
     * @param vy            скорость
     * @param maxTimeOfLife максимальное время жизни
     * @param size1         размер первой частицы
     * @param size2         размер второй частицы
     * @param r1            красный канал первой частицы
     * @param g1            зеленый канал первой частицы
     * @param b1            синий канал первой частицы
     * @param a1            альфа первой частицы
     * @param r2            красный канал второй частицы
     * @param g2            зеленый канал второй частицы
     * @param b2            синий канал второй частицы
     * @param a2            альфа второй частицы
     */
    public void init(float x, float y, float vx, float vy, float maxTimeOfLife, float size1, float size2, float r1, float g1, float b1, float a1, float r2, float g2, float b2, float a2) {
        this.currentTime = 0.0f;

        this.position.x = x;
        this.position.y = y;
        this.velocity.x = vx;
        this.velocity.y = vy;
        this.size1 = size1;
        this.size2 = size2;
        this.maxTimeOfLife = maxTimeOfLife;
        this.r1 = r1;
        this.r2 = r2;
        this.g1 = g1;
        this.g2 = g2;
        this.b1 = b1;
        this.b2 = b2;
        this.a1 = a1;
        this.a2 = a2;
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public void update(float dt) {
        this.currentTime += dt;
        position.mulAdd(velocity, dt);
        if (currentTime > maxTimeOfLife) deactivate();
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getR1() {
        return r1;
    }

    public float getG1() {
        return g1;
    }

    public float getB1() {
        return b1;
    }

    public float getA1() {
        return a1;
    }

    public float getR2() {
        return r2;
    }

    public float getG2() {
        return g2;
    }

    public float getB2() {
        return b2;
    }

    public float getA2() {
        return a2;
    }

    public float getCurrentTime() {
        return currentTime;
    }

    public float getMaxTime() {
        return maxTimeOfLife;
    }

    public float getSize1() {
        return size1;
    }

    public float getSize2() {
        return size2;
    }
}
