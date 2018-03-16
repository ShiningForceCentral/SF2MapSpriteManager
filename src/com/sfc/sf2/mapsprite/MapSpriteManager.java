/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.mapsprite;

import com.sfc.sf2.graphics.GraphicsManager;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.mapsprite.io.DisassemblyManager;
import com.sfc.sf2.mapsprite.io.PngManager;
import com.sfc.sf2.mapsprite.io.GifManager;
import com.sfc.sf2.palette.PaletteManager;
import java.awt.Color;

/**
 *
 * @author wiz
 */
public class MapSpriteManager {
       
    private PaletteManager paletteManager = new PaletteManager();
    private GraphicsManager graphicsManager = new GraphicsManager();
    private Tile[] tiles;
    private MapSprite[] mapSprites;

    public Tile[] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[] tiles) {
        this.tiles = tiles;
    }
       
    public void importDisassembly(String paletteFilePath, String graphicsBasepath){
        System.out.println("com.sfc.sf2.mapsprite.MapSpriteManager.importDisassembly() - Importing disassembly ...");
        paletteManager.importDisassembly(paletteFilePath);
        Color[] palette = paletteManager.getPalette();
        palette[0] = new Color(255, 255, 255, 0);
        mapSprites = DisassemblyManager.importDisassembly(graphicsBasepath, palette);
        tiles = new Tile[mapSprites.length*18];
        for(int i=0;i<mapSprites.length;i++){
            System.arraycopy(mapSprites[i].getTiles(), 0, tiles, i*18, 18);
        }
        graphicsManager.setTiles(tiles);
        System.out.println("com.sfc.sf2.mapsprite.MapSpriteManager.importDisassembly() - Disassembly imported.");
    }
    
    public void exportDisassembly(String basepath){
        System.out.println("com.sfc.sf2.mapsprite.MapSpriteManager.importDisassembly() - Exporting disassembly ...");
        DisassemblyManager.exportDisassembly(mapSprites, basepath);
        System.out.println("com.sfc.sf2.mapsprite.MapSpriteManager.importDisassembly() - Disassembly exported.");        
    }   
    
    public void importRom(String romFilePath, String paletteOffset, String paletteLength, String graphicsOffset, String graphicsLength){
        System.out.println("com.sfc.sf2.mapsprite.MapSpriteManager.importOriginalRom() - Importing original ROM ...");
        graphicsManager.importRom(romFilePath, paletteOffset, paletteLength, graphicsOffset, graphicsLength,GraphicsManager.COMPRESSION_BASIC);
        tiles = graphicsManager.getTiles();
        System.out.println("com.sfc.sf2.mapsprite.MapSpriteManager.importOriginalRom() - Original ROM imported.");
    }
    
    public void exportRom(String originalRomFilePath, String graphicsOffset){
        System.out.println("com.sfc.sf2.mapsprite.MapSpriteManager.exportOriginalRom() - Exporting original ROM ...");
        graphicsManager.exportRom(originalRomFilePath, graphicsOffset, GraphicsManager.COMPRESSION_BASIC);
        System.out.println("com.sfc.sf2.mapsprite.MapSpriteManager.exportOriginalRom() - Original ROM exported.");        
    }      
    
    public void importPng(String basepath){
        System.out.println("com.sfc.sf2.mapsprite.MapSpriteManager.importPng() - Importing PNG ...");
        mapSprites = PngManager.importPng(basepath);
        tiles = new Tile[mapSprites.length*18];
        for(int i=0;i<mapSprites.length;i++){
            System.arraycopy(mapSprites[i].getTiles(), 0, tiles, i*18, 18);
        }
        graphicsManager.setTiles(tiles);
        System.out.println("com.sfc.sf2.mapsprite.MapSpriteManager.importPng() - PNG imported.");
    }
    
    public void exportPng(String basepath){
        System.out.println("com.sfc.sf2.mapsprite.MapSpriteManager.exportPng() - Exporting PNG ...");
        PngManager.exportPng(mapSprites, basepath);
        System.out.println("com.sfc.sf2.mapsprite.MapSpriteManager.exportPng() - PNG exported.");       
    }
    
    public void importGif(String basepath){
        System.out.println("com.sfc.sf2.mapsprite.MapSpriteManager.importGif() - Importing GIF ...");
        mapSprites = GifManager.importGif(basepath);
        tiles = new Tile[mapSprites.length*18];
        for(int i=0;i<mapSprites.length;i++){
            System.arraycopy(mapSprites[i].getTiles(), 0, tiles, i*18, 18);
        }
        graphicsManager.setTiles(tiles);
        System.out.println("com.sfc.sf2.mapsprite.MapSpriteManager.importGif() - GIF imported.");
    }
    
    public void exportGif(String basepath){
        System.out.println("com.sfc.sf2.mapsprite.MapSpriteManager.exportGif() - Exporting GIF ...");
        GifManager.exportGif(mapSprites, basepath);
        System.out.println("com.sfc.sf2.mapsprite.MapSpriteManager.exportGif() - GIF exported.");       
    }
}
