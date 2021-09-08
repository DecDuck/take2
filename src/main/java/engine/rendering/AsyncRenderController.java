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
        renderer.Render();
        try {
            Thread.sleep((int)(1000 / frameRate));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(renderer.isRunning){
            new AsyncRenderController(renderer, frameRate).start();
        }
    }
}