package engine.rendering;

import engine.GameEngine;

public class AsyncRenderController extends Thread {
    public GameEngine renderer;
    public int frameRate;

    public AsyncRenderController(GameEngine renderer, int frameRate) {
        this.renderer = renderer;
        this.frameRate = frameRate;
    }

    public void run() {
        while(renderer.isRunning){
            try {
                renderer.Render();

                if((0.01/renderer.DeltaTime()) > frameRate){
                    Thread.sleep((int)(1000 / frameRate));
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Ignoring error and continuing rendering. Hopefully it's ok :)");
            }
        }
    }
}