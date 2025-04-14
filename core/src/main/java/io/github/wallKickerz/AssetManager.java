package io.github.wallKickerz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetManager {
    private Texture backgroundTexture;
    private Texture platformTexture;
    private Texture playerTexture;

    private Texture groundPlatformTexture; // Textura para el suelo
    private Texture floatingPlatformTexture; // Textura para plataformas flotantes
    public AssetManager() {
        loadAssets();
    }

    private void loadAssets() {
        backgroundTexture = new Texture(Gdx.files.internal("PNG/Background.png"));
        groundPlatformTexture = new Texture(Gdx.files.internal("PNG/LandPiece_DarkGreen.png"));
        floatingPlatformTexture = new Texture(Gdx.files.internal("PNG/LandPiece_DarkGray.png"));
        playerTexture = new Texture(Gdx.files.internal("PNG/CharacterLeft_Jump.png"));
    }

    public void drawBackground(SpriteBatch batch) {
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public TextureRegion getGroundPlatformTexture() {
        return new TextureRegion(groundPlatformTexture, 13, 0, 32, 45);
    }

    public TextureRegion getFloatingPlatformTexture() {
        return new TextureRegion(floatingPlatformTexture, 13, 0, 80, 52);
    }

    public TextureRegion getPlayerTexture() {
        return new TextureRegion(playerTexture);
    }

    public void dispose() {
        backgroundTexture.dispose();
        groundPlatformTexture.dispose();
        floatingPlatformTexture.dispose();
        playerTexture.dispose();
    }
}
