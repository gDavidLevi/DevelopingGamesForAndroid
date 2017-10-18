package ru.davidlevi.gspaceshooter.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Класс-синглтон Assets (активы)
 * Загружает и хранит именно те ресурсы, которые нужны на соответствующем экране игры,
 * то есть по ситуации в switch.
 */
public class Assets {
    private static final Assets ourInstance = new Assets();

    /* Публичные поля */
    AssetManager assetManager;
    TextureAtlas mainAtlas;

    private Assets() {
        assetManager = new AssetManager();
    }

    /**
     * Возвращает только один экземпляр класса
     *
     * @return AssetManager
     */
    public static Assets getInstance() {
        return ourInstance;
    }

    /**
     * Загрузка активов для типа экрана
     * Типы экранов: MENU, GAME
     *
     * @param screenType тип экрана
     */
    public void loadAssets(ScreenType screenType) {
        switch (screenType) {
            case MENU:
                assetManager.load("assets.pack", TextureAtlas.class);
                assetManager.load("background.png", Texture.class);
                assetManager.finishLoading();
                mainAtlas = assetManager.get("assets.pack", TextureAtlas.class);
                break;
            case GAME:
                assetManager.load("font3.fnt", BitmapFont.class);
                assetManager.load("assets.pack", TextureAtlas.class);
                assetManager.load("background.png", Texture.class);
                assetManager.load("music.mp3", Music.class);
                assetManager.load("laser.wav", Sound.class);
                assetManager.load("CollapseNorm.wav", Sound.class);
                assetManager.finishLoading();
                mainAtlas = assetManager.get("assets.pack", TextureAtlas.class);
                //assetManager.get("assets.pack", TextureAtlas.class).dispose();
                break;
        }
    }

    /**
     * Освобождение всех активов
     */
    public void clear() {
        assetManager.clear();
    }
}
