package engine.rendering.classes;

import java.awt.Color;
import java.awt.Font;

public class RenderText {
    public String s;
    public int x,y;
    public Color color;
    public Font font;
    public boolean offset;

    public RenderText(String s, int x, int y, Color color, Font font, boolean offset){
        this.s = s;
        this.x = x;
        this.y = y;
        this.color = color;
        this.font = font;
        this.offset = offset;
    }
}
