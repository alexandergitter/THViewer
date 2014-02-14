/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package thv.th.data;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.IOException;

import javax.swing.JComponent;

import thv.th.Main;

public class THMap extends JComponent implements MouseListener {
    private int players;
    private Tile[][] tiles;
    private Point mousePos = new Point(0,0);
    
    private static final int tileWidth = 64;
    private static final int tileWidth2 = tileWidth/2;
    
    private static final int tileHeight = 32;
    private static final int tileHeight2 = tileHeight/2;
    
    Rectangle prevVisible = new Rectangle();
    BufferedImage imageBuffer;
    BufferedImage infoBuffer;
    
    private int maxHeight = 0;
    private int maxWidth = 0;
    private final int border = 100;
    private Point mouseTile = new Point();
    
    /** Creates a new instance of Map */
    public THMap() {
        tiles = new Tile[128][128];
        this.setPreferredSize(new Dimension(128 * 64, 128 * 32));
        this.addMouseListener(this);
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }

    public void setTile(int x, int y, Tile tile) {
        tiles[x][y] = tile;
        
        maxHeight = Math.max(maxHeight, tile.getHeight());
        maxWidth = Math.max(maxWidth, tile.getWidth());
    }
    
    protected void paintComponent(Graphics g) {
        Rectangle visible = this.getVisibleRect();
        visible.x += border;
        
        if(!prevVisible.equals(visible)) {
            if(prevVisible.width != visible.width || prevVisible.height != visible.height) {
                imageBuffer = new BufferedImage(visible.width, visible.height,  BufferedImage.TYPE_INT_ARGB);
                infoBuffer = new BufferedImage(border, visible.height,  BufferedImage.TYPE_INT_ARGB);
            }
            
            prevVisible = new Rectangle(visible);
        }
        
        render2(visible);
        //g.clearRect(visible.x, visible.y, visible.width, visible.height);
        //g.clipRect(visible.x+100, visible.y+100, visible.width-200, visible.height-200);
        g.drawImage(imageBuffer, visible.x, visible.y, null);
        g.drawImage(infoBuffer, visible.x-border, visible.y, null);
        
    }
    
    private void render2(Rectangle screen) {
        Graphics2D g = imageBuffer.createGraphics();
        g.clearRect(0, 0, screen.width, screen.height);
        
        Graphics compoG = infoBuffer.getGraphics();
        compoG.setColor(Color.WHITE);
        compoG.fillRect(0, 0, border, screen.height);
        
        // this is for debugging 
        Rectangle myScreen = new Rectangle(screen);
        
        /*myScreen.x += 100;
        myScreen.y += 100;
        myScreen.width -= 200;
        myScreen.height -= 200;*/
        
        Rectangle bufferRect = new Rectangle(0, 0, myScreen.width, myScreen.height);
        Rectangle tileRect = new Rectangle();
        
        Point upperRight = new Point(myScreen.x + myScreen.width, myScreen.y);
        Point tileUpper = calcTilePosition(upperRight.x, upperRight.y);
        tileUpper.x -= 1;
        tileUpper.y -= 1;
        
        //System.out.println(tileUpper);
        
        //Rectangle mouseRect = new Rectangle();
        
        int tx = 0;
        int ty = 0;
        int offset = 0;
        
        while(tileUpper.y < 128) {
            tx = tileUpper.x;
            ty = tileUpper.y;
           
            for(; tx < 128; ++tx) {
                if(tx < 0)
                    continue;
              
                if(ty < 0)
                    break;
                
                tileRect = calcScreenRectangle(tx, ty, screen.x, screen.y, tiles[tx][ty]);
                
                /*if(mouse.x == tx && mouse.y == ty) {
                    mouseRect = new Rectangle(tileRect);
                    mouseOver = true;
                } else {
                    mouseOver = false;
                }*/
                
                if(tileRect.x > bufferRect.getMaxX() || (tileRect.y - maxHeight + tileHeight) > bufferRect.getMaxY())
                    break;
                
                else if(tileRect.getMaxX() < bufferRect.getMinX() || tileRect.getMaxY() < bufferRect.getMinY())
                    continue;
                
                else {
                    if(isMouseInTile(screen, mousePos, tiles[tx][ty], tileRect)) {
                        mouseTile.x = tx;
                        mouseTile.y = ty;
                    }
                    renderTile(g, tiles[tx][ty], tileRect.x, tileRect.y, tx, ty, false, compoG);
                }
            }
            
            --tileUpper.x;
            ++tileUpper.y;
        }

        //Point mouse = calcTilePosition(mousePos.x, mousePos.y);
        if(mouseTile.x >= 0 && mouseTile.x < 128 && mouseTile.y >= 0 && mouseTile.y < 128) {
            StringBuilder sb = new StringBuilder();
            sb.append("level: ");
            sb.append(tiles[mouseTile.x][mouseTile.y].getFilePosition());
            sb.append("   savegame-map: ");
            sb.append(tiles[mouseTile.x][mouseTile.y].getFilePosition() - 21);
            
            if(tiles[mouseTile.x][mouseTile.y].getObjectInfo() != null) {
                sb.append("   object-info: ");
                sb.append(tiles[mouseTile.x][mouseTile.y].getObjectInfo().getPosition());
            }
            
            // TODO
            //Main.getInstance().setTitle(sb.toString());
            
            tileRect = calcScreenRectangle(mouseTile.x, mouseTile.y, screen.x, screen.y, tiles[mouseTile.x][mouseTile.y]);
            renderTile(g, tiles[mouseTile.x][mouseTile.y], tileRect.x, tileRect.y, mouseTile.x, mouseTile.y, true, compoG);
        }
    }
    
