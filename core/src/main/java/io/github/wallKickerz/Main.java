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
    // Suelo
    private Texture platformsTexture;
    private TextureRegion singlePlatform;
    private final float SCALE_FACTOR = 10.0f;
    private Rectangle groundHitbox;
    private float groundTileWidth, groundTileHeight;

    // Caballero (knight)
    private Texture knightTexture;
    // Nuevo: region que contiene solo el primer frame del knight.
    private TextureRegion knightFrame;
    private float knightX, knightY;
    private float knightVelocityY;
    private final float GRAVITY = -800f;
    // Factor de escala para el caballero, igual que antes
    private final float KNIGHT_SCALE = 10.0f;
    private Rectangle knightHitbox;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // --- Suelo ---
        platformsTexture = new Texture("brackeys_platformer_assets/sprites/platforms.png");
        // Valores ajustados a la porción que corresponde a la segunda plataforma verde y larga.
        int platformSrcX = 13;   // Valor ajustable según tu imagen
        int platformSrcY = 0;
        int platformWidth = 32;
        int platformHeight = 16;
        singlePlatform = new TextureRegion(platformsTexture, platformSrcX, platformSrcY, platformWidth, platformHeight);

        groundTileWidth = singlePlatform.getRegionWidth() * SCALE_FACTOR;
        groundTileHeight = singlePlatform.getRegionHeight() * SCALE_FACTOR;
        float screenWidth = Gdx.graphics.getWidth();
        groundHitbox = new Rectangle(0, 0, screenWidth, groundTileHeight);

        // --- Caballero ---
        knightTexture = new Texture("brackeys_platformer_assets/sprites/knight.png");
        // Suponiendo que el sprite sheet del knight contiene 6 frames dispuestos horizontalmente,
        // obtenemos el ancho de cada frame:
        int numFrames = 4; // Ajusta este número según tu sprite real
        int knightFrameWidth = knightTexture.getWidth() / numFrames;
        int knightFrameHeight = knightTexture.getHeight();
        // Extraemos solo el primer frame (la región que empieza en (0,0) y tiene el ancho calculado)
        knightFrame = new TextureRegion(knightTexture, 0, 0, knightFrameWidth, knightFrameHeight);

        // Posicionamos al caballero centrado y justo sobre el suelo.
        knightX = (screenWidth - knightFrameWidth * KNIGHT_SCALE) / 2f;
        knightY = groundHitbox.y + groundHitbox.height;
        knightVelocityY = 0f;
        knightHitbox = new Rectangle(knightX, knightY, knightFrameWidth * KNIGHT_SCALE, knightFrameHeight * KNIGHT_SCALE);
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();

        // Simulación básica de física: se aplica gravedad al caballero.
        knightVelocityY += GRAVITY * dt;
        knightY += knightVelocityY * dt;
        float groundTop = groundHitbox.y + groundHitbox.height;
        if (knightY < groundTop) {
            knightY = groundTop;
            knightVelocityY = 0;
        }
        knightHitbox.setPosition(knightX, knightY);

        // Limpieza de pantalla.
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        // Dibujar el suelo repetitivo.
        for (float x = 0; x < groundHitbox.width; x += groundTileWidth) {
            batch.draw(singlePlatform, x, 0, groundTileWidth, groundTileHeight);
        }
        // Dibujar solo el primer frame del caballero escalado.
        batch.draw(knightFrame, knightX, knightY, knightFrame.getRegionWidth() * KNIGHT_SCALE, knightFrame.getRegionHeight() * KNIGHT_SCALE);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        platformsTexture.dispose();
        knightTexture.dispose();
    }
}
