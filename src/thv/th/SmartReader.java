/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
 */

package thv.th;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JComponent;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import rnc.DeRNC;
import thv.th.data.Sample;
import thv.th.data.THMap;
import thv.th.data.THPalette;
import thv.th.data.THSound;
import thv.th.reader.LangReader;
import thv.th.reader.LevelReader;
import thv.th.reader.PaletteReader;
import thv.th.reader.RawReader;
import thv.th.reader.SoundReader;
import thv.th.view.AnimationPanel;
import thv.th.view.FramePanel;
import thv.th.view.ImagePanel;
import thv.th.view.MapPanel;
import thv.th.view.PalettePanel;
import thv.th.view.RawPanel;
import thv.th.view.SavegamePanel;
import thv.th.view.SoundPanel;
import thv.th.view.SpriteElementPanel;
import thv.th.view.StringsPanel;
import thv.util.ABuffer;
import thv.util.FullFileBuffer;

public class SmartReader
implements ActionListener {
    private File themeHospitalDir = null;
    private Hashtable<String, File> table;
    private MainWindow mainWindow;

    public SmartReader(File thDir, MainWindow mainWindow) {
    	this.mainWindow = mainWindow;
        this.themeHospitalDir = thDir;
        table = FileScanner.scanDirectory(this.themeHospitalDir);
    }

    public JComponent open(File f, File thDir) throws IOException {

        // i think this is not needed as we unpack in openFromString, too
        //dernc(f);
        Attributes as = new FileTypeExtractor().getAttributes(f);

        if (as.getValue("type").equals("chunks")) {
        	dernc(f);
            File tabFile = fileFromString(as.getValue("tab"));
            File paletteFile = fileFromString(as.getValue("palette"));
            return new ImagePanel(paletteFile, f, tabFile);
        } else if (as.getValue("type").equals("extchunks")) {
            File tabFile = fileFromString(as.getValue("tab"));
            File paletteFile = fileFromString(as.getValue("palette"));
            return new ImagePanel(paletteFile, f, tabFile, true);
        } else if (as.getValue("type").equals("spriteelement")) {
            File tabFile = fileFromString(as.getValue("tab"));
            File paletteFile = fileFromString(as.getValue("palette"));
            File chunksFile = fileFromString(as.getValue("chunks"));
            return new SpriteElementPanel(paletteFile, chunksFile, tabFile, f);
        } else if (as.getValue("type").equals("frames")) {
            File tabFile = fileFromString(as.getValue("tab"));
            File paletteFile = fileFromString(as.getValue("palette"));
            File chunksFile = fileFromString(as.getValue("chunks"));
            File spriteElementFile = fileFromString(as
                    .getValue("spriteelement"));
            File listFile = fileFromString(as.getValue("list"));
            return new FramePanel(paletteFile, chunksFile, tabFile,
                    spriteElementFile, f, listFile);
        } else if (as.getValue("type").equals("animstart")) {
            File tabFile = fileFromString(as.getValue("tab"));
            File paletteFile = fileFromString(as.getValue("palette"));
            File chunksFile = fileFromString(as.getValue("chunks"));
            File spriteElementFile = fileFromString(as
                    .getValue("spriteelement"));
            File framesFile = fileFromString(as.getValue("frames"));
            File listFile = fileFromString(as.getValue("list"));
            return new AnimationPanel(paletteFile, chunksFile, tabFile,
                    spriteElementFile, framesFile, listFile, f);
        } else if (as.getValue("type").equals("map")) {
            return createMapPanel(f, as);
        } else if (as.getValue("type").equals("raw")) {
            return createRawPanel(f, as);
        } else if (as.getValue("type").equals("strings")) {
            return createStringsPanel(f);
        } else if (as.getValue("type").equals("palette")) {
            return new PalettePanel(f);
        } else if (as.getValue("type").equals("savegame")) {
            File frameFile = fileFromString(as.getValue("frames"));
            File frameListFile = fileFromString(as.getValue("framelist"));
            File frameElementFile = fileFromString(as.getValue("frameelement"));
            File frameTabFile = fileFromString(as.getValue("frametab"));
            File frameChunksFile = fileFromString(as.getValue("framechunks"));
            File framePaletteFile = fileFromString(as.getValue("framepalette"));
            File saveFile = f;
            File blockTabFile = fileFromString(as.getValue("blocktab"));
            File blockChunksFile = fileFromString(as.getValue("blockchunks"));
            File blockPaletteFile = fileFromString(as.getValue("blockpalette"));

            return new SavegamePanel(frameFile, frameListFile,
                    frameElementFile, frameTabFile, frameChunksFile,
                    framePaletteFile, saveFile, blockTabFile, blockChunksFile,
                    blockPaletteFile);
        } else if (as.getValue("type").equals("sound")) {
            return createSoundPanel(f);
        }

        return null;
    }
    
    private SoundPanel createSoundPanel(File soundsFile)
    throws IOException {
        ABuffer buf = new FullFileBuffer(soundsFile);
    	Vector<Sample> samples = SoundReader.readAll(buf);
        THSound sounds = new THSound(buf);
        for (Sample s : samples) {
            sounds.addSample(s);
        }
    	return new SoundPanel(sounds);
    }
    
    private StringsPanel createStringsPanel(File stringsFile)
    throws IOException {
    	dernc(stringsFile);
    	ABuffer buf = new FullFileBuffer(stringsFile);
        Vector<Vector<String>> sections = LangReader.read(buf);
        
        return new StringsPanel(sections);
    }
    
    private MapPanel createMapPanel(File mapFile, Attributes as)
    throws IOException {
    	File tabFile = fileFromString(as.getValue("tab"));
        File paletteFile = fileFromString(as.getValue("palette"));
        File chunksFile = fileFromString(as.getValue("chunks"));
        dernc(mapFile);
        
        FileInputStream mapStream = new FileInputStream(mapFile);
        ABuffer tabStream = new FullFileBuffer(tabFile);
        FileInputStream chunksStream = new FileInputStream(chunksFile);
        FileInputStream paletteStream = new FileInputStream(paletteFile);
    
        THPalette palette = PaletteReader.readAll(paletteStream);
    
        paletteStream.close();
    
        THMap mapComponent = LevelReader.read(mapStream, tabStream, chunksStream, palette, new Color(0, 0, 0, 0));
    
        mapStream.close();
        //tabStream.close();
        chunksStream.close();
    
        return new MapPanel(mapComponent);
    }
    
    private RawPanel createRawPanel(File rawFile, Attributes as)
    throws IOException {
        File paletteFile = fileFromString(as.getValue("palette"));
        int width = Integer.parseInt(as.getValue("width"));
        int height = Integer.parseInt(as.getValue("height"));
        
        FileInputStream paletteStream = new FileInputStream(paletteFile);
        dernc(rawFile);
        FileInputStream rawStream = new FileInputStream(rawFile);
    
        THPalette palette = PaletteReader.readAll(paletteStream);
    
        paletteStream.close();
    
        Image img = RawReader.read(rawStream, palette, width, height);
    
        rawStream.close();
    
        return new RawPanel(img);
    }

    private File fileFromString(String filename) {
        File file = table.get(filename);
        dernc(file);
        return file;
    }

    private void dernc(File file) {
        try {
            FileInputStream in = new FileInputStream(file);
            int r = in.read();
            int n = in.read();
            int c = in.read();
            in.close();

            if (r == 'R' && n == 'N' && c == 'C') {
            	// 10 is missing - the real RNC magic number is {'R', 'N', 'C', 1, 0}
                // unpack the file in place
                DeRNC.main_unpack(file, file);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    /**
     * User chose "Open File..." from the Menu.
     */
	public void actionPerformed(ActionEvent arg0) {
		try {
	        File f = mainWindow.queryOpen("Choose file to open", this.themeHospitalDir);
			JComponent c = open(f, this.themeHospitalDir);
			if (c != null) {
				mainWindow.setContent(c);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}

class FileTypeExtractor extends DefaultHandler {
    private StringBuilder fileString;
    private Attributes attributes;
    private File openFile;
    private boolean found;

    public FileTypeExtractor() {
    }

    public Attributes getAttributes(File openFile) {
        this.openFile = openFile;
        found = false;
        SAXParserFactory factory = SAXParserFactory.newInstance();

        try {
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(new File("FileList.xml"), this);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (found == true)
            return attributes;
        else
            return null;
    }

    public void startElement(String uri, String localName, String qName,
            Attributes attributes) {
        if (!qName.equalsIgnoreCase("file") || (found == true))
            return;

        fileString = new StringBuilder();
        this.attributes = new AttributesImpl(attributes);
    }

    public void characters(char[] ch, int start, int length) {
        if (fileString == null || found == true)
            return;

        fileString.append(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) {
        if (!qName.equalsIgnoreCase("file") || found == true)
            return;

        if (fileString.toString().equalsIgnoreCase(openFile.getName()))
            found = true;
        else if (openFile.getName().matches(fileString.toString()))
            found = true;
    }
}