package ru.davidlevy.gspaceshooter.game;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс BotEmitter
 *
 * @see ObjectPool пул объектов
 */
public class BotEmitter extends ObjectPool<Bot> {
    private GameScreen gameScreen;
    private TextureRegion botTexture;
    private float timeOfAppearance;
    private float innerTimer;

    /* Маршруты */
    private List<Route> routes;

    @Override
    protected Bot newObject() {
        return new Bot(gameScreen, botTexture);
    }

    /**
     * @param gameScreen       экран игры
     * @param botTexture       текстура бота
     * @param poolSize         размер пула
     * @param timeOfAppearance время (сек.), через которое появится новый бот
     */
    public BotEmitter(GameScreen gameScreen, TextureRegion botTexture, int poolSize, float timeOfAppearance) {
        super();
        this.gameScreen = gameScreen;
        this.botTexture = botTexture;
        this.timeOfAppearance = timeOfAppearance;
        this.innerTimer = 0.0f;
        /* Создание объектов */
        for (int i = 0; i < poolSize; i++)
            inactiveList.add(newObject());

        initRoutes();
    }

    /**
     * Инициализация и добавление маршрутов
     */
    private void initRoutes() {
        this.routes = new ArrayList<Route>();
        Route r1 = new Route(new Vector2(1400, 640));
        r1.addPoint(1500, new Vector2(-320.0f, 0.0f)).addPoint(600, new Vector2(-320.0f, -120.0f));
        routes.add(r1);
        Route r2 = new Route(new Vector2(1400, 80));
        r2.addPoint(1500, new Vector2(-320.0f, 0.0f)).addPoint(600, new Vector2(-320.0f, 120.0f));
        routes.add(r2);
        Route r3 = new Route(new Vector2(1400, 400));
        r3.addPoint(1500, new Vector2(-400.0f, 0.0f)).addPoint(600, new Vector2(-400.0f, 180.0f));
        routes.add(r3);
        Route r4 = new Route(new Vector2(1400, 320));
        r4.addPoint(1500, new Vector2(-400.0f, 0.0f)).addPoint(600, new Vector2(-400.0f, 180.0f));
        routes.add(r4);
    }

    public void render(SpriteBatch batch) {
        int quantity = activeList.size();
        for (int i = 0; i < quantity; i++)
            activeList.get(i).render(batch);
    }

    public void update(float dt) {
        innerTimer += dt;
        /* Установка бота спустя timeOfAppearance  */
        if (innerTimer > timeOfAppearance) {
            innerTimer -= timeOfAppearance;
            setup();
        }
        /* Пройтись по всему пулу */
        int quantity = activeList.size();
        for (int i = 0; i < quantity; i++)
            activeList.get(i).update(dt);
    }

    /**
     * Установка
     */
    private void setup() {
        Bot bot = getActiveElement();
        /* Активация бота на случайном маршруте */
        bot.activate(routes.get((int) (Math.random() * routes.size())));
    }
}
