package ru.davidlevy.gspaceshooter.game;

/**
 * Перечисление BulletType (типы пуль)
 */
public enum BulletType {
    FIREBALL(0), GREENRAY(1);

    private int index;

    public int getIndex() {
        return this.index;
    }

    BulletType(int index) {
        this.index = index;
    }
}
