package ui;

import main.GamePanel;
import main.GameState;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class UI {

    GamePanel gp;
    Font maruMonica;

    public HUD             hud;
    public TitleScreen     titleScreen;
    public PauseScreen     pauseScreen;
    public DialogueScreen  dialogueScreen;
    public CharacterScreen characterScreen;
    public OptionsScreen   optionsScreen;
    public GameOverScreen  gameOverScreen;
    public TransitionScreen transitionScreen;
    public TradeScreen     tradeScreen;

    public UI(GamePanel gp) {
        this.gp = gp;
        try {
            InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        hud              = new HUD(gp);
        titleScreen      = new TitleScreen(gp);
        pauseScreen      = new PauseScreen(gp);
        dialogueScreen   = new DialogueScreen(gp);
        characterScreen  = new CharacterScreen(gp);
        optionsScreen    = new OptionsScreen(gp);
        gameOverScreen   = new GameOverScreen(gp);
        transitionScreen = new TransitionScreen(gp);
        tradeScreen      = new TradeScreen(gp);
    }

    public void addMessage(String text) {
        hud.addMessage(text);
    }

    public void showDialogue(String text) {
        dialogueScreen.currentDialogue = text;
        gp.gameState = GameState.DIALOGUE;
    }

    public void update() {
        if (gp.gameState == GameState.TRANSITION) transitionScreen.update();
    }

    public void onKeyPressed(int code) {
        switch (gp.gameState) {
            case TITLE     -> titleScreen.onKeyPressed(code);
            case PAUSE     -> pauseScreen.onKeyPressed(code);
            case DIALOGUE  -> dialogueScreen.onKeyPressed(code);
            case CHARACTER -> characterScreen.onKeyPressed(code);
            case OPTIONS   -> optionsScreen.onKeyPressed(code);
            case GAME_OVER -> gameOverScreen.onKeyPressed(code);
            case TRADE     -> tradeScreen.onKeyPressed(code);
        }
    }

    public void draw(Graphics2D g2) {
        g2.setFont(maruMonica);
        g2.setColor(Color.white);

        if (gp.gameState == GameState.TITLE) {
            titleScreen.draw(g2);
            return;
        }

        hud.drawPlayerLife(g2);
        hud.drawMessage(g2);

        switch (gp.gameState) {
            case PAUSE      -> pauseScreen.draw(g2);
            case DIALOGUE   -> dialogueScreen.draw(g2);
            case CHARACTER  -> characterScreen.draw(g2);
            case OPTIONS    -> optionsScreen.draw(g2);
            case GAME_OVER  -> gameOverScreen.draw(g2);
            case TRANSITION -> transitionScreen.draw(g2);
            case TRADE      -> tradeScreen.draw(g2);
        }
    }
}
