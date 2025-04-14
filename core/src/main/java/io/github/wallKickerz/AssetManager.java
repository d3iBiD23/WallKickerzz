package io.github.wallKickerz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetManager {
    private Texture backgroundTexture;
    private Texture platformTexture;
    private Texture playerTexture;

    public AssetManager() {
        loadAssets();
    }

    private void loadAssets() {
        backgroundTexture = new Texture(Gdx.files.internal("PNG/Background.png"));
        platformTexture = new Texture(Gdx.files.internal("PNG/LandPiece_DarkGreen.png"));
        playerTexture = new Texture(Gdx.files.internal("PNG/CharacterLeft_Jump.png"));
    }

    public void drawBackground(SpriteBatch batch) {
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public TextureRegion getPlatformTexture() {
        // Extraemos una región específica de la textura (como en tu código original)
        return new TextureRegion(platformTexture, 13, 0, 32, 45);
    }

    public TextureRegion getPlayerTexture() {
        return new TextureRegion(playerTexture);
    }

    public void dispose() {
        backgroundTexture.dispose();
        platformTexture.dispose();
        playerTexture.dispose();
    }
}
