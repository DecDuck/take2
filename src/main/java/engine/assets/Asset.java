package engine.assets;

import java.awt.Color;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.awt.Graphics;

import engine.rendering.classes.FramePart;
import engine.rendering.classes.Pixel;

public class Asset {

    public String fileLocation;
    private BufferedImage cachedImage;
    private FramePart cachedFramePart;
    public int x,y;

    public void UpdateCacheImage(){
        File assetFile = new File(fileLocation);
        try {
            cachedImage = ImageIO.read(assetFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void UpdateCacheFramePart(int width, int height, boolean updateImageCache){
        if(updateImageCache){
            UpdateCacheImage();
        }
        List<Pixel> results = new ArrayList<Pixel>();
        for(int xi = 0; xi < width; xi++){
            for(int yi = 0; yi < height; yi++){
                results.add(new Pixel(xi, yi, new Color(cachedImage.getRGB(xi, yi), true)));
            }
        }
        cachedFramePart = new FramePart(results);
    }

    public BufferedImage GetBufferedImage(){
        return cachedImage;
    }

    public BufferedImage GetBufferedImage(int width, int height){
        Image tmp = cachedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public FramePart GetFramePart(){
        return cachedFramePart;
    }
}
