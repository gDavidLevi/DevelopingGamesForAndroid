package ru.davidlevy.gspaceshooter.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

/**
 * Класс SHIELD (щит)
 */
public class Shield implements Poolable {
    private GameScreen gameScreen;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private float angle;
    private float scale;
    private Circle hitArea;
    private boolean active;

    /**
     * @param gameScreen экран игры
     * @param texture    текстура
     */
    public Shield(GameScreen gameScreen, TextureRegion texture) {
        this.gameScreen = gameScreen;
        this.texture = texture;
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.scale = 0.0f;
        this.hitArea = new Circle(position, scale);
        this.active = false;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32, 64, 64,
                scale, scale, angle);
    }

    public void update(float dt) {
        scale += dt * 4.0f;
        if (scale > 8.0f) {
            scale = 0.0f;
            deactivate();
        }

        position.set(gameScreen.getPlayer().position);
        hitArea.set(position, scale * 40f);
        position.mulAdd(velocity, dt);
    }

    public boolean isActive() {
        return this.active;
    }

    /**
     * Активация
     */
    public void activate() {
        this.scale = 0.0f;
        this.angle = (float) Math.random() * 360.0f;
        this.active = true;
    }

    private void deactivate() {
        this.active = false;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public Circle getHitArea() {
        return this.hitArea;
    }
}
