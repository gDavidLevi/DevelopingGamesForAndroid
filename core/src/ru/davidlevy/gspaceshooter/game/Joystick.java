package ru.davidlevy.gspaceshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Класс Joystick
 */
public class Joystick {
    private Player player;
    private TextureRegion textureBase;
    private TextureRegion textureStick;
    private TextureRegion textureFire;
    private TextureRegion textureMagic;

    /* Кнопка джойстика */
    private Rectangle rectangleJoystick;

    /* Кнопки */
    private Rectangle rectangleFire;
    private Rectangle rectangleMagic;

    /* Центр джойстика */
    private float joyCenterX;
    private float joyCenterY;

    /* Тач ID */
    private int touchId;

    /* Смещение textureStick относительно центра джойстика */
    private Vector2 offset;

    /* Направление textureStick */
    private Vector2 norm;

    /* Процессор ввода */
    private MyInputProcessor mip;

    {
        this.mip = (MyInputProcessor) Gdx.input.getInputProcessor();
    }

    /**
     * @param player          игрок
     * @param textureJoystick текстура джойстика
     * @param textureFire     текстура кнопки ОГОНЬ
     */
    public Joystick(Player player, TextureRegion textureJoystick, TextureRegion textureFire, TextureRegion textureMagic) {
        this.player = player;

        this.textureBase = new TextureRegion(textureJoystick, 0, 0, 200, 200);
        this.textureStick = new TextureRegion(textureJoystick, 0, 200, 50, 50);

        this.rectangleJoystick = new Rectangle(50, 50, 200, 200);

        this.textureFire = textureFire;
        this.rectangleFire = new Rectangle(1050, 70, textureFire.getRegionHeight(), textureFire.getRegionWidth());

        this.textureMagic = textureMagic;
        this.rectangleMagic = new Rectangle(550, 80, textureMagic.getRegionHeight(), textureMagic.getRegionWidth());

        this.joyCenterX = rectangleJoystick.x + rectangleJoystick.width / 2;
        this.joyCenterY = rectangleJoystick.y + rectangleJoystick.height / 2;

        this.offset = new Vector2(0, 0);
        this.norm = new Vector2(0, 0);

        /* Ни один тач не нажат */
        this.touchId = -1;
    }

    public void render(SpriteBatch batch) {
        batch.setColor(1, 1, 1, 0.5f);
        batch.draw(textureBase, rectangleJoystick.x, rectangleJoystick.y);
        batch.setColor(1, 1, 1, 0.7f);
        batch.draw(textureStick, joyCenterX + offset.x - 25, joyCenterY + offset.y - 25);
        batch.setColor(1, 1, 1, 0.7f);
        batch.draw(textureFire, rectangleFire.x, rectangleFire.y);
        batch.draw(textureMagic, rectangleMagic.x, rectangleMagic.y);
        batch.setColor(1, 1, 1, 1);
    }

    public void update(float dt) {
        /* Какой тач нажат? */
        if (touchId == -1) touchId = mip.isTouchedInArea(rectangleJoystick);

        /* Если тач нажат, то получить вектор смещения */
        if (touchId > -1) {
            float touchX = mip.getX(touchId);
            float touchY = mip.getY(touchId);
            offset.x = touchX - joyCenterX;
            offset.y = touchY - joyCenterY;
            /* Также запрещаем стику выходить за базу джойстика */
            if (offset.len() > 75) offset.nor().scl(75);
        }

        /* Если отпустили стик, то сросить смещение и touchId */
        if (touchId > -1 && !mip.isTouched(touchId)) {
            touchId = -1;
            offset.x = 0;
            offset.y = 0;
        }

        /* Получаем направление */
        norm.set(offset).nor();

        /* Если зажата кнопка FIRE, то производить огонь */
        if (mip.isTouchedInArea(rectangleFire) != -1) player.pressFire(dt);

         /* Если зажата кнопка MAGIC, то активировать волшебство */
        if (mip.isTouchedInArea(rectangleMagic) != -1) player.magicShield();
    }

    /**
     * Возвращает значение в зависимости от положения стика джойстика
     *
     * @return float
     */
    public float getPower() {
        return offset.len() / 75.0f;
    }

    /**
     * Возвращает направление стика джойстика
     *
     * @return Vector2
     */
    public Vector2 getDirection() {
        return norm;
    }
}
