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
            float textureWidth = texture.getRegionWidth();
            float textureHeight = texture.getRegionHeight();
            height = (width / textureWidth) * textureHeight; // Nueva altura proporcional
        }

        this.bounds = new Rectangle(x, y, width, height);
    }

    public void render(SpriteBatch batch) {
        if (isGround) {
            // Para el suelo, dibujamos múltiples texturas para cubrir el ancho
            float tileWidth = texture.getRegionWidth() * 2.0f;
            for (float x = 0; x < bounds.width; x += tileWidth) {
                batch.draw(texture, x, bounds.y, tileWidth, bounds.height);
            }
        } else {
            // Plataforma flotante: escalamos proporcionalmente al ancho deseado
            float textureWidth = texture.getRegionWidth();
            float textureHeight = texture.getRegionHeight();

            // Calcula la escala para mantener la proporción original
            float scale = bounds.width / textureWidth; // Escala basada en el ancho deseado

            // Dibuja la textura escalada
            batch.draw(
                    texture,
                    bounds.x, bounds.y,
                    0, 0,
                    textureWidth, textureHeight,
                    scale, scale, // Misma escala en X e Y para mantener proporciones
                    0
            );
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