    private int renderTile(Graphics2D g, Tile t, int x, int y, int tx, int ty, boolean mouseOver, Graphics compoG) {
        int off = t.getHeight() - t.getLayer0().getHeight(null);
        Composite origComposite = g.getComposite();
        AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
        
        // layer 0
        if(mouseOver) {
            compoG.drawImage(t.getLayer0(), 10, 10, null);
            g.drawImage(RedFilter.createRedImage(t.getLayer0()), x, y + off, null);
        } else {
            g.drawImage(t.getLayer0(), x, y + off, null);
        }
        
        // shadow0
        if(t.getShadow0() != null) {
            off = t.getHeight() - t.getShadow0().getHeight(null);
            g.setComposite(alpha);
            g.drawImage(t.getShadow0(), x, y + off, null);
            g.setComposite(origComposite);
        }
        
        // layer 1
        if(t.getLayer1() != null) {
            off = t.getHeight() - t.getLayer1().getHeight(null);
            
            if(mouseOver) {
                compoG.drawImage(t.getLayer1(), 10, 80, null);
                g.drawImage(RedFilter.createRedImage(t.getLayer1()), x, y + off, null);
            } else {
                g.drawImage(t.getLayer1(), x, y + off, null);
            }
            
            // shadow 1
            if(t.getShadow1() != null) {
                //off = t.getHeight() - t.getShadow1().getHeight(null);
                g.setComposite(alpha);
                g.drawImage(t.getShadow1(), x, y + off, null);
                g.setComposite(origComposite);
            }
        }
        
        // layer 2
        if(t.getLayer2() != null) {
            off = t.getHeight() - t.getLayer2().getHeight(null);
            
            if(mouseOver) {
                compoG.drawImage(t.getLayer2(), 10, 180, null);
                g.drawImage(RedFilter.createRedImage(t.getLayer2()), x, y + off, null);
            } else {
                g.drawImage(t.getLayer2(), x, y + off, null);
            }
        }
        
        // litter
        try {
        if(t.getLitter() != null) {
            off = t.getHeight() - t.getLayer0().getHeight(null);
            t.getLitter().renderInMap(t, g, x - 109, y - 186 + off, false);
        }
        } catch(IOException e) {}
        
        // object
        try {
        if(t.getObjectInfo() != null && t.getObjectInfo().getFrame() != null) {
            ObjectInfo oi = t.getObjectInfo();
            
            off = t.getHeight() - t.getLayer0().getHeight(null);
            
            if(mouseOver) {
            	System.out.println(oi.getPosition());
                oi.getFrame().renderInMap(t, compoG, -100, 200, false);
                oi.getFrame().renderInMap(t, g, x - 109, y - 186 + off, true);
            } else {
                oi.getFrame().renderInMap(t, g, x - 109, y - 186 + off, false);
            }
        }
        } catch(IOException e) {}
        
        return t.getHeight() - t.getLayer0().getHeight(null);
    }
    
