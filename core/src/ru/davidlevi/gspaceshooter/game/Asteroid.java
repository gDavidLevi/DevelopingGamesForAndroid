package ru.davidlevi.gspaceshooter.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Класс Asteroid
 *
 * @see SpaceObject костический объект
 * @see Poolable помещается в пул объектов
 */
public class Asteroid extends SpaceObject implements Poolable {
    /* Масштаб */
    private float scale;
    /* Угол поворота */
    private float angle;
    /* Угловая скорость */
    private float angularSpeed;

    /**
     * @param texture TextureRegion
     */
    public Asteroid(TextureRegion texture) {
        /* SpaceObject */
        super.texturePlayer = texture;
        super.position = new Vector2(0, 0);
        super.velocity = new Vector2(0, 0);
        super.maxHealth = 0;
        super.currentHealth = 0;
        super.hitArea = new Circle(position.x, position.y, 28 * scale);
        super.damageReaction = 0.0f;
        super.active = false;

        /* this */
        this.scale = 0;
        this.angle = 0;
        this.angularSpeed = 0;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (damageReaction > 0.01f)
            batch.setColor(1.0f, 1.0f - damageReaction, 1.0f - damageReaction, 1.0f);

        batch.draw(texturePlayer, position.x - 32, position.y - 32, 32, 32, 64, 64, scale, scale, angle);

        if (damageReaction > 0.01f) batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void update(float dt) {
        /* Изменить цвет из-за столкновения */
        damageReaction -= dt * 2.0f;
        if (damageReaction < 0.0f) damageReaction = 0.0f;

        position.mulAdd(velocity, dt);
        angle += angularSpeed * dt;
        if (position.x < -100.0f) deactivate();
        hitArea.setPosition(position);
    }

    @Override
    public void onDestroy() {
        deactivate();
    }

    /**
     * Активация астероида
     *
     * @param x         положение
     * @param y         положение
     * @param vx        скорость
     * @param vy        скорость
     * @param maxHealth максимум здоровя
     * @param scale     масштаб
     */
    public void activate(float x, float y, float vx, float vy, int maxHealth, float scale) {
        this.position.set(x, y);
        this.velocity.set(vx, vy);
        this.angle = MathUtils.random(0.0f, 360.0f);
        this.maxHealth = maxHealth;
        this.currentHealth = this.maxHealth;
        this.active = true;
        this.scale = scale;
        this.hitArea.radius = 28.0f * this.scale;
        this.damageReaction = 0.0f;
    }
}
