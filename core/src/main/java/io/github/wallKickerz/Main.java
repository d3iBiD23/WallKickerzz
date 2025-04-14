package io.github.wallKickerz;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
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

    // Hitbox del suelo (representa el área física donde está el suelo)
    private Rectangle groundHitbox;
    private float groundTileWidth, groundTileHeight;

    // Físicas para el caballero
    private Texture knightTexture;
    private float knightX, knightY;
    private float knightVelocityY;
    private final float GRAVITY = -800f; // Gravedad en píxeles/seg^2 (ajusta según convenga)
    private final float KNIGHT_SCALE = 10.0f; // Factor para escalar el caballero
    private Rectangle knightHitbox;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // Cargamos la imagen del suelo (sprite sheet de plataformas)
        platformsTexture = new Texture("brackeys_platformer_assets/sprites/platforms.png");
        // Aquí extraemos la porción que corresponde a la segunda plataforma verde y larga.
        // Ajusta estos valores (srcX, srcY, width, height) según los datos reales que obtengas de la imagen.
        int srcX = 13;   // Por ejemplo, comienza en x = 13
        int srcY = 0;
        int width = 32;  // Ancho original del tile en la imagen
        int height = 16; // Alto original del tile en la imagen
        singlePlatform = new TextureRegion(platformsTexture, srcX, srcY, width, height);

        // Calculamos las dimensiones escaladas para el suelo
        groundTileWidth = singlePlatform.getRegionWidth() * SCALE_FACTOR;
        groundTileHeight = singlePlatform.getRegionHeight() * SCALE_FACTOR;

        // Creamos la hitbox del suelo: se extiende a lo largo de la pantalla (en x) y tiene la altura del tile escalado.
        float screenWidth = Gdx.graphics.getWidth();
        groundHitbox = new Rectangle(0, 0, screenWidth, groundTileHeight);

        // Cargamos la textura del caballero
        knightTexture = new Texture("brackeys_platformer_assets/sprites/knight.png");
        // Colocamos al caballero centrado horizontalmente. Para ello, multiplicamos por el factor de escala.
        knightX = (screenWidth - knightTexture.getWidth() * KNIGHT_SCALE) / 2f;
        // Ubicamos al caballero justo sobre el suelo, es decir, en la parte superior del hitbox del suelo.
        knightY = groundHitbox.y + groundHitbox.height;
        knightVelocityY = 0; // Sin movimiento vertical inicial

        // Creamos la hitbox del caballero
        knightHitbox = new Rectangle(knightX, knightY, knightTexture.getWidth() * KNIGHT_SCALE, knightTexture.getHeight() * KNIGHT_SCALE);
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();

        // Simulación de física simple: aplicamos gravedad al caballero
        knightVelocityY += GRAVITY * dt;
        knightY += knightVelocityY * dt;

        // Control de colisión con el suelo: si el caballero cae por debajo del suelo, se coloca justo encima
        float groundTop = groundHitbox.y + groundHitbox.height;
        if (knightY < groundTop) {
            knightY = groundTop;
            knightVelocityY = 0;
        }

        // Actualizamos la posición de la hitbox del caballero
        knightHitbox.setPosition(knightX, knightY);

        // Limpiamos la pantalla
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        // Dibujamos el suelo repitiendo el tile en toda la parte inferior
        for (float x = 0; x < groundHitbox.width; x += groundTileWidth) {
            batch.draw(singlePlatform, x, 0, groundTileWidth, groundTileHeight);
        }
        // Dibujamos el caballero en su posición, escalado
        batch.draw(knightTexture, knightX, knightY, knightTexture.getWidth() * KNIGHT_SCALE, knightTexture.getHeight() * KNIGHT_SCALE);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        platformsTexture.dispose();
        knightTexture.dispose();
    }
}
