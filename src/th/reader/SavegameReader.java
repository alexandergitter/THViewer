/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
 */

package th.reader;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;
import org.apache.commons.io.EndianUtils;
import th.data.ObjectInfo;
import th.data.THFrame;
import th.data.THMap;
import th.data.THPalette;
import th.data.Tile;

public class SavegameReader {

    public static THMap read(FileInputStream frameStream, FileInputStream frameListStream, FileInputStream frameElementStream, File frameTabFile, File frameChunksFile, THPalette framePalette, FileInputStream saveStream, FileInputStream blockTabStream, FileInputStream blockChunkStream, THPalette blockPalette, Color background) throws IOException {
        Vector<BufferedImage> tiles = ChunksReader.readAll(blockChunkStream, blockTabStream, blockPalette, background);
        THMap result = new THMap();
        
        saveStream.getChannel().position(13);
        
        for(int y = 0; y < 128; ++y) {
            for(int x = 0; x < 128; ++x) {
                int pos = (int)saveStream.getChannel().position() + 21;
                int anim = EndianUtils.readSwappedShort(saveStream);
                
                int layer0id = saveStream.read();
                int layer1id = saveStream.read();
                int layer2id = saveStream.read();
                
                layer0id = LevelReader.tileMap[layer0id];
                layer1id = LevelReader.tileMap[layer1id];
                layer2id = LevelReader.tileMap[layer2id];
                
                BufferedImage l0 = tiles.elementAt(layer0id);
                BufferedImage l1 = null;
                BufferedImage l2 = null;
                if(layer1id > 0) {
                    l1 = tiles.elementAt(layer1id);
                }
                
                if(layer2id > 0) {
                    l2 = tiles.elementAt(layer2id);
                }
                
                saveStream.skip(1);
                int extra = saveStream.read();
                saveStream.skip(1);
                
                Tile tile = new Tile(anim, l0, l1, l2, pos);
                
                switch(extra >>> 4) {
                    case 2:
                    case 10:
                        tile.setShadow1(tiles.get(154));
                        break;
                    case 3:
                    case 11:
                        tile.setShadow0(tiles.get(74));
                        tile.setShadow1(tiles.get(154));
                        break;
                    case 6:
                    case 7:
                    case 14:
                    case 15:
                        tile.setShadow0(tiles.get(73));
                        tile.setShadow1(tiles.get(154));
                        break;
                    case 1:
                    case 9:
                        tile.setShadow0(tiles.get(74));
                        break;
                    case 4:
                    case 5:
                    case 12:
                    case 13:
                        tile.setShadow0(tiles.get(73));
                        break;
                    
                }
                
                switch(extra & 0xf) {
                    case 1:
                        tile.setLitter(FramesReader.readByIndex(3737, frameStream, frameListStream, frameElementStream, frameTabFile, frameChunksFile, framePalette));
                        break;
                    case 2:
                        tile.setLitter(FramesReader.readByIndex(3738, frameStream, frameListStream, frameElementStream, frameTabFile, frameChunksFile, framePalette));
                        break;
                    case 3:
                        tile.setLitter(FramesReader.readByIndex(3739, frameStream, frameListStream, frameElementStream, frameTabFile, frameChunksFile, framePalette));
                        break;
                    case 4:
                        tile.setLitter(FramesReader.readByIndex(4533, frameStream, frameListStream, frameElementStream, frameTabFile, frameChunksFile, framePalette));
                        break;
                    case 5:
                        tile.setLitter(FramesReader.readByIndex(3741, frameStream, frameListStream, frameElementStream, frameTabFile, frameChunksFile, framePalette));
                        break;
                    case 6:
                        tile.setLitter(FramesReader.readByIndex(4542, frameStream, frameListStream, frameElementStream, frameTabFile, frameChunksFile, framePalette));
                        break;
                    case 7:
                        tile.setLitter(FramesReader.readByIndex(3742, frameStream, frameListStream, frameElementStream, frameTabFile, frameChunksFile, framePalette));
                        break;
                }
                
                long nextPos = saveStream.getChannel().position();
                                
                readObjectInfo(saveStream, tile, frameStream, frameListStream, frameElementStream, frameTabFile, frameChunksFile, framePalette);
                result.setTile(x, y, tile);
                
                saveStream.getChannel().position(nextPos);
            }
        }
        
        /*for(int y = 0; y < 128; ++y) {
            for(int x = 0; x < 128; ++x) {
                int pid = EndianUtils.readSwappedShort(mapStream);
                
                result.getTile(x, y).setParcel(pid);
            }
        }*/
        
        saveStream.close();
        return result;
    }
    
    private static void readObjectInfo(FileInputStream saveStream, Tile tile, FileInputStream frameStream, FileInputStream listStream, FileInputStream elementStream, File tabFile, File chunksFile, THPalette palette) throws IOException {
        if(tile.getAnim() != 0) {
            int base = 131085 + tile.getAnim() * 175;
            ObjectInfo oi = new ObjectInfo(base);
            
            saveStream.getChannel().position(8 + base);
            int frameIndex = EndianUtils.readSwappedShort(saveStream);
            
            saveStream.skip(6);
            int byte16 = saveStream.read();
            
            saveStream.getChannel().position(base + 29);
            oi.setLayerId(saveStream.read(), 0);
            oi.setLayerId(saveStream.read(), 1);
            oi.setLayerId(saveStream.read(), 2);
            oi.setLayerId(saveStream.read(), 3);
            oi.setLayerId(saveStream.read(), 4);
            oi.setLayerId(saveStream.read(), 5);
            oi.setLayerId(saveStream.read(), 6);
            oi.setLayerId(saveStream.read(), 7);
            oi.setLayerId(saveStream.read(), 8);
            oi.setLayerId(saveStream.read(), 9);
            oi.setLayerId(saveStream.read(), 10);
            oi.setLayerId(saveStream.read(), 11);
            oi.setLayerId(saveStream.read(), 12);
        
            THFrame frame = FramesReader.readByIndex(frameIndex, frameStream, listStream, elementStream, tabFile, chunksFile, palette);
            
            oi.setFrame(frame);
            oi.setByte16(byte16);
            
            tile.setObjectInfo(oi);
        }
    }
}
