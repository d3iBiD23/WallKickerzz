package io.github.wallKickerz;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Platform {
    private final TextureRegion texture;
    private final Rectangle bounds;
    private final boolean isGround;

    public Platform(float x, float y, float width, float height, AssetManager assetManager, boolean isGround) {
        this.isGround = isGround;
        this.texture = isGround ? assetManager.getGroundPlatformTexture() : assetManager.getFloatingPlatformTexture();

        // Ajustar la altura proporcionalmente al ancho si es una plataforma flotante
        if (!isGround) {
            float textureWidth = texture.getRegionWidth(); // Escala horizontal
            float textureHeight = texture.getRegionHeight() ;
            height = (width / textureWidth) * textureHeight; // Nueva altura proporcional
        }

        this.bounds = new Rectangle(x, y, width, height);
    }

    public void render(SpriteBatch batch) {
        if (isGround) {
            // Para el suelo, dibujamos múltiples texturas para cubrir el ancho
            float tileWidth = texture.getRegionWidth();
            float tileHeight = texture.getRegionHeight(); // Escala vertical aumentada

            // Calcula cuántas veces debemos repetir la textura horizontalmente
            int tilesX = (int) Math.ceil(bounds.width / tileWidth);

            // Dibuja cada tile del suelo
            for (int i = 0; i <= tilesX; i++) {
                batch.draw(texture, bounds.x + i * tileWidth, bounds.y, tileWidth, bounds.height);
            }
        } else {
            // Plataforma flotante: escalamos proporcionalmente al ancho deseado
            float textureWidth = texture.getRegionWidth() * 5.0f; // Escala horizontal
            float textureHeight = texture.getRegionHeight() * 1.5f; // Escala vertical
            float scale = bounds.width / textureWidth;

            batch.draw(texture, bounds.x, bounds.y, 0, 0, textureWidth, textureHeight, scale, scale, 0);
        }
    }

    // Getters
    public float getX() {
        return bounds.x;
    }

    public float getWidth() {
        return bounds.width;
    }

    public float getY() {
        return bounds.y;
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
