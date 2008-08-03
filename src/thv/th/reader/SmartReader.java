/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
 */

package thv.th.reader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;

import javax.swing.JComponent;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import rnc.DeRNC;
import thv.th.view.AnimationPanel;
import thv.th.view.FramePanel;
import thv.th.view.ImagePanel;
import thv.th.view.MainWindow;
import thv.th.view.MapPanel;
import thv.th.view.PalettePanel;
import thv.th.view.RawPanel;
import thv.th.view.SavegamePanel;
import thv.th.view.SoundPanel;
import thv.th.view.SpriteElementPanel;
import thv.th.view.StringsPanel;

public class SmartReader
implements ActionListener {
    private File themeHospitalDir = null;
    private Hashtable<String, File> table;
    private MainWindow mainWindow;

    public SmartReader(File thDir, MainWindow mainWindow) {
    	this.mainWindow = mainWindow;
        this.themeHospitalDir = thDir;
        initMapping();
    }

    /*public SmartReader(MainWindow mainWindow) {
    	this.mainWindow = mainWindow;
        getTHDir();
        initMapping();
    }*/
    
    void initMapping() {
        //table = FileScanner.scanDirectory(getTHDir());
    	table = FileScanner.scanDirectory(this.themeHospitalDir);
    }

    public JComponent open() {
        File thDir = this.themeHospitalDir; //getTHDir();
        File f = mainWindow.queryOpen("Choose file to open", thDir);
        dernc(f);
        Attributes as = new FileTypeExtractor().getAttributes(f);

        if (as.getValue("type").equals("chunks")) {
            File tabFile = openFromString(as.getValue("tab"));
            File paletteFile = openFromString(as.getValue("palette"));
            return new ImagePanel(paletteFile, f, tabFile);
        } else if (as.getValue("type").equals("extchunks")) {
            File tabFile = openFromString(as.getValue("tab"));
            File paletteFile = openFromString(as.getValue("palette"));
            return new ImagePanel(paletteFile, f, tabFile, true);
        } else if (as.getValue("type").equals("spriteelement")) {
            File tabFile = openFromString(as.getValue("tab"));
            File paletteFile = openFromString(as.getValue("palette"));
            File chunksFile = openFromString(as.getValue("chunks"));
            return new SpriteElementPanel(paletteFile, chunksFile, tabFile, f);
        } else if (as.getValue("type").equals("frames")) {
            File tabFile = openFromString(as.getValue("tab"));
            File paletteFile = openFromString(as.getValue("palette"));
            File chunksFile = openFromString(as.getValue("chunks"));
            File spriteElementFile = openFromString(as
                    .getValue("spriteelement"));
            File listFile = openFromString(as.getValue("list"));
            return new FramePanel(paletteFile, chunksFile, tabFile,
                    spriteElementFile, f, listFile);
        } else if (as.getValue("type").equals("animstart")) {
            File tabFile = openFromString(as.getValue("tab"));
            File paletteFile = openFromString(as.getValue("palette"));
            File chunksFile = openFromString(as.getValue("chunks"));
            File spriteElementFile = openFromString(as
                    .getValue("spriteelement"));
            File framesFile = openFromString(as.getValue("frames"));
            File listFile = openFromString(as.getValue("list"));
            return new AnimationPanel(paletteFile, chunksFile, tabFile,
                    spriteElementFile, framesFile, listFile, f);
        } else if (as.getValue("type").equals("map")) {
            File tabFile = openFromString(as.getValue("tab"));
            File paletteFile = openFromString(as.getValue("palette"));
            File chunksFile = openFromString(as.getValue("chunks"));
            return new MapPanel(f, tabFile, chunksFile, paletteFile);
        } else if (as.getValue("type").equals("raw")) {
            File paletteFile = openFromString(as.getValue("palette"));
            int width = Integer.parseInt(as.getValue("width"));
            int height = Integer.parseInt(as.getValue("height"));
            return new RawPanel(paletteFile, width, height, f);
        } else if (as.getValue("type").equals("strings")) {
            return new StringsPanel(f);
        } else if (as.getValue("type").equals("palette")) {
            return new PalettePanel(f);
        } else if (as.getValue("type").equals("savegame")) {
            File frameFile = openFromString(as.getValue("frames"));
            File frameListFile = openFromString(as.getValue("framelist"));
            File frameElementFile = openFromString(as.getValue("frameelement"));
            File frameTabFile = openFromString(as.getValue("frametab"));
            File frameChunksFile = openFromString(as.getValue("framechunks"));
            File framePaletteFile = openFromString(as.getValue("framepalette"));
            File saveFile = f;
            File blockTabFile = openFromString(as.getValue("blocktab"));
            File blockChunksFile = openFromString(as.getValue("blockchunks"));
            File blockPaletteFile = openFromString(as.getValue("blockpalette"));

            return new SavegamePanel(frameFile, frameListFile,
                    frameElementFile, frameTabFile, frameChunksFile,
                    framePaletteFile, saveFile, blockTabFile, blockChunksFile,
                    blockPaletteFile);
        } else if (as.getValue("type").equals("sound")) {
            return new SoundPanel(f);
        }

        return null;
    }

    private File openFromString(String filename) {
        File file = table.get(filename);
        dernc(file);
        return file;
    }

    private void dernc(File file) {
        try {
            FileInputStream r = new FileInputStream(file);
            int c = r.read();

            if (c == 'R') {
                c = r.read();

                if (c == 'N') {
                    c = r.read();

                    if (c == 'C') {
                        // unpack the file in place
                        DeRNC.main_unpack(file, file);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }




    /**
     * User chose "Open File..." from the Menu.
     */
	public void actionPerformed(ActionEvent arg0) {
		JComponent c = open();
		if (c != null) {
			mainWindow.setContent(c);
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