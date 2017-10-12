package ru.davidlevy.gspaceshooter.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Класс MagicEmitter
 *
 * @see ObjectPool пул объектов
 */
public class MagicEmitter extends ObjectPool<Magic> {
    private GameScreen gameScreen;
    private TextureRegion magicTexture;

    @Override
    protected Magic newObject() {
        return new Magic(gameScreen, magicTexture);
    }

    public MagicEmitter(GameScreen gameScreen, TextureRegion magicTexture, int poolSize) {
        super();
        this.gameScreen = gameScreen;
        this.magicTexture = magicTexture;

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
     * Установка
     */
    public void setup() {
        Magic magic = getActiveElement();
        /* Активация магии вокруг игрока */
        magic.activate();
    }

}
