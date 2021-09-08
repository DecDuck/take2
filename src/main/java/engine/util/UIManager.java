package engine.util;

import engine.GameEngine;
import engine.rendering.classes.FramePart;
import engine.rendering.classes.Pixel;

import java.util.*;
import java.util.function.Function;

import javax.swing.Action;
import java.lang.reflect.*;
import java.awt.Color;
import java.awt.Point;
import java.awt.Font;

public class UIManager {
    public GameEngine engine;
    public List<UIComponent> uiComponents = new ArrayList<>();

    private class UIComponent{
        public enum UIType{
            Button,
            Text,
        }

        public UIType uType;
        public FloatArea area;
        public String text;
        public List<Method> onClick;

        public UIComponent(String text, FloatArea area){
            uType = UIType.Text;
            this.text = text;
            this.area = area;
        }

        public UIComponent(String text, FloatArea area, Method... functions){
            uType = UIType.Button;
            this.text = text;
            this.area = area;
            onClick = new ArrayList<Method>();
            for(int i = 0; i < functions.length; i++){
                onClick.add(functions[i]);
            }
        }

        public void Update(){
            if(uType == UIType.Button){
                Point p = engine.inputManager.GetMouse();
                if(p.x > area.x &&
                    p.y > area.y &&
                        p.x < area.x+area.w &&
                            p.y < area.y+area.h &&
                                engine.inputManager.IsMouseDown()){
                                    for(Method f : onClick){
                                        try {
                                            f.invoke(engine.uiManager);
                                        } catch (IllegalAccessException | IllegalArgumentException
                                                | InvocationTargetException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    }
                            }
            }
        }
    }

    public UIManager(GameEngine engine){
        this.engine = engine;
    }

    public void CreateButton(String name, FloatArea area, Method... functions){
        UIComponent button = new UIComponent(name, area, functions);
        uiComponents.add(button);
    }

    public void Update(){
        for(UIComponent c : uiComponents){
            c.Update();
        }
    }

    public void AddRender(){
        for(UIComponent c : uiComponents){
            List<Pixel> result = new ArrayList<Pixel>();
            for(int x = 0; x < c.area.w; x++){
                for(int y = 0; y < c.area.h; y++){
                    result.add(new Pixel((int)c.area.x + x, (int)c.area.y + y, Color.white));
                }
            }
            engine.AddRender(new FramePart(result));
            engine.AddRender(new FramePart(c.text, (int)(c.area.w/2 + c.area.x), (int)(c.area.h/2 + c.area.y), Color.black, new Font("TimesRoman", Font.PLAIN, 15), true));
        }
    }

    public void TestButton(){
        System.out.println("Button Works!");
    }
}
