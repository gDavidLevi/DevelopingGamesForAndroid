package ru.davidlevy.gspaceshooter.game;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Класс Bot (бот)
 *
 * @see Ship корабль
 * @see Poolable помещается в пул объектов
 */
public class Bot extends Ship implements Poolable {
    /* Поля для анимации */
    private int maxFrames;
    private TextureRegion[] frame;
    private float time;
    private float timePerFrame;

    /* Маршрут */
    private Route route;

    /**
     * @param gameScreen экран игры
     * @param textureBot текстура бота
     */
    public Bot(GameScreen gameScreen, TextureRegion textureBot) {
        /* SpaceObject */
        super.gameScreen = gameScreen;
        super.texturePlayer = textureBot;
        super.position = new Vector2(0, 0);
        super.velocity = new Vector2(0, 0);
        super.maxHealth = 0;
        super.currentHealth = this.maxHealth;
        super.hitArea = new Circle(position, 28);

        /* Ship */
        super.enginePower = 0;
        super.currentFire = 0.0f;
        super.fireRate = 0;
        super.weaponDirection = new Vector2(-1.0f, 0.0f);
        super.isPlayer = false;

        /* this */
        this.maxFrames = 4;
        this.timePerFrame = 0.1f; // 1/10 секунды
        this.time = MathUtils.random(0.0f, maxFrames * timePerFrame);
        this.frame = new TextureRegion[maxFrames];
        for (int i = 0; i < maxFrames; i++)
            frame[i] = new TextureRegion(textureBot, i * 64, 0, 64, 64);
    }

    @Override
    public void render(SpriteBatch batch) {
        if (damageReaction > 0.01f)
            batch.setColor(1.0f, 1.0f - damageReaction, 1.0f - damageReaction, 1.0f);

        /* Анимация бота */
        int k = (int) (this.time / this.timePerFrame);
        if (k > this.maxFrames - 1) k = maxFrames - 1;
        batch.draw(frame[k], position.x - 32, position.y - 32);

        if (damageReaction > 0.01f) batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void update(float dt) {
        this.time += dt;
        if (time > maxFrames * timePerFrame) time = 0.0f;

        /* Бот стреляет */
        pressFire(dt);

        damageReaction -= dt * 2.0f;
        if (damageReaction < 0.0f) damageReaction = 0.0f;

        if (position.x < -100) deactivate();

        /* Изменить направление в зависимости от текущего значения Х */
        velocity.set(route.getMyVelocity((int) position.x));

        position.mulAdd(velocity, dt);
        hitArea.setPosition(position);
        velocity.scl(0.95f);
    }

    @Override
    public void onDestroy() {
        deactivate();
    }

    public void activate(Route route) {
        position.set(route.getInitialPosition());
        fireRate = 1.0f;
        hitArea.setRadius(28);
        maxHealth = 10;
        currentHealth = maxHealth;
        active = true;
        this.route = route;
    }
}
