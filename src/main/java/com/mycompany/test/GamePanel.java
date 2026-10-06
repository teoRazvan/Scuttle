
package com.mycompany.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 *
 * @author Andrei
 */
public class GamePanel extends JPanel implements Runnable{
    
    final int FPS = 60;
    
    final int originalTileSize = 16; // 16x16 pixels tile
    final int scale = 3;
    
    public final int tileSize = originalTileSize * scale; //64x64 pixels tile
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    
    public final int screenWidth = maxScreenCol * tileSize; // 768 pixels
    public final int screenHeight = maxScreenRow * tileSize; // 576 pixels
    
    
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this, keyH);
    
    
    //Player position
    int playerX = screenWidth / 2;
    int playerY = screenHeight / 2;
    final int playerSpeed = 10;
    
    public GamePanel()
    {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }
    
    public void startGame()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        
        double drawInterval = 1000000000 / FPS;
        double nextDrawInterval = System.nanoTime() + drawInterval;
        
        
        while(gameThread != null)
        {
            
            long currentTime = System.nanoTime();
            //System.out.println("currentTime " + currentTime);
            
            update();
            
            repaint();
            
            try {
                double remainingTime = nextDrawInterval - System.nanoTime();
                remainingTime /= 1000000;
                
                if(remainingTime < 0)
                    remainingTime = 0;
                
                Thread.sleep((long)remainingTime);
                
                
                nextDrawInterval += drawInterval; 
                
                
                //System.out.println("The game is running");
            } catch (InterruptedException ex) {
                System.getLogger(GamePanel.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
        
    
    }
    
    public void update()
    {
        player.update();
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D)g;
        
        player.draw(g2);
        
        g2.dispose();
    }
}
