package io.github.wallKickerz;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Spring {
    private Texture texture;
    private Rectangle bounds;
    private TextureRegion region;
    private float x, y;

    public Spring(Texture texture, float x, float y) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.region = new TextureRegion(texture);
        bounds = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
    }

    public void render(SpriteBatch batch) {
        batch.draw(region, x, y);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public float getY() {
        return y;
    }

    public float getHeight() {
        return bounds.height;
    }

    public void update(float delta) {
        bounds.setPosition(x, y); // por si se mueve
    }

    public void dispose() {
    }
}
