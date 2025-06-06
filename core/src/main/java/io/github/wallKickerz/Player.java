package io.github.wallKickerz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Player {
    private static final float SCALE = 2.5f;
    private static final float GRAVITY = -2500f;
    private static final float JUMP_VELOCITY = 1800f;
    private static final float MOVE_SPEED = 600f;

    private final Rectangle hitbox;
    private float x, y;
    private float velocityY = 0;
    private float velocityX = 0;
    private enum State {
        JUMPING, STANDING
    }

    private State currentState;
    private boolean facingRight;

    private TextureRegion currentTexture;
    private final AssetManager assetManager;

    public float getVelocityY() {
        return velocityY;
    }

    public Player() {
        this.assetManager = new AssetManager();
        this.currentState = State.STANDING;
        this.currentTexture = assetManager.getPlayerLeftStandingRegion();
        float width = currentTexture.getRegionWidth() * SCALE;
        float height = currentTexture.getRegionHeight() * SCALE;
        this.x = (Gdx.graphics.getWidth() - width) / 2f;
        this.y = 100; // Posición inicial encima del suelo

        // Hitbox ajustada (más estrecha y un poco más baja)
        float hitboxOffsetX = 10f;
        float hitboxOffsetY = 5f;
        float hitboxWidth = width - 20f;
        float hitboxHeight = height - 10f;
        this.hitbox = new Rectangle(x + hitboxOffsetX, y + hitboxOffsetY, hitboxWidth, hitboxHeight);
    }

    public void handleInput() {
        if (Gdx.input.isTouched()) {
            float touchX = Gdx.input.getX();
            velocityX = touchX < Gdx.graphics.getWidth() / 2f ? -MOVE_SPEED : MOVE_SPEED;
            facingRight = velocityX > 0;
        } else {
            velocityX = 0;
        }
    }

    public void jumpHigher() {
        // Reproducir efecto de sonido
        assetManager.getJumpSound().play(0.8f, 1.4f, 0f);

        this.velocityY = 5000f; // salto mejorado (e.g. tras muelle)
    }

    public void update(float delta, Array<Platform> platforms) {
        // Aplicar gravedad
        velocityY += GRAVITY * delta;
        y += velocityY * delta;
        x += velocityX * delta;

        // Limitar movimiento horizontal
        float width = currentTexture.getRegionWidth() * SCALE;
        if (x < 0) x = 0;
        if (x + width > Gdx.graphics.getWidth()) x = Gdx.graphics.getWidth() - width;

        // Actualiza la textura dependiendo de la dirección y estado
        if (currentState == State.STANDING) {
            currentTexture = facingRight ? assetManager.getPlayerRightStandingRegion() : assetManager.getPlayerLeftStandingRegion();
        } else if (currentState == State.JUMPING) {
            currentTexture = facingRight ? assetManager.getPlayerRightJumpRegion() : assetManager.getPlayerLeftJumpRegion();
        }
        // Actualizar hitbox
        hitbox.setPosition(x + 10f, y + 5f);


        // Verificar colisiones con plataformas
        for (Platform platform : platforms) {
            if (isLandingOnPlatform(platform, delta)) {
                // Reproducir sonido de salto al rebotar en plataforma
                assetManager.getJumpSound().play(0.8f, 1.4f, 0f);

                y = platform.getY() + platform.getHeight();
                velocityY = JUMP_VELOCITY;

                // Manejar plataforma frágil
                if (platform instanceof FragilePlatform) {
                    ((FragilePlatform) platform).breakPlatform();
                }
                break;
            }
        }
    }

    public void jump() {
        if (currentState == State.STANDING) {
            // Reproducir efecto de sonido
            assetManager.getJumpSound().play(0.8f, 1.4f, 0f);

            velocityY = JUMP_VELOCITY;
            currentState = State.JUMPING;
        }
    }

    private boolean isLandingOnPlatform(Platform platform, float delta) {
        float playerFeetY = hitbox.y;
        float playerPrevY = hitbox.y - velocityY * delta;

        boolean falling = velocityY <= 0;
        boolean hitsFromAbove = playerFeetY <= platform.getY() + platform.getHeight() &&
            playerPrevY >= platform.getY() + platform.getHeight();

        boolean overlapsX = hitbox.x + hitbox.width > platform.getX() &&
            hitbox.x < platform.getX() + platform.getWidth();

        return falling && hitsFromAbove && overlapsX;
    }

    public void render(SpriteBatch batch) {
        batch.draw(currentTexture, x, y, currentTexture.getRegionWidth() * SCALE, currentTexture.getRegionHeight() * SCALE);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setY(float y) {
        this.y = y;
    }
}
