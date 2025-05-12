package io.github.wallKickerz;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FragilePlatform extends Platform {
    private enum State { INTACT, BROKEN }
    private State state = State.INTACT;
    private final TextureRegion brokenRegion;
    private float breakTimer = 0f;
    private static final float TIME_TO_DISAPPEAR = 0.5f; // segs tras romperse

    public FragilePlatform(float x, float y, float width, AssetManager assets) {
        super(x, y, width, 0, assets, false);
        this.brokenRegion = assets.getBrokenFloatingPlatformTexture();
    }

    /** Llamar cuando el jugador aterrice en esta plataforma */
    public void breakPlatform() {
        if (state == State.INTACT) {
            state = State.BROKEN;
            breakTimer = 0f;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (state == State.INTACT) {
            super.render(batch); // dibuja la textura normal :contentReference[oaicite:2]{index=2}:contentReference[oaicite:3]{index=3}
        } else {
            // dibuja la textura rota (sin escalar, asumiendo mismo tamaño que la original)
            float x = getX(), y = getY(), w = getWidth(), h = getHeight();
            batch.draw(brokenRegion, x, y, w, h);
        }
    }

    /** Actualiza el temporizador y devuelve true si debe desaparecer */
    public boolean updateAndCheckRemoval(float delta) {
        if (state == State.BROKEN) {
            breakTimer += delta;
            return breakTimer >= TIME_TO_DISAPPEAR;
        }
        return false;
    }
}
