/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
 */

package th.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import rnc.DeRNC;
import th.Main;
import th.view.AnimationPanel;
import th.view.FramePanel;
import th.view.ImagePanel;
import th.view.MapPanel;
import th.view.PalettePanel;
import th.view.RawPanel;
import th.view.SavegamePanel;
import th.view.SoundPanel;
import th.view.SpriteElementPanel;
import th.view.StringsPanel;

public class SmartReader extends DefaultHandler {
    private File themeHospitalDir = null;
    private JFileChooser fc = new JFileChooser();

    public SmartReader(File thDir) {
        this.themeHospitalDir = thDir;
    }

    public SmartReader() {
        getTHDir();
    }

    public JComponent open() {
        File f = queryOpen("Choose file to open");
        dernc(f);
        File thDir = getTHDir();
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
        File file = new File(getTHDir().getPath() + File.separator + filename);
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

    private File getTHDir() {
        if (themeHospitalDir != null)
            return themeHospitalDir;

        fc.setDialogTitle("Select your Theme Hospital Directory");
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int t = fc.showOpenDialog(null);

        if (t == JFileChooser.APPROVE_OPTION) {
            themeHospitalDir = fc.getSelectedFile();
        }

        return themeHospitalDir;
    }

    private File queryOpen(String title) {
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setDialogTitle(title);
        fc.setCurrentDirectory(getTHDir());
        fc.showOpenDialog(Main.getInstance());

        return fc.getSelectedFile();
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