package engine.assets;

import java.util.Dictionary;
import java.util.Hashtable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

public class SaveFileManager {
    private String fileLocation;
    private SaveableObject object;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private class SaveableObject{
        public Dictionary<String, Object> saved = new Hashtable<String,Object>();
    }

    public SaveFileManager(){
        object = new SaveableObject();
    }

    public void SetFileLocation(String location){
        fileLocation = location;
    }

    public void Load(){
        if(fileLocation == null || fileLocation == ""){
            return;
        }
        File f = new File(fileLocation);
        if(!f.exists()){
            return;
        }
        String contents;
        try {
            contents = Files.readString(Path.of(f.getAbsolutePath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        object = gson.fromJson(contents, SaveableObject.class);
    }

    public void Save(){
        if(fileLocation == null || fileLocation == ""){
            return;
        }
        File f = new File(fileLocation);
        if(!f.exists()){
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        try {
            Files.writeString(Path.of(f.getAbsolutePath()), gson.toJson(object), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    public Object Get(String key){
        return object.saved.get(key);
    }

    public void Save(String key, Object obj){
        object.saved.put(key, obj);
    }
}
