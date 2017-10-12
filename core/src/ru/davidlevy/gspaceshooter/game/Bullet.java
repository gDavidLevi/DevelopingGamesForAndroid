package ru.davidlevy.gspaceshooter.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Класс Bullet (пуля)
 *
 * @see Poolable помещается в пул объектов
 */
public class Bullet implements Poolable {
    /* Тип пули */
    private BulletType bulletType;
    /* Пуля игрока? */
    private boolean isPlayersBullet;
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;

    public Bullet() {
        this.position = new Vector2(0.0f, 0.0f);
        this.velocity = new Vector2(0.0f, 0.0f);
        this.active = false;
    }

    /**
     * Активация
     *
     * @param bulletType      тип пули
     * @param isPlayersBullet пуля игрока?
     * @param x               положение
     * @param y               положение
     * @param vx              скорость
     * @param vy              скорость
     */
    public void activate(BulletType bulletType, boolean isPlayersBullet, float x, float y, float vx, float vy) {
        this.bulletType = bulletType;
        this.isPlayersBullet = isPlayersBullet;
        position.set(x, y);
        velocity.set(vx, vy);
        active = true;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        if (position.x > SpaceGame.SCREEN_WIDTH || position.x < 0 || position.y < 0 || position.y > SpaceGame.SCREEN_HEIGHT) {
            deactivate();
        }
    }

    public BulletType getBulletType() {
        return this.bulletType;
    }

    public boolean isPlayersBullet() {
        return this.isPlayersBullet;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public boolean isActive() {
        return this.active;
    }

    public void deactivate() {
        this.active = false;
    }
}
