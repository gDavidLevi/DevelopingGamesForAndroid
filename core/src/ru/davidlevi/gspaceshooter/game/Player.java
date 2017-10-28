package ru.davidlevi.gspaceshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.StringBuilder;

/**
 * Класс Player (игрок)
 *
 * @see Ship корабль
 */
public class Player extends Ship {
    private TextureRegion textureRedBandOfHealth;
    private TextureRegion textureGreenBandOfHealth;
    private TextureRegion textureShieldArea;

    /* Текущее количество жизни */
    private int currentLives;

    /* Джойстик */
    private Joystick joystick;

    /* Количество очков */
    private int score;

    /* Звуковой спецэффект "Огонь" */
    private Sound fireSound;

    /* Информация полосы здоровья */
    private StringBuilder infoBandOfHealth;

    /* Счет */
    private int money;

    /* Внутреннее время игрока */
    private float innerTime;

    /* Управление щитом */
    private Circle hitAreaShield;
    private float maxTimeShieldCharge = 2.0f; // секунд
    private boolean canRunShield = false;
    private float scaleShield;
    private float angleShield;
    //private TypePowerUp typePowerUp;

    /**
     * @param gameScreen          экран игры
     * @param texturePlayer       текстура игрока
     * @param textureBandOfHealth текстура полосы здоровья
     * @param textureJoystick     текстура джойстика
     * @param textureFireButton   текстура кнопки огонь
     * @param textureShieldButton текстура кнопки магия
     * @param textureShieldArea   текстура щитового поля
     * @param fireSound           звук огня
     * @param position            позиция
     * @param velocity            скорость
     * @param enginePower         мощность
     */
    public Player(GameScreen gameScreen, TextureRegion texturePlayer, TextureRegion textureBandOfHealth, TextureRegion textureJoystick, TextureRegion textureFireButton, TextureRegion textureShieldButton, TextureRegion textureShieldArea, Sound fireSound, Vector2 position, Vector2 velocity, float enginePower) {
        /* SpaceObject */
        super.maxHealth = 40;
        super.currentHealth = this.maxHealth;
        super.hitArea = new Circle(position, 24);

        /* Ship */
        super.currentFire = 0.0f;
        super.fireRate = 0.1f;
        super.weaponDirection = new Vector2(1.0f, 0.0f);
        super.isPlayer = true;

        /* this */
        this.gameScreen = gameScreen;
        this.texturePlayer = texturePlayer;
        this.textureRedBandOfHealth = new TextureRegion(textureBandOfHealth, 0, 32, 224, 32);
        this.textureGreenBandOfHealth = new TextureRegion(textureBandOfHealth, 0, 0, 224, 32);
        this.textureShieldArea = textureShieldArea;
        this.joystick = new Joystick(this, textureJoystick, textureFireButton, textureShieldButton);
        this.fireSound = fireSound;
        this.position = position;
        this.velocity = velocity;
        this.enginePower = enginePower;
        this.currentLives = 5;
        this.infoBandOfHealth = new StringBuilder(50);
        this.score = 0;
        this.money = 0;
        this.scaleShield = 0.0f;
        this.hitAreaShield = new Circle(position, scaleShield);
        this.innerTime = 0.0f;
    }

    @Override
    public void render(SpriteBatch batch) {
        /* Отрисовка игрока */
        if (damageReaction > 0.01f)
            batch.setColor(1.0f, 1.0f - damageReaction, 1.0f - damageReaction, 1.0f);
        batch.draw(this.texturePlayer, position.x - 32, position.y - 32, 32, 32, 64, 64, 1, 1, velocity.y / 30.0f);
        if (damageReaction > 0.01f) batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);

