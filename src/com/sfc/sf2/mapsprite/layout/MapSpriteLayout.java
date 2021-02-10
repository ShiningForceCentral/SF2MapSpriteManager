/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.mapsprite.layout;

import com.sfc.sf2.graphics.Tile;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import javax.swing.JPanel;

/**
 *
 * @author wiz
 */
public class MapSpriteLayout extends JPanel {
    
    private static final int DEFAULT_TILES_PER_ROW = 6;
    
    private int tilesPerRow = DEFAULT_TILES_PER_ROW;
    private Tile[] tiles;
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);   
        g.drawImage(buildImage(), 0, 0, this);       
    }
    
    public BufferedImage buildImage(){
        BufferedImage image = buildImage(this.tiles,this.tilesPerRow);
        setSize(image.getWidth(), image.getHeight());
        return image;
    }
    
    public static BufferedImage buildImage(Tile[] tiles, int tilesPerRow){
        int imageHeight = (tiles.length/tilesPerRow)*8;
        if(tiles.length%tilesPerRow!=0){
            imageHeight+=8;
        }
        IndexColorModel icm = buildIndexColorModel(tiles[0].getPalette());
        BufferedImage image = new BufferedImage(tilesPerRow*8, imageHeight , BufferedImage.TYPE_BYTE_BINARY, icm);
        Graphics graphics = image.getGraphics();
        int layoutX = 0;
        int layoutY = 0;
        int mapSpriteIndex = 0;
        for(int i=0;i<tiles.length;i++){         
            /*
                1  4  7 10 13 16
                2  5  8 11 14 17    
                3  6  9 12 15 18
            */
            graphics.drawImage(tiles[i].getImage(), layoutX*8, (mapSpriteIndex*3*8)+layoutY*8, null);
            if(layoutY<2){
                layoutY++;
            }else{
                if(layoutX<5){
                    layoutX++;
                    layoutY = 0;
                }else{
                    layoutX = 0;
                    layoutY = 0;
                    mapSpriteIndex++;
                }
            }
        }
        return image;
    }
    
    private static IndexColorModel buildIndexColorModel(Color[] colors){
        byte[] reds = new byte[16];
        byte[] greens = new byte[16];
        byte[] blues = new byte[16];
        byte[] alphas = new byte[16];
        /*reds[0] = (byte)0xFF;
        greens[0] = (byte)0xFF;
        blues[0] = (byte)0xFF;
        alphas[0] = 0;*/
        for(int i=0;i<16;i++){
            reds[i] = (byte)colors[i].getRed();
            greens[i] = (byte)colors[i].getGreen();
            blues[i] = (byte)colors[i].getBlue();
            alphas[i] = (byte)0xFF;
        }
        alphas[0] = 0;
        IndexColorModel icm = new IndexColorModel(4,16,reds,greens,blues,alphas);
        return icm;
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), getHeight());
    }
    
        public Tile[] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[] tiles) {
        this.tiles = tiles;
    }
    
    public int getTilesPerRow() {
        return tilesPerRow;
    }

    public void setTilesPerRow(int tilesPerRow) {
        this.tilesPerRow = tilesPerRow;
    }
    
}
