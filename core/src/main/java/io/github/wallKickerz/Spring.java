package io.github.wallKickerz;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Spring {
    private Texture texture;
    private Rectangle bounds;
    private float x, y;

    public Spring(Texture texture, float x, float y) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.bounds = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public float getY() {
        return y;
    }

    public void dispose() {
        texture.dispose();
    }
}
