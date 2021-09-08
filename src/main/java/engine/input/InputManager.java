package engine.input;

import engine.GameEngine;
import java.awt.Point;
import java.awt.event.*;
import java.util.*;

public class InputManager {
    public GameEngine engine;
    private Point lastMousePos = new Point(0, 0);
    boolean isMouseDown = false;

    private Set<Integer> keysDown = new HashSet<>();

    public InputManager(GameEngine engine) {
        this.engine = engine;
        KeyListener keyListener = new KeyListener() {
            public void keyPressed(KeyEvent e) {
                keysDown.add(e.getKeyCode());
            }

            public void keyReleased(KeyEvent e) {
                keysDown.remove(e.getKeyCode());
            }

            @Override
            public void keyTyped(KeyEvent arg0) {}
        };
        MouseListener mouseListener = new MouseListener(){

            @Override
            public void mouseClicked(MouseEvent arg0) {}

            @Override
            public void mouseEntered(MouseEvent arg0) {}

            @Override
            public void mouseExited(MouseEvent arg0) {}

            @Override
            public void mousePressed(MouseEvent arg0) {
                isMouseDown = true;
            }

            @Override
            public void mouseReleased(MouseEvent arg0) {
                isMouseDown = false;
            }
            
        };
        engine.renderer.frame.addKeyListener(keyListener);
        engine.renderer.frame.addMouseListener(mouseListener);
    }

    public Point GetMouse() {
        Point p = engine.renderer.frame.getMousePosition();
        if (p != null) {
            lastMousePos = p;
            return p;
        } else {
            return lastMousePos;
        }
    }

    public boolean IsMouseDown(){
        boolean m = isMouseDown;
        isMouseDown = false;
        return m;
    }

    public boolean GetKeyDown(char key) {
        return keysDown.contains((int)key);
    }
}