        /* Отрисовка щита */
        if (this.canRunShield)
            batch.draw(this.textureShieldArea, position.x - 32, position.y - 32, 32, 32, 64, 64,
                    scaleShield, scaleShield, angleShield);
    }

    /**
     * Отрисовка приборной панели
     *
     * @param batch      пачка
     * @param bitmapFont шрифт
     * @param x          позиция
     * @param y          позиция
     */
    public void renderDashboard(SpriteBatch batch, BitmapFont bitmapFont, float x, float y) {
        /* Эффект дрожания полос */
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.draw(textureRedBandOfHealth, x + (int) (Math.random() * damageReaction * 10), y + (int) (Math.random() * damageReaction * 10), textureRedBandOfHealth.getRegionWidth(), 48);
        batch.draw(textureGreenBandOfHealth, x + (int) (Math.random() * damageReaction * 10), y + (int) (Math.random() * damageReaction * 10), (int) ((float) currentHealth / maxHealth * 224), 48);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        batch.setColor(1, 1, 0, damageReaction);
        batch.draw(textureRedBandOfHealth, x + (int) (Math.random() * damageReaction * 25), y + (int) (Math.random() * damageReaction * 25), textureRedBandOfHealth.getRegionWidth(), 48);
        batch.draw(textureGreenBandOfHealth, x + (int) (Math.random() * damageReaction * 25), y + (int) (Math.random() * damageReaction * 25), (int) ((float) currentHealth / maxHealth * 224), 48);
        batch.setColor(1, 1, 1, 1);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        /* Текущие жизни */
        infoBandOfHealth.setLength(0);
        infoBandOfHealth.append("x").append(currentLives);
        bitmapFont.draw(batch, infoBandOfHealth, x + 230, y + 40);

        /* Очки */
        infoBandOfHealth.setLength(0);
        infoBandOfHealth.append("Score: ").append(score);
        bitmapFont.draw(batch, infoBandOfHealth, x + 4, y - 4);

        /* Зарядка щита */
        infoBandOfHealth.setLength(0);
        infoBandOfHealth.append("Shield charge: ").append((int) (innerTime * 100f / this.maxTimeShieldCharge)).append(" %");
        bitmapFont.draw(batch, infoBandOfHealth, x + 4, y - 48);
        //batch.draw(this.textureGreenBandOfHealth, x, y, (innerTime * 100f / this.maxTimeShieldCharge), 16);

        /* Джойстик */
        joystick.render(batch);
    }

    /**
     * Полное восстановление здоровья
     */
    public void fullRepair() {
        this.currentHealth = this.maxHealth;
    }


    @Override
    public void update(float dt) {
        this.innerTime += dt;
        joystick.update(dt);
        position.mulAdd(velocity, dt);
        hitArea.setPosition(position);
        velocity.scl(0.95f);

        /* Увеличить скорость движения игрока в зависимости от положения стика джойстика */
        if (joystick.getPower() > 0.02f) {
            velocity.x += enginePower * dt * joystick.getDirection().x * joystick.getPower();
            velocity.y += enginePower * dt * joystick.getDirection().y * joystick.getPower();
        }

        /* Управление с кравиатуры (WASD + L,M) */
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            velocity.x += enginePower * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            velocity.x -= enginePower * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.y += enginePower * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.y -= enginePower * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.L)) {
            pressFire(dt);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.M)) {
            runShield(/*TypePowerUp.SHIELD*/);
        }

        /* Изменить цвет из-за столкновения */
        damageReaction -= dt * 2.0f;
        if (damageReaction < 0.0f) damageReaction = 0.0f;

        /* Определить границы экрана */
        if (position.x < 32) {
            position.x = 32;
            if (velocity.x < 0) velocity.x = 0;
        }
        if (position.x > SpaceGame.SCREEN_WIDTH - 32) {
            position.x = SpaceGame.SCREEN_WIDTH - 32;
            if (velocity.x > 0) velocity.x = 0;
        }
        if (position.y < 32) {
            position.y = 32;
            if (velocity.y < 0) velocity.y = 0;
        }
        if (position.y > SpaceGame.SCREEN_HEIGHT - 32) {
            position.y = SpaceGame.SCREEN_HEIGHT - 32;
            if (velocity.y > 0) velocity.y = 0;
        }

        /* Если скорость растет по Х, то показать хвост частиц */
        if (velocity.x > 20.0f) {
            float size = velocity.len() / 200.0f;
            gameScreen.getParticleEmitter().setup(position.x - 24, position.y, -MathUtils.random(5.0f, 20.0f), MathUtils.random(-24.0f, 24.0f), 0.5f,
                    size, 0.8f,
                    1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        }

        /* Спросим у джойстика, нажата ли кнопка огонь */
        if (joystick.isActivateFireButton()) pressFire(dt);

        /* Щит */
        if (innerTime >= this.maxTimeShieldCharge) {
            /* Активируем щит от джойстика или после нажатия на клавишу М */
            if (joystick.isActivateShieldButton()) this.canRunShield = true;
            /* ... */
            if (this.canRunShield) {
                /* Поворачиваем щит */
                this.angleShield = (float) Math.random() * 360.0f;
                /* Увеличиваем щит */
                scaleShield += dt * 4.0f;
                if (scaleShield > 8.0f) {
                    /* Дезактивируем щит */
                    joystick.desactivateShieldButton();
                    this.canRunShield = false;
                    this.scaleShield = 0.0f;
                    innerTime = 0.0f;
                    return; // выйти из всех условий!
                }
            }
            innerTime = this.maxTimeShieldCharge; // =5 секунд
        }
        /* Зона контакта щита */
        this.hitAreaShield.set(position, this.scaleShield * 32f);
        /* Расчет щита */
//        scaleShield += dt;
//        for (int i = 0; i < 10; i++)
//            gameScreen.getParticleEmitter().setup(position.x + 32 * (float) Math.cos(scaleShield * 5 + i * 0.628f), position.y + 32 * (float) Math.sin(scaleShield * 5 + i * 0.628f), MathUtils.random(-10, 10), MathUtils.random(-10, 10), 0.2f, 0.8f, 1.1f, 0, 0, 1, 1, 1, 1, 1, 1);
    }

    @Override
    public void onDestroy() {
        currentHealth = maxHealth;
        currentLives--;
        damageReaction = 0.0f;
    }

    @Override
    public void fire() {
        super.fire();
        fireSound.play();
    }

    public Circle getHitAreaShield() {
        return hitAreaShield;
    }

    public void setPosition(Vector2 newPosition) {
        super.position.set(newPosition);
    }

    public void setCurrentHp(int currentHp) {
        super.currentHealth = currentHp;
    }

    public void setCurrentLives(int currentLives) {
        this.currentLives = currentLives;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCurrentLives() {
        return currentLives;
    }

    public int getScore() {
        return this.score;
    }

    public void addScore(int amount) {
        score += amount;
    }

    public void addMoney(int amount) {
        money += amount;
    }

    public void dispose() {
        fireSound.dispose();
    }

    public void runShield(/*TypePowerUp.SHIELD*/) {
        this.canRunShield = true;
        //this.typePowerUp = typePowerUp;
    }
}
