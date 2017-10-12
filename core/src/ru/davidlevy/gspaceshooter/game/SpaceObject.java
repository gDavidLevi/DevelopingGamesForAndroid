package ru.davidlevy.gspaceshooter.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

/**
 * Класс SpaceObject (космический объект)
 */
public abstract class SpaceObject {
    /* Экран игры */
    protected GameScreen gameScreen;

    /* Текстура */
    protected TextureRegion texturePlayer;

    /* Положение */
    protected Vector2 position;

    /* Скорость */
    protected Vector2 velocity;

    /* Текущее здоровье */
    protected int currentHealth;

    /* Максимальное здоровье */
    protected int maxHealth;

    /* Реакция на повреждение */
    protected float damageReaction;

    /* Область контакта */
    protected Circle hitArea;

    /* Антивен ли? */
    protected boolean active;

    /**
     * Отрисовка
     *
     * @param batch SpriteBatch
     */
    public abstract void render(SpriteBatch batch);

    /**
     * Обновление
     *
     * @param dt дельта времени
     */
    public abstract void update(float dt);

    /**
     * Destroy
     */
    public abstract void onDestroy();

    /**
     * Нанести урок определенное количество раз
     *
     * @param damage раз
     * @return boolean
     */
    public boolean takeDamage(int damage) {
        currentHealth -= damage;
        damageReaction += 0.2f;
        if (damageReaction > 1.0f) damageReaction = 1.0f;
        if (currentHealth <= 0) {
            onDestroy();
            return true;
        }
        return false;
    }

    public void deactivate() {
        this.active = false;
    }

    public Circle getHitArea() {
        return this.hitArea;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public Vector2 getVelocity() {
        return this.velocity;
    }

    public int getMaxHealth() {
        return this.maxHealth;
    }

    public boolean isActive() {
        return this.active;
    }
}
