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
import com.badlogic.gdx.utils.Array;

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

    // Textura y regiones para plataformas flotantes
    private Texture floatingPlatformTexture;
    private TextureRegion floatingPlatformRegion;
    private Array<Rectangle> floatingPlatforms;
    // Física de salto
    private float velocityY = 0;
    private final float GRAVITY = -900f;
    private final float JUMP_VELOCITY = 900f;
    private float knightVelocityX = 0f;
    private final float MOVE_SPEED = 350f;


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

        floatingPlatformTexture = new Texture("PNG/LandPiece_DarkGray.png");
        floatingPlatformRegion = new TextureRegion(floatingPlatformTexture);

        // Crear plataformas flotantes en posiciones fijas (puedes hacerlas aleatorias más adelante)
        floatingPlatforms = new Array<>();
        floatingPlatforms.add(new Rectangle(100, 500, 200, 50));
        floatingPlatforms.add(new Rectangle(250, 1100, 200, 50));
        floatingPlatforms.add(new Rectangle(150, 1300, 200, 50));

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Dibujar el suelo
        for (float x = 0; x < groundHitbox.width; x += groundTileWidth) {
            batch.draw(singlePlatform, x, 0, groundTileWidth, groundTileHeight);
        }

        // Dibujar plataformas flotantes
        for (Rectangle platform : floatingPlatforms) {
            batch.draw(floatingPlatformRegion, platform.x, platform.y, platform.width, platform.height);
        }

        batch.draw(knightTexture, knightX, knightY, knightTexture.getWidth() * KNIGHT_SCALE, knightTexture.getHeight() * KNIGHT_SCALE);
        knightHitbox.setPosition(knightX + 10f, knightY + 5f); // Actualizar la posición de la hitbox


        // Control táctil: mover izquierda/derecha según mitad de pantalla tocada
        if (Gdx.input.isTouched()) {
            float touchX = Gdx.input.getX();
            float screenWidth = Gdx.graphics.getWidth();

            if (touchX < screenWidth / 2f) {
                knightVelocityX = -MOVE_SPEED; // Mover a la izquierda
            } else {
                knightVelocityX = MOVE_SPEED; // Mover a la derecha
            }
        } else {
            knightVelocityX = 0f; // No tocar: no moverse
        }

        float dt = Gdx.graphics.getDeltaTime();
        velocityY += GRAVITY * dt;
        knightY += velocityY * dt;
        knightX += knightVelocityX * dt;

        // Limitar la velocidad máxima
        float knightWidth = knightTexture.getWidth() * KNIGHT_SCALE;
        // Limitar la posición X del personaje para que no salga de la pantalla
        if (knightX < 0) knightX = 0;
        if (knightX + knightWidth > Gdx.graphics.getWidth())
            knightX = Gdx.graphics.getWidth() - knightWidth;


        // Colisión con el suelo
        if (knightHitbox.overlaps(groundHitbox)) {
            if (velocityY <= 0 && knightHitbox.y >= groundHitbox.y + groundHitbox.height / 2f) {
                knightY = groundHitbox.y + groundHitbox.height;
                velocityY = JUMP_VELOCITY;
            }
        }

        // Colisión con plataformas flotantes
        for (Rectangle platform : floatingPlatforms) {
            float knightFeetY = knightHitbox.y;
            float knightPrevY = knightY - velocityY * dt;

            boolean falling = velocityY <= 0;
            boolean hitsFromAbove = knightFeetY <= platform.y + platform.height &&
                knightPrevY >= platform.y + platform.height;

            if (falling && hitsFromAbove &&
                knightHitbox.x + knightHitbox.width > platform.x &&
                knightHitbox.x < platform.x + platform.width) {

                knightY = platform.y + platform.height;
                velocityY = JUMP_VELOCITY;
            }
        }

        // Actualizar posición de hitbox del personaje
        knightHitbox.setPosition(knightX + 10f, knightY + 5f);

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
