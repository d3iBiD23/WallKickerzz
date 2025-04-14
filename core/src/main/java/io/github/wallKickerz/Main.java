package io.github.wallKickerz;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    // Variables para el suelo
    private Texture platformsTexture;
    private TextureRegion singlePlatform;
    private final float SCALE_FACTOR = 10.0f;
    private Rectangle groundHitbox;
    private float groundTileWidth, groundTileHeight;

    // Variables para el caballero (idle animation)
    private Texture knightTexture;
    // Animación idle a partir del primer row
    private Animation<TextureRegion> idleAnimation;
    private float stateTime;
    private float knightX, knightY;
    private float knightVelocityY;
    private final float GRAVITY = -800f;
    private final float KNIGHT_SCALE = 10.0f;
    private Rectangle knightHitbox;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // --- Suelo ---
        platformsTexture = new Texture("brackeys_platformer_assets/sprites/platforms.png");
        // Extraemos la porción que corresponde a la segunda plataforma verde y larga.
        // Ajusta estos valores (srcX, srcY, width, height) según tu imagen.
        int platformSrcX = 13;
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
        // Asumimos que el sprite sheet del knight está organizado en 5 filas (animaciones)
        // y que la animación "idle" es la primera fila.
        // Supongamos además que la animación idle tiene 6 frames.
        int numAnimations = 5;
        int framesIdle = 6; // Ajusta este número según tu sprite
        int frameWidth = knightTexture.getWidth() / framesIdle;
        int frameHeight = knightTexture.getHeight() / numAnimations;

        // Dividimos el sprite en una matriz (cada fila es una animación)
        TextureRegion[][] regions = TextureRegion.split(knightTexture, frameWidth, frameHeight);
        // La primera fila (índice 0) es la animación idle
        TextureRegion[] idleFrames = regions[0];
        // Creamos la animación idle con una duración de 0.1 segundos por frame
        idleAnimation = new Animation<TextureRegion>(0.1f, idleFrames);
        idleAnimation.setPlayMode(Animation.PlayMode.LOOP);
        stateTime = 0f;

        // Posicionamos al caballero centrado y justo sobre el suelo.
        knightX = (screenWidth - frameWidth * KNIGHT_SCALE) / 2f;
        knightY = groundHitbox.y + groundHitbox.height;
        knightVelocityY = 0f;
        knightHitbox = new Rectangle(knightX, knightY, frameWidth * KNIGHT_SCALE, frameHeight * KNIGHT_SCALE);
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        stateTime += dt;

        // Física básica: aplicar gravedad al caballero
        knightVelocityY += GRAVITY * dt;
        knightY += knightVelocityY * dt;
        float groundTop = groundHitbox.y + groundHitbox.height;
        if (knightY < groundTop) {
            knightY = groundTop;
            knightVelocityY = 0;
        }
        knightHitbox.setPosition(knightX, knightY);

        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        // Dibujar el suelo repetitivo
        for (float x = 0; x < groundHitbox.width; x += groundTileWidth) {
            batch.draw(singlePlatform, x, 0, groundTileWidth, groundTileHeight);
        }
        // Obtener el frame actual de la animación idle y dibujarlo
        TextureRegion currentFrame = idleAnimation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, knightX, knightY, currentFrame.getRegionWidth() * KNIGHT_SCALE, currentFrame.getRegionHeight() * KNIGHT_SCALE);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        platformsTexture.dispose();
        knightTexture.dispose();
    }
}
