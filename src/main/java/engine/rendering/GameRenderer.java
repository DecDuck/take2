package engine.rendering;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.text.StyleConstants.FontConstants;

import engine.GameEngine;
import engine.rendering.classes.FramePart;
import engine.rendering.classes.Pixel;
import engine.rendering.classes.RenderText;

public class GameRenderer {
    public JFrame frame;

    public List<FramePart> toPush;
    public List<List<FramePart>> buffer = new ArrayList<List<FramePart>>();

    public BufferedImage image;

    public GameEngine engine;

    private GraphicsConfiguration config =
			GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice()
				.getDefaultConfiguration();

    public GameRenderer() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame = new JFrame();
        frame.setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
        frame.setVisible(true);
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                engine.isRunning = false;
            }

            @Override
            public void windowClosed(WindowEvent e) {
                engine.isRunning = false;
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });
    }

    public void AddRender(FramePart toAdd) {
        // System.out.println("addrender");
        if (toPush == null) {
            toPush = new ArrayList<FramePart>();
        }
        toPush.add(toAdd);
    }

    public void PushRender() {
        buffer.clear();
        buffer.add(toPush);
        toPush = null;
    }

    public int getWidth() {
        return frame.getWidth();
    }

    public int getHeight() {
        return frame.getHeight();
    }

    public final BufferedImage create(final int width, final int height,
			final boolean alpha) {
		return config.createCompatibleImage(width, height, alpha
				? Transparency.TRANSLUCENT : Transparency.OPAQUE);
	}

    public void Render() {
        List<List<FramePart>> cachedBuffer = new ArrayList<List<FramePart>>(buffer);
        if (cachedBuffer.size() <= 0) {
            return;
        }
        List<FramePart> toDraw = cachedBuffer.get(0);
        if (toDraw == null) {
            return;
        }
        if (toDraw.size() <= 0) {
            return;
        }

        BufferStrategy bs = frame.getBufferStrategy();

        image = create(frame.getWidth(), frame.getHeight(), true);

        Graphics2D g = (Graphics2D) image.getGraphics();
        Graphics2D graphics = (Graphics2D) bs.getDrawGraphics();
        WritableRaster raster = image.getRaster();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        for (FramePart part : toDraw) {
            if (!part.isText) {
                try {
                    if (part.processedData != null) {
                        int[] data = part.processedData;
                        raster.setPixels(part.x, part.y, part.w, part.h, data);
                    } else {
                        for (Pixel p : part.partList) {
                            if(p.color.getAlpha() > 0){
                                float[] pixels = new float[] { p.color.getRed(), p.color.getGreen(), p.color.getBlue(),
                                    p.color.getAlpha() };
                                raster.setPixel(p.x, p.y, pixels);
                            }
                            
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    //e.printStackTrace();
                }

            } else {
                g.setFont(part.renderText.font);
                g.setColor(part.renderText.color);
                int x = part.renderText.x;
                int y = part.renderText.y;
                if (part.renderText.offset) {
                    FontMetrics fm = g.getFontMetrics(part.renderText.font);
                    x -= fm.stringWidth(part.renderText.s) / 2;
                }
                g.drawString(part.renderText.s, x, y);
            }
        }

        graphics.drawImage(image, 0, 0, frame.getWidth(), frame.getHeight(), null);
        graphics.dispose();
        bs.show();
        Toolkit.getDefaultToolkit().sync();
    }
}