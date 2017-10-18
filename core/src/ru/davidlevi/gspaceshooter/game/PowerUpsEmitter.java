package ru.davidlevi.gspaceshooter.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Класс PowerUpsEmitter (эмиттер плюшек)
 */
public class PowerUpsEmitter {
    /* Количество плюшек */
    private static final int NUMBERS_OF_POWERUPS = 50;

    /* Массив  */
    private PowerUp[] powerUps;
    private TextureRegion[][] textureRegion;

    public PowerUpsEmitter(TextureRegion textureRegion) {
        this.textureRegion = textureRegion.split(32, 32);

        this.powerUps = new PowerUp[NUMBERS_OF_POWERUPS];
        for (int i = 0; i < NUMBERS_OF_POWERUPS; i++)
            powerUps[i] = new PowerUp();
    }

    public PowerUp[] getPowerUps() {
        return powerUps;
    }

    public void render(SpriteBatch batch) {
        int index;
        for (int i = 0; i < NUMBERS_OF_POWERUPS; i++) {
            if (powerUps[i].isActive()) {
                index = powerUps[i].getType().getNumber();
                batch.draw(textureRegion[0][index], powerUps[i].getPosition().x - 16, powerUps[i].getPosition().y - 16);
            }
        }
    }

    public void update(float dt) {
        for (int i = 0; i < NUMBERS_OF_POWERUPS; i++)
            if (powerUps[i].isActive()) powerUps[i].update(dt);
    }

    public void makePower(float x, float y) {
        /* Вероятность появления плющек */
        if (Math.random() < 0.9) {
            for (int i = 0; i < NUMBERS_OF_POWERUPS; i++) {
                /* Если плюшка неактивна, то... */
                if (!powerUps[i].isActive()) {
                    /*... создаем случайную плюшку; */
                    TypePowerUp type = TypePowerUp.values()[(int) (Math.random() * 5)];
                    /*... и активируем её. */
                    powerUps[i].activate(x, y, type);
                    break;
                }
            }
        }
    }
    /**/
}
