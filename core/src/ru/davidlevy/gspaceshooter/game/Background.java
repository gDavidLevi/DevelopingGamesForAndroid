package ru.davidlevy.gspaceshooter.game;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Класс Background описывает звездный фон игры
 */
public class Background {
    /* Количество звезд на фоне */
    private static final int NUMBERS_OF_STARS = 250;

    /* Класс Star (звезда) */
    private class Star {
        private Vector2 position;
        private float speed;

        Star() {
            this.position = new Vector2((float) Math.random() * SpaceGame.SCREEN_WIDTH,
                    (float) Math.random() * SpaceGame.SCREEN_HEIGHT);
            this.speed = MathUtils.random(20.0f, 100.0f);
        }

        /**
         * Обновление
         *
         * @param dt           дельта времени
         * @param acceleration ускорение
         */
        public void update(float dt, Vector2 acceleration) {
            position.x -= speed * dt;
            position.mulAdd(acceleration, -dt * 0.1f);
            if (position.y < -20) {
                position.y = SpaceGame.SCREEN_HEIGHT;
            }
            if (position.y > SpaceGame.SCREEN_HEIGHT) {
                position.y = -20;
            }
            if (position.x < -40) {
                position.x = SpaceGame.SCREEN_WIDTH;
                speed = MathUtils.random(20.0f, 100.0f);
            }
        }
    }
    /* -=================- */

    /* Текстуры */
    private Texture textureBackground;
    private TextureRegion textureStar;
    /* Массив звезд */
    private Star[] stars;

    /**
     * @param textureStar текстура звезды
     */
    public Background(TextureRegion textureStar) {
        textureBackground = new Texture("background.png");
        this.textureStar = textureStar;
        stars = new Star[NUMBERS_OF_STARS];
        for (int i = 0; i < NUMBERS_OF_STARS; i++)
            stars[i] = new Star();
    }

    public void render(SpriteBatch batch) {
        batch.draw(textureBackground, 0, 0);
        int quantity = stars.length;
        for (int i = 0; i < quantity; i++) {
            float scale = stars[i].speed / 200.0f + 0.1f;
            if (Math.random() < 0.02) scale *= 1.5f;
            batch.draw(textureStar, stars[i].position.x, stars[i].position.y, 8, 8, 16, 16, scale, scale, 0);
        }
    }

    /**
     * Обновление
     *
     * @param dt             дельта времени
     * @param playerVelocity скрость игрока
     */
    public void update(float dt, Vector2 playerVelocity) {
        int quantity = stars.length;
        for (int i = 0; i < quantity; i++)
            stars[i].update(dt, playerVelocity);
    }

    // public void dispose() {
    // textureBackground.dispose();
    // }
}
