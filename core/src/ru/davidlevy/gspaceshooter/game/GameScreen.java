package ru.davidlevy.gspaceshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс GameScreen (экран игры)
 *
 * @see Screen one of many application screens, such as a main menu, a settings menu, the game screen and so on.
 * @see LevelInfo информация об уровне игры
 */
public class GameScreen implements Screen {
    /* Игровое пространство */
    private SpaceGame spaceGame;
    private SpriteBatch batch;

    private BitmapFont bitmapFont;
    private Background background;
    private Player player;

    /* Эмиттеры */
    private AsteroidEmitter asteroidEmitter;
    private BulletEmitter bulletEmitter;
    private PowerUpsEmitter powerUpsEmitter;
    private ParticleEmitter particleEmitter;
    private BoomEmitter boomEmitter;
    private BotEmitter botEmitter;
    private MagicEmitter magicEmitter;

    /* Уровень игры */
    private List<LevelInfo> levels;
    private int currentLevel;
    private float currentLevelTime;
    private int maxLevels;
    private float timePerLevel;

    /* Кнопки */
    private TextureRegion atlasMenu;
    private Rectangle rectanlgeMenu;

    /* Чтение из фалов */
    private BufferedReader bufferedReader;
    private FileHandle fileStateGame;
    private FileHandle fileLevelData;

    /* Атлас текстур */
    private TextureAtlas atlas;

    /* Процессор ввода */
    private MyInputProcessor mip;

    /* Вектор сонаправленности */
    private final Vector2 norm;

    {
        this.bufferedReader = null;
        this.norm = new Vector2(0, 0);
        this.fileStateGame = Gdx.files.external("settings.save");
        this.fileLevelData = Gdx.files.internal("leveldata.csv");
        this.mip = (MyInputProcessor) Gdx.input.getInputProcessor();
    }

    /**
     * @param spaceGame игровое пространство
     * @param batch     пачка
     */
    public GameScreen(SpaceGame spaceGame, SpriteBatch batch) {
        this.spaceGame = spaceGame;
        this.batch = batch;
    }

    @Override
    public void show() {
        /* Загрузим активы для игры */
        Assets.getInstance().loadAssets(ScreenType.GAME);
        this.atlas = Assets.getInstance().mainAtlas;
        this.bitmapFont = Assets.getInstance().assetManager.get("font3.fnt", BitmapFont.class);

        /* Если было сохранение состояния, то продолжить игру */
        if (this.fileStateGame.exists()) {
            loadDefaultSettings();
            loadSavedSettings();
        } else loadDefaultSettings();

        /* Фоновая музыка */
        Music music = Assets.getInstance().assetManager.get("music.mp3", Music.class);
        music.setLooping(true);
        music.play();

        background = new Background(atlas.findRegion("star16"));

        asteroidEmitter = new AsteroidEmitter(this, atlas.findRegion("asteroid64"), 20, 0.4f);
        botEmitter = new BotEmitter(this, atlas.findRegion("ufo"), 10, 1.0f);
        bulletEmitter = new BulletEmitter(atlas.findRegion("bullets36"), 100);
        powerUpsEmitter = new PowerUpsEmitter(atlas.findRegion("powerUps"));
        particleEmitter = new ParticleEmitter(atlas.findRegion("star16"));
        boomEmitter = new BoomEmitter(atlas.findRegion("explosion64"),
                Assets.getInstance().assetManager.get("CollapseNorm.wav", Sound.class));
        magicEmitter = new MagicEmitter(this, atlas.findRegion("magic"), 3);

        atlasMenu = atlas.findRegion("btMenu");
        rectanlgeMenu = new Rectangle(20, 490, atlasMenu.getRegionWidth(), atlasMenu.getRegionHeight());

        this.currentLevelTime = 0.0f;
        this.timePerLevel = 60.0f;
        loadGameLevelInfo();
    }

    /**
     * Загрузка настроек по умолчанию
     */
    private void loadDefaultSettings() {
        player = new Player(this, atlas.findRegion("ship64"),
                atlas.findRegion("hpBar"),
                atlas.findRegion("joystick"),
                atlas.findRegion("btFire"),
                atlas.findRegion("btMagic"),
                Assets.getInstance().assetManager.get("laser.wav", Sound.class),
                new Vector2(100, 328),
                new Vector2(0.0f, 0.0f),
                800.0f);
        currentLevel = 1;
    }

