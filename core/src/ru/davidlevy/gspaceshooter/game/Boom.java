package ru.davidlevy.gspaceshooter.game;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Класс Boom (анимация "взрыв")
 */
public class Boom {
    private TextureRegion[] frames;
    private Vector2 position;
    private float maxFrames;
    private float time;
    private float timePerFrame;
    private float angle;
    private boolean active;

    /**
     * @param frames массив типа TextureRegion
     */
    public Boom(TextureRegion[] frames) {
        this.frames = frames;
        this.position = new Vector2(0, 0);
        this.maxFrames = frames.length;
        this.timePerFrame = 0.02f;
        this.active = false;
    }

    public void render(SpriteBatch batch) {
        batch.draw(frames[(int) (time / timePerFrame)], position.x - 32, position.y - 32,
                32, 32, 64, 64, 2.5f, 2.5f, angle);
    }

    public void update(float dt) {
        time += dt;
        if (time > maxFrames * timePerFrame) // если > 1,28 сек, то выключить анимацию
            active = false;
    }

    public boolean isActive() {
        return this.active;
    }

    /**
     * Активация
     *
     * @param position положение
     */
    public void activate(Vector2 position) {
        this.position.set(position);
        this.time = 0.0f;
        this.angle = (float) Math.random() * 360.0f;
        this.active = true;
    }
}
