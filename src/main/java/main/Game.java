package main;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

import engine.GameEngine;
import engine.rendering.RenderUtil;
import engine.rendering.classes.FramePart;
import engine.util.FloatArea;
import engine.util.FloatPoint;

public class Game {
    public static Game game;

    public GameEngine engine;

    public FloatPoint player = new FloatPoint(100, 100);

    public Color mainColor = new Color(231, 190, 134, 255);
    public Color loadingGradient = new Color(0, 0, 0, 255);
    public Font mainFont = new Font("Microsoft Sans Serif",Font.PLAIN, 128);
    public Font smallMainFont = new Font("Microsoft Sans Serif",Font.PLAIN, 70);

    public int cursorSize = 34;

    public String title = "DuxTux Factory";

    public boolean playing = false;

    public static void main(String[] args){
        game = new Game();
        game.Init(args);
    }

    public void Init(String[] args) {
        engine = new GameEngine();
        engine.StartRenderCycle();
        engine.SetCursorHidden(true);
        GraphicsEnvironment graphics =
                GraphicsEnvironment.getLocalGraphicsEnvironment();

        GraphicsDevice device = graphics.getDefaultScreenDevice();
        device.setFullScreenWindow(engine.renderer.frame);
        engine.assetManager.Load("cursor.png");
        engine.assetManager.LoadQueue("bg.jpg", "logo.png");
        double timer = 0;
        while(engine.assetManager.loading){
            timer += engine.DeltaTime();
            String s = "";
            switch(((int)timer) % 3){
                case 0:
                    s = ".";
                    break;
                case 1:
                    s = "..";
                    break;
                case 2:
                    s = "...";
                    break;
            }
            engine.AddRender(loadingBackground(engine.GetX(), engine.GetY()));
            engine.AddRender(new FramePart(title, engine.GetX()/2, engine.GetY()/3, Color.white, mainFont, true));
            engine.AddRender(new FramePart("Loading"+s, engine.GetX()/2, (engine.GetY()/3)*2, Color.white, smallMainFont, true));
            engine.AddRender(mouse(engine.inputManager.GetMouse(), cursorSize, cursorSize));
            engine.PushRender();
        }
        ImageIcon icon = new ImageIcon(engine.assetManager.Get(2).GetBufferedImage());
        engine.renderer.frame.setIconImage(icon.getImage());
        MainMenuLoop();
        System.exit(0);
    }

    public void StartPlay(){
        playing = true;
    }

    public void Play(){
        FramePart background = null;
        int windowX = 0;
        int windowY = 0;
        while(playing){
            if(windowX != engine.GetX() || windowY != engine.GetY()){
                windowX = engine.GetX();
                windowY = engine.GetY();
                background = background(windowX, windowY);
            }
            engine.AddRender(background);
            engine.AddRender(mouse(engine.inputManager.GetMouse(), cursorSize, cursorSize));
            engine.PushRender();
        }
    }

    public void Fade(FramePart... parts){
        FramePart background = null;
        int windowX = 0;
        int windowY = 0;
        double timer = 0.0;
        boolean done = false;
        while(!done){
            if(windowX != engine.GetX() || windowY != engine.GetY()){
                windowX = engine.GetX();
                windowY = engine.GetY();
                background = background(windowX, windowY);
            }
            engine.AddRender(background);

            for(int y = 0; y < parts.length; y++){
                engine.AddRender(parts[y]);
            }

            timer += engine.DeltaTime();

            engine.AddRender(fade(timer, 6, windowX, windowY));
            engine.PushRender();
        }
    }

    public void MainMenuLoop(){
        FramePart background = null;
        int windowX = 0;
        int windowY = 0;
        while(engine.isRunning){
            if(windowX != engine.GetX() || windowY != engine.GetY()){
                windowX = engine.GetX();
                windowY = engine.GetY();
                background = background(windowX, windowY);
            }
            engine.AddRender(background);

            engine.AddRender(new FramePart(title, windowX/50, windowY/9, Color.white, mainFont, false));

            try {
                engine.uiManager.CreateButton("Play", new FloatArea((windowX/50), (windowY/4), 140, 50), this, Game.class.getDeclaredMethod("StartPlay"));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return;
            }

            engine.uiManager.Update();
            engine.uiManager.AddRender();

            engine.AddRender(mouse(engine.inputManager.GetMouse(), cursorSize, cursorSize));
            engine.PushRender();

            if(playing){
                Fade();
                Play();
            }
        }
    }

    public FramePart loadingBackground(int width, int height){
        int[] data = new int[width*height*4];
        for(int i = 0; i < data.length; i+=4){
            int x = (i/4)%width;
            Color c = GetGradientLoading(x/width);
            data[i] = c.getRed();
            data[i+1] = c.getGreen();
            data[i+2] = c.getBlue();
            data[i+3] = 255;
        }
        return new FramePart(0, 0, width, height, data);
    }
    public FramePart background(int width, int height){
        BufferedImage bg = engine.assetManager.Get(1).GetBufferedImage(width, height);
        int[] data = new int[width*height*4];
        for(int i = 0; i < data.length; i+=4){
            int x = (i/4)%width;
            int y = (i/4)/width;
            Color c = new Color(bg.getRGB(x, y), true);
            data[i] = c.getRed();
            data[i+1] = c.getGreen();
            data[i+2] = c.getBlue();
            data[i+3] = 255;
        }
        return new FramePart(0, 0, width, height, data);
    }
    public Color GetGradientLoading(double x){
        double red = mainColor.getRed() + x * (loadingGradient.getRed() - mainColor.getRed());
        double green = mainColor.getGreen() + x * (loadingGradient.getGreen() - mainColor.getGreen());
        double blue = mainColor.getBlue() + x * (loadingGradient.getBlue() - mainColor.getBlue());
        //System.out.println(red + ":" + green + ":" + blue);
        return new Color((int)red, (int)green, (int)blue, 255);
    }
    public FramePart mouse(Point p, int width, int height){
        return engine.assetManager.CreateFramePart(p.x, p.y, width, height, engine.assetManager.Get(0).GetBufferedImage(width, height));
    }
    public FramePart fade(double current, int max, int width, int height){
        int[] data = new int[width*height*4];
        for(int i = 0; i < data.length; i+=4){
            data[i] = 255;
            data[i+1] = 255;
            data[i+2] = 255;
            data[i+3] = (int) (current/max) * 255;
        }
        return new FramePart(0, 0, width, height, data);
    }
}