    /**
     * Загрузка сохраненных настроек
     */
    private void loadSavedSettings() {
        try {
            this.bufferedReader = this.fileStateGame.reader(256);
            String string;
            while ((string = bufferedReader.readLine()) != null) {
                String[] param = string.split("\\t");
                this.player.setPosition(new Vector2(Float.parseFloat(param[0]), Float.parseFloat(param[1])));
                player.setCurrentHp(Integer.parseInt(param[2]));
                player.setCurrentLives(Integer.parseInt(param[3]));
                player.setScore(Integer.parseInt(param[4]));
                this.currentLevel = Integer.parseInt(param[5]);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Загруза из файла данныхиз файла данных информации об уровнях
     */
    private void loadGameLevelInfo() {
        this.levels = new ArrayList<LevelInfo>();
        try {
            this.bufferedReader = this.fileLevelData.reader(8192);
            bufferedReader.readLine(); // пропускаем строку с заголовком
            String string;
            while ((string = bufferedReader.readLine()) != null) {
                String[] param = string.split("\\t");
                LevelInfo levelInfo = new LevelInfo(Integer.parseInt(param[0]),
                        Float.parseFloat(param[1]),
                        Integer.parseInt(param[2]),
                        Integer.parseInt(param[3]),
                        Float.parseFloat(param[4]),
                        Float.parseFloat(param[5]));
                levels.add(levelInfo);
            }
            this.maxLevels = levels.size();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /* Подгонка под экран. Вариант 1. Тоже самое делает viewport. */
//        Matrix4 m4 = new Matrix4();
//        m4.setToTranslationAndScaling(-1f, -1f, 0, 2.0f / 1280.0f, 2.0f / 720.0f, 0.0f);
//        batch.setProjectionMatrix(m4);

        /* Подгонка под экран. Вариант 2 */
        batch.setProjectionMatrix(spaceGame.getCamera().combined);

        batch.begin();
        background.render(batch);
        player.render(batch);
        player.renderDashboard(batch, bitmapFont, 20, 660);
        bitmapFont.draw(batch, "LEVEL " + currentLevel, 570, 690);

        /* Кнопка */
        batch.draw(atlasMenu, rectanlgeMenu.x, rectanlgeMenu.y);

        /* Эмиттеры  */
        asteroidEmitter.render(batch);
        botEmitter.render(batch);
        bulletEmitter.render(batch);
        powerUpsEmitter.render(batch);
        boomEmitter.render(batch);
        particleEmitter.render(batch);
        magicEmitter.render(batch);
        batch.end();
    }

    /**
     * Метод осуществляет переход на другой уровень через timePerLevel секунд
     *
     * @param dt дельта времени
     */
    private void updateLevel(float dt) {
        currentLevelTime += dt;
        if (currentLevelTime > timePerLevel) {
            currentLevelTime = 0.0f;
            currentLevel++;
            if (currentLevel > maxLevels) currentLevel = maxLevels;
            asteroidEmitter.setTimeofAppearance(getCurrentLevelInfo().getAsteroidTimeOfAppearance());
        }
    }

    public void update(float dt) {
        /* Выход в меню по кнопке ESC или нажатия на кнопку rectanlgeMenu */
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) | mip.isTouchedInArea(rectanlgeMenu) != -1)
            spaceGame.setScreen(spaceGame.getMenuScreen());

        updateLevel(dt);

        /* Эмиттеры */
        background.update(dt, player.getVelocity());
        player.update(dt);
        asteroidEmitter.update(dt);
        botEmitter.update(dt);
        bulletEmitter.update(dt);
        powerUpsEmitter.update(dt);
        particleEmitter.update(dt);
        boomEmitter.update(dt);
        magicEmitter.update(dt);

        checkCollision();

        /* Актуализация пула */
        asteroidEmitter.actualizationPool();
        botEmitter.actualizationPool();
        bulletEmitter.actualizationPool();
        particleEmitter.actualizationPool();
        magicEmitter.actualizationPool();
    }


    public void checkCollision() {
        /* Столкновение пуль с игроком */
        int quantityActivesBullets = bulletEmitter.getActiveList().size();
        for (int i = 0; i < quantityActivesBullets; i++) {
            Bullet bullet = bulletEmitter.getActiveList().get(i);
            /* Если это пуля игрока, то... */
            if (bullet.isPlayersBullet()) {
                /*... попала ли она в астероид? */
                int quantityActivesAsteroids = asteroidEmitter.getActiveList().size();
                for (int j = 0; j < quantityActivesAsteroids; j++) {
                    Asteroid asteroid = asteroidEmitter.getActiveList().get(j);
                    /* Если попала, то... */
                    if (asteroid.getHitArea().contains(bullet.getPosition())) {
                        /*... и если астероид уничтожен, то... */
                        if (asteroid.takeDamage(1)) {
                            /*... вознарадить игрока: */
                            player.addScore(asteroid.getMaxHealth() * 10);
                            powerUpsEmitter.makePower(asteroid.getPosition().x, asteroid.getPosition().y);
                            boomEmitter.setup(asteroid.getPosition());
                        }
                        bullet.deactivate();
                        break;
                    }
                }

                /*... попала ли она в бота? */
                int quantityActivesBots = botEmitter.getActiveList().size();
                for (int j = 0; j < quantityActivesBots; j++) {
                    Bot bot = botEmitter.getActiveList().get(j);
                    /* Если попала, то... */
                    if (bot.getHitArea().contains(bullet.getPosition())) {
                        /*... и если бот уничтожен, то... */
                        if (bot.takeDamage(1)) {
                            /*... вознарадить игрока: */
                            player.addScore(bot.getMaxHealth() * 100);
                            powerUpsEmitter.makePower(bot.getPosition().x, bot.getPosition().y);
                            boomEmitter.setup(bot.getPosition());
                        }
                        bullet.deactivate();
                        break;
                    }
                }
            } else {
                /*... НО если попали в игрока, то... */
                if (player.getHitArea().contains(bullet.getPosition())) {
                    /*... вычесть из currentHealth игрока значение 5. */
                    player.takeDamage(5);
                    bullet.deactivate();
                    break;
                }
            }
        }

        /* Столкновение астероида с игроком */
        int quantityActivesAsteroids = asteroidEmitter.getActiveList().size();
        for (int i = 0; i < quantityActivesAsteroids; i++) {
            Asteroid asteroid = asteroidEmitter.getActiveList().get(i);
            if (player.getHitArea().overlaps(asteroid.getHitArea())) {
                /* Расстояние от корабля до астероида */
                float distance = player.getPosition().dst(asteroid.getPosition());
                float interLen = (player.getHitArea().radius + asteroid.getHitArea().radius) - distance;
                /* Нормированный вектор */
                norm.set(asteroid.getPosition()).sub(player.getPosition()).nor();
                /* Новые положения */
                asteroid.getPosition().mulAdd(norm, interLen);
                player.getPosition().mulAdd(norm, -interLen);
                /* Новые скорости */
                asteroid.getVelocity().mulAdd(norm, interLen * 4);
                player.getVelocity().mulAdd(norm, -interLen * 4);
                /* Нанесение урона обоим */
                asteroid.takeDamage(1);
                player.takeDamage(1);
            }
        }

        /* Магия + астероиды */
        int quantityActivesMagic = magicEmitter.getActiveList().size();
        for (int i = 0; i < quantityActivesMagic; i++) {
            Magic magic = magicEmitter.getActiveList().get(i);
            int quantity = asteroidEmitter.getActiveList().size();
            for (int j = 0; j < quantity; j++) {
                Asteroid asteroid = asteroidEmitter.getActiveList().get(j);
                /* Если попал в зану магии, то... */
                if (magic.getHitArea().contains(asteroid.getPosition())) {
                    /*... и если астероид уничтожен, то... */
                    if (asteroid.takeDamage(5)) {
                        /*... вознарадить игрока: */
                        player.addScore(asteroid.getMaxHealth() * 10);
                        boomEmitter.setup(asteroid.getPosition()); // взрыв
                    }
                    magic.deactivate();
                    break;
                }
            }
        }


        /* "Столкновение" PowerUp'ов с игроком */
        int quantityPowerUps = powerUpsEmitter.getPowerUps().length;
        for (int i = 0; i < quantityPowerUps; i++) {
            PowerUp powerUp = powerUpsEmitter.getPowerUps()[i];
            /* Если вознаграждение активно, и... */
            if (powerUp.isActive()) {
                /*... если игрок пересекся с PowerUp'ом, то... */
                if (player.getHitArea().contains(powerUp.getPosition())) {
                    /*... начислить PowerUp'с игроку. */
                    powerUp.accrue(player);
                    powerUp.deactivate();
                }
            }
        }

        /* Как только закончатся жизни у игрока выйти из игры в меню. */
        if (player.getCurrentLives() < 0) spaceGame.setScreen(spaceGame.getMenuScreen());
    }

    public LevelInfo getCurrentLevelInfo() {
        return this.levels.get(currentLevel - 1);
    }

    public Player getPlayer() {
        return this.player;
    }

    public BulletEmitter getBulletEmitter() {
        return this.bulletEmitter;
    }

    public ParticleEmitter getParticleEmitter() {
        return this.particleEmitter;
    }

    public MagicEmitter getMagicEmitter() {
        return this.magicEmitter;
    }

    public int getCurrentLevel() {
        return this.currentLevel;
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
