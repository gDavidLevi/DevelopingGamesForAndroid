package ru.davidlevy.gspaceshooter.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Класс Ship (корабль)
 *
 * @see SpaceObject космический объект
 */
public abstract class Ship extends SpaceObject {
    /* Мощность двигателя*/
    protected float enginePower;

    /* Текущий огонь */
    protected float currentFire;

    /* Cкорострельность */
    protected float fireRate;

    /* Направление оружия */
    protected Vector2 weaponDirection;

    /* Это игрок? */
    protected boolean isPlayer;

    /**
     * Метод вызывает огонь
     *
     * @param dt дельта времени
     */
    public void pressFire(float dt) {
        currentFire += dt;
        if (currentFire > fireRate) {
            currentFire -= fireRate;
            fire();
        }
    }

    /**
     * Огонь!
     */
    public void fire() {
        /* Выбор оружия в зависимости от isPlayer */
        BulletType bulletType = BulletType.FIREBALL;
        if (!isPlayer) bulletType = BulletType.GREENRAY;
        /* Запустить эмиттер пуль */
        gameScreen.getBulletEmitter().setup(bulletType, isPlayer, position.x + 24.0f, position.y + 0.0f, weaponDirection.x * 640, weaponDirection.y * 640);
    }
}
