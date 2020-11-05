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

public class PaletteViewer extends JPanel {
	private static final long serialVersionUID = 8218483032643388806L;

	public final static int COLOR_SIZE = 20;
	
	private FileInputStream input;
	public ArrayList<Color> colors = new ArrayList<>();
	
	public PaletteViewer(String filename, long offset) throws IOException {
		this.input = new FileInputStream(filename);
		this.input.getChannel().position(offset);
		
    	input.read();
		for (int i=0; i < 256; i++) {
    		int r = input.read();
    		int g = input.read();
    		int b = input.read();
    		colors.add(new Color(r << 2, g << 2, b << 2));
		}
		
		if (offset == 0x1014d36) {
			colors.set(192, new Color(0x3f << 2, 0x3f << 2, 0x3f << 2));
		}
		if (offset == 0x9c768f || offset == 0x1014d36) {
			colors.set(0, new Color(0, 0, 0));
		}
		
		setPreferredSize(new Dimension(40*COLOR_SIZE, 256*COLOR_SIZE));
	}
	
	public void paintComponent(Graphics graphics) {
	    super.paintComponent(graphics);
	    Graphics2D g2d = (Graphics2D) graphics;

    	// Should be size of palette 0x300, but don't bother
		for (int i=0; i < 16; i++) {
	    	for (int j = 0; j < 16; j++) {
	    		int index = i*16+j;
	    		Color c = colors.get(index);
	    		g2d.setColor(c);
	    		g2d.fillRect(0, index*COLOR_SIZE, COLOR_SIZE*30, COLOR_SIZE);
	    		g2d.drawString(String.format("%d #%02x%02x%02x", index, c.getRed(), c.getGreen(), c.getBlue()), COLOR_SIZE*32, index*COLOR_SIZE+14);
	    	}
	    }   
	}
	
	protected int readInt(FileInputStream input) throws IOException  {
		byte[] bytes = input.readNBytes(4);
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		return bb.getInt();
	}
}
