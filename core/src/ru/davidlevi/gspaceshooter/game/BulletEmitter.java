package ru.davidlevi.gspaceshooter.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Класс BulletEmitter
 *
 * @see ObjectPool пул объектов
 */
public class BulletEmitter extends ObjectPool<Bullet> {
    /* Массив текстур пуль */
    private TextureRegion[] bulletsTexture;

    /**
     * @param bulletsTexture текстура пули
     * @param poolSize       размер пула
     */
    public BulletEmitter(TextureRegion bulletsTexture, int poolSize) {
        super(poolSize);

        /* 0 - пуля игрока, 1 - пуля бота */
        this.bulletsTexture = new TextureRegion[2];
        for (int i = 0; i < 2; i++)
            this.bulletsTexture[i] = new TextureRegion(bulletsTexture, i * 36, 0, 36, 36);
    }

    public void render(SpriteBatch batch) {
        TextureRegion textureOfBullet;
        int quantity = activeList.size();
        for (int i = 0; i < quantity; i++) {
            textureOfBullet = bulletsTexture[activeList.get(i).getBulletType().getIndex()];
            batch.draw(textureOfBullet,
                    activeList.get(i).getPosition().x - 24, activeList.get(i).getPosition().y - 24,
                    24, 24, 48, 48, 0.7f, 0.7f, 0.0f);
        }
    }

    public void update(float dt) {
        int quantity = activeList.size();
        for (int i = 0; i < quantity; i++)
            activeList.get(i).update(dt);
    }

    @Override
    protected Bullet newObject() {
        return new Bullet();
    }

    /**
     * Установка
     *
     * @param bulletType      тип пули
     * @param isPlayersBullet пеля игрока?
     * @param x               положение
     * @param y               положение
     * @param vx              скорость
     * @param vy              скорость
     */
    public void setup(BulletType bulletType, boolean isPlayersBullet, float x, float y, float vx, float vy) {
        Bullet b = getActiveElement();
        b.activate(bulletType, isPlayersBullet, x, y, vx, vy);
    }
}
