package engine.assets;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import java.awt.image.BufferedImage;

import engine.rendering.classes.FramePart;
import engine.rendering.classes.Pixel;

public class AssetManager {
    private List<Asset> assets = new ArrayList<Asset>();

    public boolean loading = false;

    public class AsyncAssetLoader extends Thread{

        private String[] toLoad;
        private AssetManager manager;

        public AsyncAssetLoader(String[] toLoad, AssetManager manager){
            this.toLoad = toLoad;
            this.manager = manager;
        }

        @Override
        public void run() {
            for(String fileLocation : toLoad){
                manager.Load(fileLocation);
            }
            manager.loading = false;
        }

    }

    public void LoadQueue(String... fileLocations){
        loading = true;
        new AsyncAssetLoader(fileLocations, this).start();
    }

    public int Load(String fileLocation){
        Asset asset = new Asset();
        asset.fileLocation = "assets/" + fileLocation;
        asset.UpdateCacheImage();
        assets.add(asset);

        return assets.size() - 1;
    }

    public FramePart CreateFramePart(int xoffset, int yoffset, int width, int height, BufferedImage reference){
        List<Pixel> results = new ArrayList<Pixel>();
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                Color c = new Color(reference.getRGB(x, y), true);
                if(c.getAlpha() > 0){
                    c = new Color(c.getRed(), c.getGreen(), c.getBlue(), 255);
                }
                results.add(new Pixel(xoffset + x, yoffset + y, c));
            }
        }
        FramePart result = new FramePart(results);
        return result;
    }

    public Asset Get(int index){
        return assets.get(index);
    }
}
