package io.github.wallKickerz;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Spring {

    private static final float SPRING_SCALE = 2f;
    private Texture texture;
    private Rectangle bounds;
    private TextureRegion region;
    private float x, y;
    private float width, height;

    public Spring(Texture texture, float x, float y) {
        this.texture = texture;
        this.region = new TextureRegion(texture);
        this.width = texture.getWidth() * SPRING_SCALE;
        this.height = texture.getHeight() * SPRING_SCALE;

        this.x = x;
        this.y = y;

        bounds = new Rectangle(x, y, width, height);
    }

    public void render(SpriteBatch batch) {
        batch.draw(region, x, y, width, height);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public float getY() {
        return y;
    }

    public float getHeight() {
        return height;
    }

    public void update(float delta) {
        bounds.setPosition(x, y);
    }

    public void dispose() {
        // Nada que hacer si no creas Textures manualmente
    }
}
