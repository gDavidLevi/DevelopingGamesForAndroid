package ru.davidlevy.gspaceshooter.game;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

/**
 * Класс AsteroidEmitter
 *
 * @see ObjectPool пул объектов
 */
public class AsteroidEmitter extends ObjectPool<Asteroid> {
    private GameScreen gameScreen;
    private TextureRegion asteroidTexture;
    private float timeofAppearance;
    /* Внутренний таймер */
    private float innerTimer;

    /**
     * @param gameScreen       экран игры
     * @param asteroidTexture  текстура
     * @param poolSize         размер пула
     * @param timeofAppearance время (сек.), через которое появится новый астероид
     */
    public AsteroidEmitter(GameScreen gameScreen, TextureRegion asteroidTexture, int poolSize, float timeofAppearance) {
        super();
        this.gameScreen = gameScreen;
        this.asteroidTexture = asteroidTexture;
        this.timeofAppearance = timeofAppearance;
        this.innerTimer = 0.0f;
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
        innerTimer += dt;
        /* Установка астероида спустя timeofAppearance  */
        if (innerTimer > timeofAppearance) {
            innerTimer -= timeofAppearance;
            setup();
        }
        /* Пройтись по всему пулу */
        int quantity = activeList.size();
        for (int i = 0; i < quantity; i++)
            activeList.get(i).update(dt);
    }

    /**
     * Встунавливает время появления
     *
     * @param timeofAppearance время через (секунд)
     */
    public void setTimeofAppearance(float timeofAppearance) {
        this.timeofAppearance = timeofAppearance;
    }

    /**
     * Создание нового объекта типа Asteroid
     *
     * @return Asteroid
     * @see ObjectPool пул объектов
     */
    @Override
    protected Asteroid newObject() {
        return new Asteroid(asteroidTexture);
    }

    /**
     * Установка
     */
    public void setup() {
        Asteroid asteroid = getActiveElement();
        LevelInfo levelInfo = gameScreen.getCurrentLevelInfo();

        float x = MathUtils.random(1400.0f, 2200.0f);
        float y = MathUtils.random(0, 720.0f);
        float vx = -MathUtils.random(levelInfo.getAsteroidSpeedMin(), levelInfo.getAsteroidSpeedMax());
        float vy = 0;

        int maxHealth = MathUtils.random(levelInfo.getAsteroidHealthMin(), levelInfo.getAsteroidHealthMax());

        /* Масштаб астероида в завивимости от его здоровья */
        int delta = levelInfo.getAsteroidHealthMax() - levelInfo.getAsteroidHealthMin();
        int delta2 = maxHealth - levelInfo.getAsteroidHealthMin();
        float scale = 0.5f + 1.5f * ((float) delta2 / (float) delta);

        asteroid.activate(x, y, vx, vy, maxHealth, scale);
    }
}
