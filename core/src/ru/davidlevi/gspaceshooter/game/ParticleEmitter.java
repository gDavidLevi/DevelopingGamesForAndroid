package ru.davidlevi.gspaceshooter.game;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Класс ParticleEmitter
 *
 * @see Poolable помещается в пул объектов
 */
public class ParticleEmitter extends ObjectPool<Particle> {
    private TextureRegion particleTexture;

    public ParticleEmitter(TextureRegion particleTexture) {
        this.particleTexture = particleTexture;
    }

    @Override
    protected Particle newObject() {
        return new Particle();
    }

    public void render(SpriteBatch batch) {
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        int quantity = activeList.size();

        /* Эффект 1 */
        for (int i = 0; i < quantity; i++)
            func01(batch, i);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);

        /* Эффект 2 */
        for (int i = 0; i < quantity; i++)
            func01(batch, i);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void update(float dt) {
        int quantity = activeList.size();
        for (int i = 0; i < quantity; i++)
            activeList.get(i).update(dt);
    }

    private void func01(SpriteBatch batch, int i) {
        Particle particle = activeList.get(i);
        float t = particle.getCurrentTime() / particle.getMaxTime();
        float scale = func02(particle.getSize1(), particle.getSize2(), t);
        if (Math.random() < 0.04) scale *= 2.0f;
        batch.setColor(func02(particle.getR1(), particle.getR2(), t), func02(particle.getG1(), particle.getG2(), t), func02(particle.getB1(), particle.getB2(), t), func02(particle.getA1(), particle.getA2(), t));
        batch.draw(particleTexture, particle.getPosition().x - 8, particle.getPosition().y - 8, 8, 8, 16, 16, scale, scale, 0);
    }

    private float func02(float value1, float value2, float point) {
        return value1 + (value2 - value1) * point;
    }

    /*
     * Установка
     */
    public void setup(float x, float y, float vx, float vy, float maxTimeOfLife,
                      float size1, float size2,
                      float r1, float g1, float b1, float a1, float r2, float g2, float b2, float a2) {
        Particle particle = getActiveElement();
        particle.init(x, y, vx, vy, maxTimeOfLife, size1, size2, r1, g1, b1, a1, r2, g2, b2, a2);
    }
}
