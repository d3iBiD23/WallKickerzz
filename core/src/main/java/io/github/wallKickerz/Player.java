package io.github.wallKickerz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Player {
    private static final float SCALE = 2.5f;
    private static final float GRAVITY = -2500f;
    private static final float JUMP_VELOCITY = 1800f;
    private static final float MOVE_SPEED = 600f;

    private final TextureRegion texture;
    private final Rectangle hitbox;
    private float x, y;
    private float velocityY = 0;
    private float velocityX = 0;

    public float getVelocityY() {
        return velocityY;
    }

    public Player(AssetManager assetManager) {
        this.texture = assetManager.getPlayerTexture();
        float width = texture.getRegionWidth() * SCALE;
        float height = texture.getRegionHeight() * SCALE;
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
        } else {
            velocityX = 0;
        }
    }

    public void jumpHigher() {
        this.velocityY = 5000f; // o cualquier valor más alto que tu salto normal
    }

    public void update(float delta, Array<Platform> platforms) {
        // Aplicar gravedad
        velocityY += GRAVITY * delta;
        y += velocityY * delta;
        x += velocityX * delta;

        // Limitar movimiento horizontal
        float width = texture.getRegionWidth() * SCALE;
        if (x < 0) x = 0;
        if (x + width > Gdx.graphics.getWidth()) x = Gdx.graphics.getWidth() - width;

        // Actualizar hitbox
        hitbox.setPosition(x + 10f, y + 5f);

        // Verificar colisiones con plataformas
        for (Platform platform : platforms) {
            if (isLandingOnPlatform(platform, delta)) {
                y = platform.getY() + platform.getHeight();
                velocityY = JUMP_VELOCITY;
                break;
            }
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
        batch.draw(texture, x, y, texture.getRegionWidth() * SCALE, texture.getRegionHeight() * SCALE);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setY(float y) {
        this.y = y;
    }
}
