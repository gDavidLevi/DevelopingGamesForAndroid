package ru.davidlevy.gspaceshooter.game;

/**
 * Класс LevelInfo.
 * Хранит информацию об уровне игры
 *
 * @see GameScreen экран игры
 */
public class LevelInfo {
    private int level;
    private float asteroidTimeOfAppearance;
    private int asteroidHealthMin;
    private int asteroidHealthMax;
    private float asteroidSpeedMin;
    private float asteroidSpeedMax;

    /**
     * @param level                    номер уровня
     * @param asteroidTimeOfAppearance время (сек.), через которое появится новый астероид
     * @param asteroidHealthMin        минимальное здоровье
     * @param asteroidHealthMax        максимальное здоровье
     * @param asteroidSpeedMin         минимальная скорость
     * @param asteroidSpeedMax         максимальная скорость
     */
    public LevelInfo(int level, float asteroidTimeOfAppearance, int asteroidHealthMin,
                     int asteroidHealthMax, float asteroidSpeedMin, float asteroidSpeedMax) {
        this.level = level;
        this.asteroidTimeOfAppearance = asteroidTimeOfAppearance;
        this.asteroidHealthMin = asteroidHealthMin;
        this.asteroidHealthMax = asteroidHealthMax;
        this.asteroidSpeedMin = asteroidSpeedMin;
        this.asteroidSpeedMax = asteroidSpeedMax;
    }

    public float getAsteroidTimeOfAppearance() {
        return this.asteroidTimeOfAppearance;
    }

    public int getAsteroidHealthMin() {
        return this.asteroidHealthMin;
    }

    public int getAsteroidHealthMax() {
        return this.asteroidHealthMax;
    }

    public float getAsteroidSpeedMin() {
        return this.asteroidSpeedMin;
    }

    public float getAsteroidSpeedMax() {
        return this.asteroidSpeedMax;
    }

    public int getLevel() {
        return this.level;
    }
}
