package ru.davidlevy.gspaceshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.StringBuilder;

/**
 * Класс MenuScreen (экран меню)
 *
 * @see Screen one of many application screens, such as a main menu, a settings menu, the game screen and so on.
 */
public class MenuScreen implements Screen {
    private SpaceGame spaceGame;
    private SpriteBatch batch;
    private Background background;
    private Vector2 emptyVelocity;
    private TextureAtlas atlas;
    private TextureRegion atlasInfo;

    /* Кнопки */
    private TextureRegion atlasContinue;
    private TextureRegion atlasExit;
    private TextureRegion atlasSaveStateGame;
    private TextureRegion atlasNewState;
    private Rectangle rectanlgeSaveStateGame;
    private Rectangle rectanlgeContinue;
    private Rectangle rectanlgeExit;
    private Rectangle rectanlgeNewState;

    private FileHandle fileStateGame;

    /* Процессор ввода */
    private MyInputProcessor mip;

    {
        this.emptyVelocity = new Vector2(0, 0);
        this.fileStateGame = Gdx.files.external("settings.save");
        this.mip = (MyInputProcessor) Gdx.input.getInputProcessor();
    }

    public MenuScreen(SpaceGame spaceGame, SpriteBatch batch) {
        this.spaceGame = spaceGame;
        this.batch = batch;
    }

    @Override
    public void show() {
        /* Загрузим активы для меню */
        Assets.getInstance().loadAssets(ScreenType.MENU);
        atlas = Assets.getInstance().mainAtlas;

        atlasInfo = atlas.findRegion("help");
        background = new Background(atlas.findRegion("star16"));

        /* Кнопки */
        atlasContinue = atlas.findRegion("btContinue");
        atlasExit = atlas.findRegion("btExit");
        atlasSaveStateGame = atlas.findRegion("btSaveStateGame");
        atlasNewState = atlas.findRegion("btNewStateGame");
        rectanlgeContinue = new Rectangle(170, 100, atlasContinue.getRegionWidth(), atlasContinue.getRegionHeight());
        rectanlgeExit = new Rectangle(800, 100, atlasExit.getRegionWidth(), atlasExit.getRegionHeight());
        rectanlgeSaveStateGame = new Rectangle(800, 400, atlasSaveStateGame.getRegionWidth(), atlasSaveStateGame.getRegionHeight());
        rectanlgeNewState = new Rectangle(170, 400, atlasNewState.getRegionWidth(), atlasNewState.getRegionHeight());
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /* Подгонка под экран */
        batch.setProjectionMatrix(spaceGame.getCamera().combined);

        batch.begin();
        background.render(batch);
        batch.draw(atlasInfo, 500, 280);
        batch.draw(atlasContinue, rectanlgeContinue.x, rectanlgeContinue.y);
        batch.draw(atlasExit, rectanlgeExit.x, rectanlgeExit.y);
        batch.draw(atlasSaveStateGame, rectanlgeSaveStateGame.x, rectanlgeSaveStateGame.y);
        batch.draw(atlasNewState, rectanlgeNewState.x, rectanlgeNewState.y);
        batch.end();
    }

    private void update(float dt) {
        background.update(dt, emptyVelocity);

        /* Играть, продолжить игру */
        if (mip.isTouchedInArea(rectanlgeContinue) != -1)
            spaceGame.setScreen(spaceGame.getGameScreen());

        /* Выход */
        if (mip.isTouchedInArea(rectanlgeExit) != -1) {
            Gdx.app.exit();
            System.exit(0);
        }

        /* Сохранить состояние */
        if (mip.isTouchedInArea(rectanlgeSaveStateGame) != -1) saveStateGame();

        /* Сбросить сохраненное состояние */
        if (mip.isTouchedInArea(rectanlgeNewState) != -1) newStateGame();
    }

    /**
     * Сброс состояние игры
     */
    private void newStateGame() {
        fileStateGame.delete();
    }

    /**
     * Сохранение состояния игры
     */
    private void saveStateGame() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(spaceGame.getGameScreen().getPlayer().position.x).append("\t")
                .append(spaceGame.getGameScreen().getPlayer().position.y).append("\t")
                .append(spaceGame.getGameScreen().getPlayer().currentHealth).append("\t")
                .append(spaceGame.getGameScreen().getPlayer().getCurrentLives()).append("\t")
                .append(spaceGame.getGameScreen().getPlayer().getScore()).append("\t")
                .append(spaceGame.getGameScreen().getCurrentLevel());
        fileStateGame.writeString(stringBuilder.toString(), false); // false - не дописывать файл
    }

    @Override
    public void resize(int width, int height) {
        spaceGame.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Assets.getInstance().clear();
    }

    @Override
    public void dispose() {
        Assets.getInstance().clear();
    }
}
