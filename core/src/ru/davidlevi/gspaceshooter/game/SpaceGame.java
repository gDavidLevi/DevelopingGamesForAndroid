package ru.davidlevi.gspaceshooter.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Класс SpaceGame (игровое пространство)
 */
public class SpaceGame extends Game {
    /* Размеры экрана */
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;

    private SpriteBatch batch;

    /* Экраны */
    private GameScreen gameScreen;
    private MenuScreen menuScreen;

    /* Обзасть обзора */
    private Viewport viewport;

    /* Камера */
    private Camera camera;

    /* Процессор ввода */
    private MyInputProcessor mip;

    {
        mip = new MyInputProcessor(this);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);

        viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);
        viewport.update(SCREEN_WIDTH, SCREEN_HEIGHT, true);
        viewport.apply();

        /* Зарегистрировать в системе наш процессор ввода */
        Gdx.input.setInputProcessor(mip);

        menuScreen = new MenuScreen(this, batch);
        gameScreen = new GameScreen(this, batch);

        /* Установить начальный экран */
        setScreen(menuScreen);
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        getScreen().render(dt);
    }

    public Camera getCamera() {
        return camera;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public MenuScreen getMenuScreen() {
        return menuScreen;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        viewport.apply();
        camera.update();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}