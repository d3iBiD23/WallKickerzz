package io.github.wallKickerz;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture platformsTexture;
    // Región que corresponde a la segunda plataforma verde (ajusta las coordenadas según tu sprite)
    private TextureRegion singlePlatform;

    // Factor de escala para agrandar la plataforma (por ejemplo, 2.0 para el doble)
    private final float SCALE_FACTOR = 10.0f;

    @Override
    public void create() {
        batch = new SpriteBatch();
        platformsTexture = new Texture("brackeys_platformer_assets/sprites/platforms.png");

        // Ajusta estos valores a la porción exacta de tu plataforma verde y larga.
        // Por ejemplo: si la segunda plataforma empieza en (srcX, srcY) y mide (width x height).
        int srcX = 13;   // Ejemplo, ajusta según tu imagen.
        int srcY = 0;
        int width = 32;  // Ancho original del tile en la imagen.
        int height = 16; // Alto original del tile en la imagen.

        // Creamos la región de la textura con las dimensiones originales.
        singlePlatform = new TextureRegion(platformsTexture, srcX, srcY, width, height);
    }

    @Override
    public void render() {
        // Limpiar la pantalla
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        // Calculamos las dimensiones escaladas de la plataforma.
        float scaledTileWidth = singlePlatform.getRegionWidth() * SCALE_FACTOR;
        float scaledTileHeight = singlePlatform.getRegionHeight() * SCALE_FACTOR;

        int screenWidth = Gdx.graphics.getWidth();
        // Dibujamos la plataforma de manera repetitiva en la parte inferior, usando el ancho escalado.
        for (int x = 0; x < screenWidth; x += scaledTileWidth) {
            batch.draw(singlePlatform, x, 0, scaledTileWidth, scaledTileHeight);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        platformsTexture.dispose();
    }
}
