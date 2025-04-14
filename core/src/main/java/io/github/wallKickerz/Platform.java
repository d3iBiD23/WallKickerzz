package io.github.wallKickerz;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Platform {
    private final TextureRegion texture;
    private final Rectangle bounds;
    private final boolean isGround;

    public Platform(float x, float y, float width, float height, AssetManager assetManager, boolean isGround) {
        this.bounds = new Rectangle(x, y, width, height);
        this.isGround = isGround;
        this.texture = isGround ?
                assetManager.getGroundPlatformTexture() :
                assetManager.getFloatingPlatformTexture();
    }

    public void render(SpriteBatch batch) {
        if (isGround) {
            // Para el suelo, dibujamos múltiples texturas para cubrir el ancho
            float tileWidth = texture.getRegionWidth() * 2.0f;
            for (float x = 0; x < bounds.width; x += tileWidth) {
                batch.draw(texture, x, bounds.y, tileWidth, bounds.height);
            }
        } else {
            // Plataforma flotante
            batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    // Getters
    public float getX() {
        return bounds.x;
    }

    public float getY() {
        return bounds.y;
    }

    public float getWidth() {
        return bounds.width;
    }

    public float getHeight() {
        return bounds.height;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isGround() {
        return isGround;
    }
}
