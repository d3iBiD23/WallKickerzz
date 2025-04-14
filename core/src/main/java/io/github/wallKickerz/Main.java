package io.github.wallKickerz;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import jdk.jfr.consumer.RecordedClass;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Texture platformsTexture;
    private TextureRegion singlePlatform;
    private Texture knightTexture;

    private final float SCALE_FACTOR = 3.0f;
    private final float KNIGHT_SCALE = 3.0f;

    private Rectangle groundHitbox;
    private float groundTileWidth, groundTileHeight;

    private float knightX, knightY;
    private Rectangle knightHitbox;

    @Override
    public void create() {
        batch = new SpriteBatch();

        backgroundTexture = new Texture("PNG/Background.png");
        platformsTexture = new Texture("PNG/LandPiece_DarkGreen.png");
        knightTexture = new Texture("PNG/CharacterLeft_Jump.png");

        // Suelo
        int platformSrcX = 13;
        int platformSrcY = 0;
        int platformWidth = 32;
        int platformHeight = 45;
        singlePlatform = new TextureRegion(platformsTexture, platformSrcX, platformSrcY, platformWidth, platformHeight);

        groundTileWidth = singlePlatform.getRegionWidth() * SCALE_FACTOR;
        groundTileHeight = singlePlatform.getRegionHeight() * SCALE_FACTOR;

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        groundHitbox = new Rectangle(0, 0, screenWidth, groundTileHeight);

        // Posicionar al personaje justo encima del suelo, centrado
        knightX = (screenWidth - knightTexture.getWidth() * KNIGHT_SCALE) / 2f;
        knightY = groundHitbox.y + groundHitbox.height;

        // Ajustar la posición Y del personaje para que esté justo encima del suelo
        float knightWidth = knightTexture.getWidth() * KNIGHT_SCALE;
        float knightHeight = knightTexture.getHeight() * KNIGHT_SCALE;

        // Hitbox ajustada más estrecha y un poco más baja (ajusta como quieras)
        float hitboxOffsetX = 10f;
        float hitboxOffsetY = 5f;
        float hitboxWidth = knightWidth - 20f;  // más estrecho
        float hitboxHeight = knightHeight - 10f; // más bajo

        knightHitbox = new Rectangle(knightX + hitboxOffsetX, knightY + hitboxOffsetY, hitboxWidth, hitboxHeight);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        for (float x = 0; x < groundHitbox.width; x += groundTileWidth) {
            batch.draw(singlePlatform, x, 0, groundTileWidth, groundTileHeight);
        }

        batch.draw(knightTexture, knightX, knightY,
            knightTexture.getWidth() * KNIGHT_SCALE,
            knightTexture.getHeight() * KNIGHT_SCALE);
        knightHitbox.setPosition(knightX + 10f, knightY + 5f); // Actualizar la posición de la hitbox
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundTexture.dispose();
        platformsTexture.dispose();
        knightTexture.dispose();
    }
}
