package yetmorecode.proline;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import javax.swing.JPanel;

public class PixmapListViewer extends JPanel {
	private static final long serialVersionUID = 4470777298821205654L;
	private static final int PIXEL_SIZE = 10;
	private int width;
	private int height;
	private ArrayList<Color> pixels = new ArrayList<>();
	private FileInputStream input;
	private int itemCount;
	
	public PixmapListViewer(String file, long offset, ArrayList<Color> palette) throws IOException {
		input = new FileInputStream(file);
		input.getChannel().position(offset);
		
		// Should be size of palette 0x0, but don't bother
    	readInt(input);
    	width = readInt(input);
    	height = readInt(input);
    	itemCount = readInt(input);
    	
    	System.out.println(String.format("itemCount 0x%x, %d x %d = 0x%x", itemCount, width, height, width*height));
    	for (int item = 0; item < itemCount; item++) {
    		// first item
    		long pos = input.getChannel().position();
        	int size = readInt(input);    	
        	System.out.println(String.format("item %x %x-%x: size %x", item, pos, pos+size, size));
        	
    		for (int i = 0; i < size; i++) {
        		int color = input.read();
        		if (color == 0) {
        			int skip = input.read();
        			i++;
        			for (int j = 0; j < skip; j++) {
            			pixels.add(palette.get(0));	
            		}
        		} else {
        			pixels.add(palette.get(color+1 == 256 ? color : color+1));
        		}
        	}	
    	}
    
		setPreferredSize(new Dimension(Math.max(640, 10 + width * PIXEL_SIZE), (height+1) * PIXEL_SIZE * itemCount));
	}
	
	public void paintComponent(Graphics graphics) {
	    super.paintComponent(graphics);
	    Graphics2D g2d = (Graphics2D) graphics;

	    var spacing = 0;
		for (int i=0; i < height*itemCount; i++) {
			
			if (i % height == 0) {
				spacing += 10;
			}
			
	    	for (int j = 0; j < width; j++) {
	    		g2d.setColor(pixels.get(i*(width)+j));
	    		g2d.fillRect(10 + j*PIXEL_SIZE, i*PIXEL_SIZE+spacing, PIXEL_SIZE, PIXEL_SIZE);	
	    	}
	    } 
	}
	
	protected static int readInt(FileInputStream input) throws IOException  {
		byte[] bytes = input.readNBytes(4);
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		return bb.getInt();
	}
	
	protected static int readShort(FileInputStream input) throws IOException  {
		byte[] bytes = input.readNBytes(2);
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		return bb.getShort();
	}
}
