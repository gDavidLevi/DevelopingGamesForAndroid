package ru.davidlevi.gspaceshooter.game;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Класс BoomEmitter
 */
public class BoomEmitter {
    /* Количество бумов */
    private static final int NUMBERS_OF_BOOMS = 50;

    private Boom[] booms;
    private Sound boomSound;

    /**
     * @param texture   TextureRegion
     * @param boomSound Sound
     */
    public BoomEmitter(TextureRegion texture, Sound boomSound) {
        TextureRegion[] frames = createArrayOfFramesFromMatrixRegions(texture.split(64, 64));
        this.booms = new Boom[NUMBERS_OF_BOOMS];
        for (int i = 0; i < NUMBERS_OF_BOOMS; i++)
            booms[i] = new Boom(frames);
        this.boomSound = boomSound;
    }

    /**
     * Преобразуем текстуру из квадрата в ленту кадров
     *
     * @param textureRegions TextureRegion[][]
     * @return TextureRegion[]
     */
    private TextureRegion[] createArrayOfFramesFromMatrixRegions(TextureRegion[][] textureRegions) {
        TextureRegion[] frames = new TextureRegion[textureRegions[0].length * textureRegions.length];
        for (int i = 0, n = 0; i < textureRegions.length; i++)
            for (int j = 0; j < textureRegions[0].length; j++, n++)
                frames[n] = textureRegions[i][j];
        return frames;
    }

    public void update(float dt) {
        for (int i = 0; i < NUMBERS_OF_BOOMS; i++)
            if (booms[i].isActive()) booms[i].update(dt);
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < NUMBERS_OF_BOOMS; i++)
            if (booms[i].isActive()) booms[i].render(batch);
    }

    /**
     * Установка
     *
     * @param position позиция
     */
    public void setup(Vector2 position) {
        for (int i = 0; i < NUMBERS_OF_BOOMS; i++) {
            if (!booms[i].isActive()) {
                booms[i].activate(position);
                boomSound.play();
                break;
            }
        }
    }

    public void dispose() {
        boomSound.dispose();
    }
}
