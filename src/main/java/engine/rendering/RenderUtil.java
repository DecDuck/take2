package engine.rendering;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.Font;

import engine.rendering.RenderUtil;
import engine.rendering.classes.FramePart;
import engine.rendering.classes.Pixel;

public class RenderUtil {
    public static FramePart circle(int x, int y, int radius){
        List<Pixel> results = new ArrayList<Pixel>();
        int numberOfPoints = radius*3;
        for (int i = 0; i < numberOfPoints; ++i)
        {
            double angle = Math.toRadians(((double) i / numberOfPoints) * 360d);

            results.add(new Pixel(x + (int)(Math.cos(angle) * radius), y + (int)(Math.sin(angle) * radius), Color.white));
        }
        return new FramePart(results);
    }
    public static FramePart bigText(int width, int height, String text){
        
        return new FramePart(text, width/2, height/2, Color.white, new Font("TimesRoman", Font.PLAIN, 100), true);
    }
    public static FramePart framerate(double deltaTime){
        return new FramePart(""+(int)(1/deltaTime), 10, 25, Color.green,new Font("TimesRoman", Font.PLAIN, 15), false);
    }
}
