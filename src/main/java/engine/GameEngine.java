package engine;

import java.util.Random;

import engine.assets.AssetManager;
import engine.assets.SaveFileManager;
import engine.input.InputManager;
import engine.rendering.AsyncRenderController;
import engine.rendering.GameRenderer;
import engine.rendering.classes.FramePart;
import engine.util.UIManager;

import java.awt.Point;
import java.awt.Cursor;
import java.awt.image.*;

public class GameEngine {
    public GameRenderer renderer;

    public AssetManager assetManager;
    public InputManager inputManager;
    public SaveFileManager saveFileManager;
    public UIManager uiManager;
    public boolean isRunning = true;
    public Random random;

    private long deltaPrevious;
    private long deltaTime;

    private long loopPrevious;
    private long loopTime;

    public GameEngine(){
        renderer = new GameRenderer();
        renderer.engine = this;
        assetManager = new AssetManager();
        inputManager = new InputManager(this);
        uiManager = new UIManager(this);
        saveFileManager = new SaveFileManager();
        random = new Random();
        random.setSeed(System.nanoTime());
        //renderer.frame.addMouseListener(mouseManager);
        deltaPrevious = System.nanoTime();
    }

    public void PushRender(){
        renderer.PushRender();
        loopTime = System.nanoTime() - loopPrevious;
        loopPrevious = System.nanoTime();
    }

    public void Render(){
        renderer.Render();
        deltaTime = System.nanoTime() - deltaPrevious;
        deltaPrevious = System.nanoTime();
    }

    public void AddRender(FramePart part){
        renderer.AddRender(part);
    }

    public double DeltaTime(){
        return deltaTime / 100000000000.0;
    }

    public double LoopTime(){
        return loopTime / 100000000000.0;
    }

    public int GetX(){
        return renderer.frame.getWidth();
    }

    public int GetY(){
        return renderer.frame.getHeight();
    }

    public void SetCursorHidden(boolean b){
        if(b){
            BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Cursor blankCursor = renderer.frame.getToolkit().createCustomCursor(
                cursorImg, new Point(0, 0), "blank cursor");
            renderer.frame.setCursor(blankCursor);
        }else{
            renderer.frame.setCursor(Cursor.getDefaultCursor());
        }
    }

    public void StartRenderCycle(){
        new AsyncRenderController(this, 10000000).start();
    }
}
