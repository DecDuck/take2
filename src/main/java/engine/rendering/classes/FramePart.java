package engine.rendering.classes;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.awt.Font;

public class FramePart {
    public List<Pixel> partList = new ArrayList<Pixel>();

    public boolean isText;
    public RenderText renderText;

    public int[] processedData;
    public int x,y,w,h;

    public FramePart(List<Pixel> pixelArray){
        partList = pixelArray;
        isText = false;
    }

    public FramePart(String text, int f, int g, Color color, Font font, boolean offset){
        isText = true;
        renderText = new RenderText(text, f, g, color, font, offset);
    }

    public FramePart(int x,int y,int w,int h, int[] data){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.processedData = data;
    }

    public int[] getPartDimensions(){
        
        int rightMost = 0;
        for(Pixel p : partList){
            if(p.x > rightMost){
                rightMost = p.x;
            }
        }
        int leftMost = rightMost;
        for(Pixel p : partList){
            if(p.x < leftMost){
                leftMost = p.x;
            }
        }
        int upMost = 0;
        for(Pixel p : partList){
            if(p.y > upMost){
                upMost = p.y;
            }
        }
        int downMost = upMost;
        for(Pixel p : partList){
            if(p.y < downMost){
                downMost = p.y;
            }
        }
        int[] results = new int[4];
        results[0] = leftMost;
        results[1] = downMost;
        results[2] = rightMost - leftMost;
        results[3] = upMost - downMost;
        return results;
    }
}