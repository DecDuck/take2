import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.stream.Collectors;
import javax.swing.SwingUtilities;

import engine.GameEngine;
import engine.assets.Asset;
import engine.rendering.AsyncRenderController;
import engine.rendering.RenderUtil;
import engine.rendering.classes.FramePart;
import engine.rendering.classes.Pixel;
import engine.util.FloatArea;
import engine.util.FloatPoint;

class Game {
    public static GameEngine engine;

    public static FloatPoint player = new FloatPoint(100, 100);

    public static void main(String[] args) {
        engine = new GameEngine();
        engine.StartRenderCycle();
        engine.SetCursorHidden(true);
        engine.assetManager.LoadQueue("bg.jpg", "cursor.png");
        while(engine.assetManager.loading){
            engine.AddRender(RenderUtil.bigText(engine.GetX(), engine.GetY(), "Loading..."));
            engine.PushRender();
        }
        FramePart background = null;
        int windowX = 0;
        int windowY = 0;
        try {
            engine.uiManager.CreateButton("Test!", new FloatArea(120, 120, 50, 40), engine.uiManager.getClass().getMethod("TestButton"));
        } catch (NoSuchMethodException | SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        while (engine.isRunning) {
            if(windowX != engine.GetX() || windowY != engine.GetY()){
                windowX = engine.GetX();
                windowY = engine.GetY();
                System.out.println(windowX + ":" + windowY);
                background = background(windowX, windowY, engine.assetManager.Get(0));
            }
            engine.AddRender(background);
            engine.uiManager.Update();
            engine.uiManager.AddRender();
            Point p = engine.inputManager.GetMouse();
            engine.AddRender(mouse(p.x, p.y, 32, 32));
            engine.AddRender(RenderUtil.framerate(engine.DeltaTime()));
            
            if(engine.inputManager.GetKeyDown('W')){
                player.y -= 10000 * engine.LoopTime();
            }
            if(engine.inputManager.GetKeyDown('S')){
                player.y += 10000 * engine.LoopTime();
            }
            if(engine.inputManager.GetKeyDown('A')){
                player.x -= 10000 * engine.LoopTime();
            }
            if(engine.inputManager.GetKeyDown('D')){
                player.x += 10000 * engine.LoopTime();
            }
            engine.AddRender(RenderUtil.circle((int)player.x, (int)player.y, 30));
            engine.PushRender();
        }

    }
    public static FramePart background(int width, int height, Asset background){
        BufferedImage bg = background.GetBufferedImage(width, height);
        int[] data = new int[width*height*4];
        for(int i = 0; i < data.length; i+=4){
            int color = bg.getRGB((i/4)%width, (int)(i/4)/width);
            data[i] = color & 0xff;
            data[i+1] = (color & 0xff00) >> 8;
            data[i+2] = (color & 0xff0000) >> 16;
            data[i+3] = (color & 0xff000000) >>> 24;
        }
        return new FramePart(0, 0, width, height, data);
    }

    public static FramePart mouse(int x, int y, int width, int height){
        BufferedImage bg = engine.assetManager.Get(1).GetBufferedImage(width, height);
        int[] data = new int[width*height*4];
        for(int i = 0; i < data.length; i+=4){
            int color = bg.getRGB((i/4)%width, (int)(i/4)/width);
            data[i] = color & 0xff;
            data[i+1] = (color & 0xff00) >> 8;
            data[i+2] = (color & 0xff0000) >> 16;
            data[i+3] = (color & 0xff000000) >>> 24;
        }
        return new FramePart(x, y, width, height, data);
    }

    public static <T> List<T> fromArrayToList(T[] a) { 
	    return Arrays.stream(a).collect(Collectors.toList());
	}
}