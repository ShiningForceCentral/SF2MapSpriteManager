/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.mapsprite.io;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.compressed.BasicGraphicsDecoder;
import com.sfc.sf2.graphics.compressed.BasicGraphicsEncoder;
import com.sfc.sf2.mapsprite.MapSprite;
import java.awt.Color;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wiz
 */
public class SFCDBankManager {
    
    public static MapSprite[] importSFCDBank(String bankpath, Color[] palette, String fileLoadingOffset, String pointerTableOffset, String numberOfEntries){
        System.out.println("com.sfc.sf2.mapsprite.io.SFCDBankManager.importSFCDBank() - Importing SFCD Bank ...");
        List<MapSprite> mapSprites = new ArrayList();
        try{
            Path path = Paths.get(bankpath);
            int entryNumber = Integer.parseInt(numberOfEntries);
            byte[] fileData = Files.readAllBytes(path);
            int loadingOffset = Integer.parseInt(fileLoadingOffset, 16);
            int ptOffset = 0;
            if(!pointerTableOffset.isBlank()){
                ptOffset = Integer.parseInt(pointerTableOffset, 16);
            }else{
                ptOffset = ((fileData[0]&0xFF)<<24) + ((fileData[1]&0xFF)<<16) + ((fileData[2]&0xFF)<<8) + ((fileData[3]&0xFF)) - loadingOffset;
            }
            for(int i=0;i<entryNumber;i++){
                int pointerByte1 = fileData[ptOffset+i*4+0]&0xFF;
                int pointerByte2 = fileData[ptOffset+i*4+1]&0xFF;
                int pointerByte3 = fileData[ptOffset+i*4+2]&0xFF;
                int pointerByte4 = fileData[ptOffset+i*4+3]&0xFF;
                long pointer = (pointerByte1<<24) + (pointerByte2<<16) + (pointerByte3<<8) + (pointerByte4) - loadingOffset;
                byte[] data = new byte[0x1000];
                System.arraycopy(fileData, (int)pointer, data, 0, 0x1000);
                Tile[] tiles = BasicGraphicsDecoder.decodeBasicGraphics(data, palette);
                if(tiles!=null){
                    if(tiles.length==18){
                       MapSprite mapSprite = new MapSprite();
                       mapSprite.setIndex(i);                   
                       mapSprite.setTiles(tiles);
                       mapSprites.add(mapSprite);
                       System.out.println("Created MapSprite " + i + " with " + tiles.length + " tiles.");                       
                    }else{
                        System.out.println("Could not create MapSprite " + i + " because of wrong length : tiles=" + tiles.length);
                    }
                }
            }
        }catch(Exception e){
             System.err.println("com.sfc.sf2.mapsprite.io.SFCDBankManager.importSFCDBank() - Error while parsing graphics data : "+e);
        }         
                
        System.out.println("com.sfc.sf2.mapsprite.io.SFCDBankManager.importSFCDBank() - SFCD Bank imported.");
        return mapSprites.toArray(new MapSprite[mapSprites.size()]);
    }
    
 
    
}
