package engine.rendering.classes;

import java.awt.Color;

public class Pixel {
    public int x;
    public int y;
    public Color color;

    public Pixel(int x, int y, Color color){
        this.x = x;
        this.y = y;
        this.color = color;
    }
}