    private boolean isMouseInTile(final Rectangle _screen, final Point _mouse, final Tile tile, final Rectangle _tileRect) {
        int off = tile.getHeight() - tile.getLayer0().getHeight(null);
        
        Rectangle screen = new Rectangle(_screen);
        Point mouse = new Point(_mouse);
        Rectangle tileRect = new Rectangle(_tileRect);
        
        tileRect.x += screen.x;
        tileRect.y += off + screen.y;
        tileRect.width = tile.getLayer0().getWidth();
        tileRect.height = tile.getLayer0().getHeight();
        
        if(tileRect.contains(mouse)) {
            mouse.x -= tileRect.x;
            mouse.y -= tileRect.y;
            
            if(tile.getLayer0().getRGB(mouse.x, mouse.y) == 0)
                return false;
            else
                return true;
        }
        
        return false;
    }
    
    private Rectangle calcScreenRectangle(int tx, int ty, int screenx, int screeny, Tile t) {
        Rectangle tileRect = new Rectangle(0,0,0,0);
        
        tileRect.width = t.getWidth();
        tileRect.height = t.getHeight();
                
        tileRect.x = ((tx * tileWidth2) + 4096 - tileWidth2) - (ty * tileWidth2) - screenx;
        tileRect.y = (tx * tileHeight2) + (ty * tileHeight2) - screeny;
        tileRect.y -= (t.getHeight() - tileHeight);
        
        return tileRect;
    }
    
    private Point calcTilePosition(int screenx, int screeny) {
        Point res = new Point();
        
        int temp = screenx + 2*screeny - 4064;
        temp -= temp % 64;
        
        res.x = (temp / 64);
        
        temp = -screenx + 2*screeny + 4064;
        temp -= temp % 64;
        
        res.y = (temp / 64);
        
        return res;
    }
    
    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     */
    public void mouseClicked(MouseEvent e) {
        this.mousePos.x = e.getPoint().x;
        this.mousePos.y = e.getPoint().y;
        this.repaint();
        //System.out.println(mousePos);
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     */
    public void mousePressed(MouseEvent e) {}

    /**
     * Invoked when a mouse button has been released on a component.
     */
    public void mouseReleased(MouseEvent e) {}

    /**
     * Invoked when the mouse enters a component.
     */
    public void mouseEntered(MouseEvent e) {}

    /**
     * Invoked when the mouse exits a component.
     */
    public void mouseExited(MouseEvent e) {}
}

class RedFilter extends RGBImageFilter {
    public static Image createRedImage (Image i) {
	RedFilter filter = new RedFilter();
	ImageProducer prod = new FilteredImageSource(i.getSource(), filter);
	Image grayImage = Toolkit.getDefaultToolkit().createImage(prod);
	return grayImage;
    }

    /**
     * Constructs a GrayFilter object that filters a color image to a 
     * grayscale image. Used by buttons to create disabled ("grayed out")
     * button images.
     *
     * @param b  a boolean -- true if the pixels should be brightened
     * @param p  an int in the range 0..100 that determines the percentage
     *           of gray, where 100 is the darkest gray, and 0 is the lightest
     */
    public RedFilter() {
        canFilterIndexColorModel = true;
    }
    
    /**
     * Overrides <code>RGBImageFilter.filterRGB</code>.
     */
    public int filterRGB(int x, int y, int rgb) {
        int percent = 50;
        // Use NTSC conversion formula.
	int gray = (int)((0.30 * ((rgb >> 16) & 0xff) + 
                         0.59 * ((rgb >> 8) & 0xff) + 
                         0.11 * (rgb & 0xff)) / 3);
	
        gray = (255 - ((255 - gray) * (100 - percent) / 100));
        //gray = (gray * (100 - percent) / 100);
	
        if (gray < 0) gray = 0;
        if (gray > 255) gray = 255;
        
        int r = (rgb & 0x00ff0000) >> 16;
        int g = (rgb & 0x0000ff00) >> 8;
        int b = (rgb & 0x000000ff);
        
        r = (r + 0xff) / 2;
        g = (g + 0x10) / 2;
        b = (b + 0x10) / 2;
        
        if(r > 255) r = 255;
        if(g > 255) g = 255;
        if(b > 255) b = 255;
                
        return (rgb & 0xff000000) | (r << 16) | (g << 8) | b;
    }
}