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

public class ImageViewer extends JPanel {
	private static final long serialVersionUID = 4470777298821205654L;
	private static final int PIXEL_SIZE = 1;
	private int width;
	private int height;
	private ArrayList<Color> pixels = new ArrayList<>();
	private FileInputStream input;
	
	public ImageViewer(String file, long offset, ArrayList<Color> palette) throws IOException {
		input = new FileInputStream(file);
		input.getChannel().position(offset);
		
		// Should be size of palette 0x0, but don't bother
    	int size = readInt(input);
    	input.readNBytes(8);
    	width = readShort(input);
    	height = readShort(input);
    	input.readNBytes(128-12);
    	
    	int unpackedSize;
    	System.out.println(String.format("%d x %d, size %x , %x, colors: %d", width, height, size, width*height, palette.size()));
		for (int i = 0; i < height; i++) {		
			for (int j = 0; j < width; j += unpackedSize) {
				int color = input.read();
				unpackedSize = 1;
    			if ((color & 0xc0) == 0xc0) {
    				int c = input.read();
        			unpackedSize = color & 0x3f;
        			for (int k = 0; k < unpackedSize; k++) {
        				pixels.add(palette.get(c+1 == 256 ? c : c+1));
        			}
    			} else {
    				pixels.add(palette.get(color+1 == 256 ? color : color+1));	
    			}
    		}
    	}
	
		setPreferredSize(new Dimension(width * PIXEL_SIZE, height * PIXEL_SIZE));
		System.out.println(String.format("pixels: %x, %x", pixels.size(), width * height));
	}
	
	public void paintComponent(Graphics graphics) {
	    super.paintComponent(graphics);
	    Graphics2D g2d = (Graphics2D) graphics;

		for (int i=0; i < height-1; i++) {
	    	for (int j = 0; j < width+1; j++) {
	    		g2d.setColor(pixels.get(i*(width+1)+j));
	    		g2d.fillRect(j*PIXEL_SIZE, i*PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);	
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
