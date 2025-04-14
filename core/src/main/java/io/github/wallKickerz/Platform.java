package io.github.wallKickerz;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Platform {
    private final TextureRegion texture;
    private final Rectangle bounds;

    public Platform(float x, float y, float width, float height, AssetManager assetManager) {
        this.bounds = new Rectangle(x, y, width, height);
        this.texture = assetManager.getPlatformTexture();
    }

    public void render(SpriteBatch batch) {
        // Para el suelo, dibujamos múltiples texturas para cubrir el ancho
        if (bounds.y == 0) { // Es el suelo
            float tileWidth = texture.getRegionWidth() * 2.0f;
            for (float x = 0; x < bounds.width; x += tileWidth) {
                batch.draw(texture, x, 0, tileWidth, bounds.height);
            }
        } else { // Plataforma flotante
            batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    // Getters
    public float getX() { return bounds.x; }
    public float getY() { return bounds.y; }
    public float getWidth() { return bounds.width; }
    public float getHeight() { return bounds.height; }
    public Rectangle getBounds() { return bounds; }
}
