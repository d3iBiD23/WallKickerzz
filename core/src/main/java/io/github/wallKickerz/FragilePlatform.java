package io.github.wallKickerz;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FragilePlatform extends Platform {
    private enum State { INTACT, BROKEN }
    private State state = State.INTACT;
    private final TextureRegion intactRegion;
    private final TextureRegion brokenRegion;
    private float breakTimer = 0f;
    private static final float TIME_TO_DISAPPEAR = 0.3f; // segs tras romperse

    public FragilePlatform(float x, float y, float width, AssetManager assets) {
        super(x, y, width, 0, assets, false);
        this.intactRegion = assets.getFragilePlatformTexture();  // Textura intacta
        this.brokenRegion = assets.getBrokenFloatingPlatformTexture();  // Textura rota
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
            batch.draw(intactRegion, getX(), getY(), getWidth(), getHeight()); // Dibuja la plataforma intacta
        } else {
            batch.draw(brokenRegion, getX(), getY(), getWidth(), getHeight()); // Dibuja la plataforma rota
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
