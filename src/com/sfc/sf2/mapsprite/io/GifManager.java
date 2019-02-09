/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.mapsprite.io;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.layout.DefaultLayout;
import com.sfc.sf2.mapsprite.MapSprite;
import com.sfc.sf2.mapsprite.layout.MapSpriteLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author wiz
 */
public class GifManager {
    
    private static final String BASE_FILENAME = "mapspriteXXX-Y-Z.gif";
    
    public static MapSprite[] importGif(String basepath){
        System.out.println("com.sfc.sf2.mapsprite.io.GifManager.importGif() - Importing GIF files ...");
        List<MapSprite> mapSprites = new ArrayList();
        try{
            for(int i=0;i<1000;i++){
                String index = String.format("%03d", i/3);
                int facing = i%3;
                String filePath0 = basepath + BASE_FILENAME.replace("XXX-Y-Z.gif", index+"-"+facing+"-0.gif");
                String filePath1 = basepath + BASE_FILENAME.replace("XXX-Y-Z.gif", index+"-"+facing+"-1.gif");
                Tile[] tiles0 = loadGifFile(filePath0);
                Tile[] tiles1 = loadGifFile(filePath1);
                if(tiles0!=null && tiles1!=null){
                    if(tiles0.length==9 && tiles1.length==9){
                       MapSprite mapSprite = new MapSprite();
                       mapSprite.setIndex(i);
                       Tile[] tiles = new Tile[tiles0.length + tiles1.length];
                       System.arraycopy(tiles0, 0, tiles, 0, tiles0.length);
                       System.arraycopy(tiles1, 0, tiles, tiles0.length, tiles1.length);                    
                       mapSprite.setTiles(tiles);
                       mapSprites.add(mapSprite);
                       System.out.println("Created MapSprite " + i + " with " + tiles.length + " tiles.");                       
                    }else{
                        System.out.println("Could not create MapSprite " + i + " because of wrong lengths : tiles0=" + tiles0.length + ", tiles1=" + tiles1.length);
                    }
                }
            }
        }catch(Exception e){
             System.err.println("com.sfc.sf2.mapsprite.io.GifManager.importGif() - Error while parsing graphics data : "+e);
        }        
        System.out.println("com.sfc.sf2.mapsprite.io.GifManager.importGif() - GIF files imported.");
        return mapSprites.toArray(new MapSprite[mapSprites.size()]);                
    }
    
    
    
    public static Tile[] loadGifFile(String filepath) throws IOException{
        Tile[] tiles = null;
        try{
            Path path = Paths.get(filepath);
            if(path.toFile().exists()){
                BufferedImage img = ImageIO.read(path.toFile());
                ColorModel cm = img.getColorModel();
                if(!(cm instanceof IndexColorModel)){
                    System.out.println("GIF FORMAT ERROR : COLORS ARE NOT INDEXED AS EXPECTED.");
                }else{
                    IndexColorModel icm = (IndexColorModel) cm;
                    Color[] palette = buildColors(icm);

                    int imageWidth = img.getWidth();
                    int imageHeight = img.getHeight();
                    if(imageWidth%8!=0 || imageHeight%8!=0){
                        System.out.println("GIF FORMAT WARNING : DIMENSIONS ARE NOT MULTIPLES OF 8. (8 pixels per tile)");
                    }else{
                        tiles = new Tile[(imageWidth/8)*(imageHeight/8)];
                        int tileId = 0;
                        for(int x=0;x<imageWidth;x+=8){
                            for(int y=0;y<imageHeight;y+=8){
                                //System.out.println("Building tile from coordinates "+x+":"+y);
                                Tile tile = new Tile();
                                tile.setId(tileId);
                                tile.setPalette(palette);
                                for(int j=0;j<8;j++){
                                    for(int i=0;i<8;i++){
                                        Color color = new Color(img.getRGB(x+i,y+j));
                                        for(int c=0;c<16;c++){
                                            if(color.equals(palette[c])){
                                                tile.setPixel(i, j, c);
                                                break;
                                            }
                                        }
                                    }
                                }
                                //System.out.println(tile);
                                tiles[tileId] = tile;   
                                tileId++;
                            }
                        }
                    }
                }                
            }
        }catch(Exception e){
             System.err.println("com.sfc.sf2.mapsprite.io.GifManager.importGif() - Error while parsing GIF data : "+e);
             throw e;
        }                
        return tiles;                
    }
    
    private static Color[] buildColors(IndexColorModel icm){
        Color[] colors = new Color[16];
        if(icm.getMapSize()>16){
            System.out.println("com.sfc.sf2.mapsprite.io.GifManager.buildColors() - GIF FORMAT HAS MORE THAN 16 INDEXED COLORS : "+icm.getMapSize());
        }
        byte[] reds = new byte[icm.getMapSize()];
        byte[] greens = new byte[icm.getMapSize()];
        byte[] blues = new byte[icm.getMapSize()];
        icm.getReds(reds);
        icm.getGreens(greens);
        icm.getBlues(blues);
        for(int i=0;i<16;i++){
            colors[i] = new Color((int)(reds[i]&0xff),(int)(greens[i]&0xff),(int)(blues[i]&0xff));
        }
        return colors;
    }
    
    
    public static void exportGif(MapSprite[] mapSprites, String basepath){
        try {
            //System.out.println("com.sfc.sf2.mapsprite.io.GifManager.exportGif() - Exporting GIF files ...");
            for(MapSprite mapSprite : mapSprites){
                String index = String.format("%03d", mapSprite.getIndex()/3);
                int facing = mapSprite.getIndex()%3;
                String filePath0 = basepath + System.getProperty("file.separator") + BASE_FILENAME.replace("XXX-Y-Z.gif", index+"-"+facing+"-0.gif");
                writeGifFile(Arrays.copyOfRange(mapSprite.getTiles(), 0, 9),filePath0);
                String filePath1 = basepath + System.getProperty("file.separator") + BASE_FILENAME.replace("XXX-Y-Z.gif", index+"-"+facing+"-1.gif");
                writeGifFile(Arrays.copyOfRange(mapSprite.getTiles(), 9, 18),filePath1);                
            }
            //System.out.println("com.sfc.sf2.mapsprite.io.GifManager.exportGif() - GIF files exported.");
        } catch (Exception ex) {
            Logger.getLogger(GifManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
                
    }    
    
    public static void writeGifFile(Tile[] tiles, String filepath){
        try {
            //System.out.println("com.sfc.sf2.mapsprite.io.GifManager.exportGif() - Exporting GIF file ...");
            BufferedImage image = MapSpriteLayout.buildImage(tiles, 3);
            File outputfile = new File(filepath);
            //System.out.println("File path : "+outputfile.getAbsolutePath());
            ImageIO.write(image, "gif", outputfile);
            System.out.println("GIF file exported : " + outputfile.getAbsolutePath());
        } catch (Exception ex) {
            Logger.getLogger(GifManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
                
    }
    
}
