package ru.davidlevy.gspaceshooter.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Класс ShieldEmitter
 *
 * @see ObjectPool пул объектов
 */
public class ShieldEmitter extends ObjectPool<Shield> {
    private GameScreen gameScreen;
    private TextureRegion textureShield;

    @Override
    protected Shield newObject() {
        return new Shield(gameScreen, textureShield);
    }

    public ShieldEmitter(GameScreen gameScreen, TextureRegion textureShield, int poolSize) {
        super();
        this.gameScreen = gameScreen;
        this.textureShield = textureShield;

        /* Создание объектов */
        for (int i = 0; i < poolSize; i++)
            inactiveList.add(newObject());
    }

    public void render(SpriteBatch batch) {
        int quantity = activeList.size();
        for (int i = 0; i < quantity; i++)
            activeList.get(i).render(batch);
    }

    public void update(float dt) {
        /* Пройтись по всему пулу */
        int quantity = activeList.size();
        for (int i = 0; i < quantity; i++)
            activeList.get(i).update(dt);
    }

    /**
     * Установка щита вокруг игрока
     */
    public void setup() {
        Shield shield1 = getActiveElement();
        shield1.activate();
    }

}
