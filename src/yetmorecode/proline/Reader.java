package yetmorecode.proline;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class Reader extends JFrame implements TreeSelectionListener {
	private static final long serialVersionUID = 2197788420629252068L;
	private JTree tree;
	private JScrollPane contentScroll;
	private JLabel content;
	private JSplitPane splitPane;
	private JSplitPane propertiesSplit;
	
	JMenuBar menubar;
	JMenu menu, submenu;
	JMenuItem menuOpen;
	
	DefaultMutableTreeNode top;
	String file;
	
	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
		loadFile(file);
	}

	public static void main(String[] args) {
		Reader r = new Reader();
		r.setVisible(true);
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	            "proline resource file", "rsc");
	        chooser.setFileFilter(filter);
	    int returnVal = chooser.showOpenDialog(r);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	r.setFile(chooser.getSelectedFile().getAbsolutePath());
	    }
		
	}
	
	public Reader() {
		setTitle("Proline Resource Editor 1.0");
        setSize(1300,700);
	    addWindowListener(new WindowAdapter() {
	    	public void windowClosing(WindowEvent windowEvent){
	            System.exit(0);
	        }        
	    });    
	    
	
	
	    DefaultMutableTreeNode t = new DefaultMutableTreeNode("");
	    
	    tree = new JTree(t);
	    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	    tree.addTreeSelectionListener(this);
	    
	    JScrollPane treeScroll = new JScrollPane(tree);
	    
	    content = new JLabel("");
	    contentScroll = new JScrollPane(content);
	    contentScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
	    
	    var propertieScroll = new JScrollPane();
	    propertiesSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	    propertiesSplit.setLeftComponent(contentScroll);
	    propertiesSplit.setRightComponent(propertieScroll);
	    propertiesSplit.setDividerLocation(650);
	    
	    splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	    splitPane.setLeftComponent(treeScroll);
	    splitPane.setRightComponent(propertiesSplit);
	    splitPane.setDividerLocation(250);
	    
	    //Provide minimum sizes for the two components in the split pane
	    Dimension minimumSize = new Dimension(100, 50);
	    treeScroll.setMinimumSize(minimumSize);
	    contentScroll.setMinimumSize(minimumSize);
	    
	    add(splitPane);

	    /*
	    //Create the menu bar.
	    menubar = new JMenuBar();

	    //Build the first menu.
	    menu = new JMenu("File");
	    menu.setMnemonic(KeyEvent.VK_F);
	    menubar.add(menu);
	    
	    menuOpen = new JMenuItem("Open",
                KeyEvent.VK_O);
	    menuOpen.addActionListener(this);
	    menu.add(menuOpen);
	    setJMenuBar(menubar);
	    */
	
	    
	}

	private void loadFile(String filename) {
	    var palettesCategory = new DefaultMutableTreeNode("palettes");
	    var pixmapsCategory = new DefaultMutableTreeNode("pixmaps");
	    
	    top = new DefaultMutableTreeNode(filename);
		
	    DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
	    model.setRoot(top);
	    
	    top.add(palettesCategory);
	    
	    var p = new DefaultMutableTreeNode(new Resource(0x1014d36, "main palette", Resource.Type.TYPE_PALETTE));
	    palettesCategory.add(p);
	    
	    top.add(pixmapsCategory);
	    pixmapsCategory.add(new DefaultMutableTreeNode(new Resource(0xa76d4d, "main menu background", Resource.Type.TYPE_PIXMAP, 0x1014d36)));
	    pixmapsCategory.add(new DefaultMutableTreeNode(new Resource(0x6316d0, "loading screen a", Resource.Type.TYPE_PIXMAP, 0x9c4047)));
	    pixmapsCategory.add(new DefaultMutableTreeNode(new Resource(0x6625af, "loading screen b", Resource.Type.TYPE_PIXMAP, 0x9c434b)));
	    pixmapsCategory.add(new DefaultMutableTreeNode(new Resource(0xafd2ee, "end", Resource.Type.TYPE_PIXMAP, 0x9c3a3f)));
	    pixmapsCategory.add(new DefaultMutableTreeNode(new Resource(0x69d750, "cryface", Resource.Type.TYPE_PIXMAP, 0x9c464f)));
	    pixmapsCategory.add(new DefaultMutableTreeNode(new Resource(0x5cd5ad, "crew", Resource.Type.TYPE_PIXMAP, 0x9c3d43)));
	    pixmapsCategory.add(new DefaultMutableTreeNode(new Resource(0xafd2ee, "end2", Resource.Type.TYPE_PIXMAP, 0x9c3a3f)));
	    
	    var multiCategory = new DefaultMutableTreeNode("multi");
	    top.add(multiCategory);
	    
	    
	    
	    var merch = new DefaultMutableTreeNode("merchandise");
	    top.add(merch);
	    merch.add(new DefaultMutableTreeNode(new Resource(0x4379b8, "background", Resource.Type.TYPE_PIXMAP, 0x1014d36)));
	    
	    var items = new DefaultMutableTreeNode("item pixmaps");
	    merch.add(items);
	    items.add(new DefaultMutableTreeNode(new Resource(0x225148, "poster", Resource.Type.TYPE_PIXMAPLIST, 0x9c3a3f)));
	    items.add(new DefaultMutableTreeNode(new Resource(0x2343a6, "poster", Resource.Type.TYPE_PIXMAPLIST, 0x9c3a3f)));
	    items.add(new DefaultMutableTreeNode(new Resource(0x23821c, "poster", Resource.Type.TYPE_PIXMAPLIST, 0x9c3a3f)));
	    items.add(new DefaultMutableTreeNode(new Resource(0x21c13c, "poster", Resource.Type.TYPE_PIXMAPLIST, 0x9c3a3f)));
	    items.add(new DefaultMutableTreeNode(new Resource(0x2238ef, "poster", Resource.Type.TYPE_PIXMAPLIST, 0x9c3a3f)));
	    items.add(new DefaultMutableTreeNode(new Resource(0x22bfc9, "poster", Resource.Type.TYPE_PIXMAPLIST, 0x9c3a3f)));
	    items.add(new DefaultMutableTreeNode(new Resource(0x22fe7d, "poster", Resource.Type.TYPE_PIXMAPLIST, 0x9c3a3f)));
	    items.add(new DefaultMutableTreeNode(new Resource(0x2306df, "poster", Resource.Type.TYPE_PIXMAPLIST, 0x9c3a3f)));
	    items.add(new DefaultMutableTreeNode(new Resource(0x23b3bd, "poster", Resource.Type.TYPE_PIXMAPLIST, 0x9c3a3f)));
	    items.add(new DefaultMutableTreeNode(new Resource(0x22eba3, "poster", Resource.Type.TYPE_PIXMAPLIST, 0x9c3a3f)));
	    items.add(new DefaultMutableTreeNode(new Resource(0x22944a, "poster", Resource.Type.TYPE_PIXMAPLIST, 0x9c3a3f)));
	    items.add(new DefaultMutableTreeNode(new Resource(0x23197d, "poster", Resource.Type.TYPE_PIXMAPLIST, 0x9c3a3f)));
	    items.add(new DefaultMutableTreeNode(new Resource(0x233802, "poster", Resource.Type.TYPE_PIXMAPLIST, 0x9c3a3f)));
	    items.add(new DefaultMutableTreeNode(new Resource(0x22b719, "poster", Resource.Type.TYPE_PIXMAPLIST, 0x9c3a3f)));
	    items.add(new DefaultMutableTreeNode(new Resource(0x236a3a, "poster", Resource.Type.TYPE_PIXMAPLIST, 0x9c3a3f)));
	    items.add(new DefaultMutableTreeNode(new Resource(0x221b9e, "poster", Resource.Type.TYPE_PIXMAPLIST, 0x9c3a3f)));
	    items.add(new DefaultMutableTreeNode(new Resource(0x222d25, "poster", Resource.Type.TYPE_PIXMAPLIST, 0x9c3a3f)));
	    items.add(new DefaultMutableTreeNode(new Resource(0x21fb77, "poster", Resource.Type.TYPE_PIXMAPLIST, 0x9c3a3f)));
	    items.add(new DefaultMutableTreeNode(new Resource(0x22244d, "poster", Resource.Type.TYPE_PIXMAPLIST, 0x9c3a3f)));
	    items.add(new DefaultMutableTreeNode(new Resource(0x23049a, "poster", Resource.Type.TYPE_PIXMAPLIST, 0x9c3a3f)));
	    items.add(new DefaultMutableTreeNode(new Resource(0x227b93, "puppet", Resource.Type.TYPE_PIXMAPLIST, 0x9c3a3f)));
	    
	    
	    var editor = new DefaultMutableTreeNode("Editor");
	    top.add(editor);
	    editor.add(new DefaultMutableTreeNode(new Resource(0x20b0398, "Background drivers", Resource.Type.TYPE_PIXMAP, 0x2126203)));
	    editor.add(new DefaultMutableTreeNode(new Resource(0x20d82c7, "Background engineers", Resource.Type.TYPE_PIXMAP, 0x2126203)));
	    editor.add(new DefaultMutableTreeNode(new Resource(0x2100148, "Background teams", Resource.Type.TYPE_PIXMAP, 0x2126203)));
	    editor.add(new DefaultMutableTreeNode(new Resource(0x213007d, "Alphabet", Resource.Type.TYPE_PIXMAPLIST, 0x2126203)));
	    editor.add(new DefaultMutableTreeNode(new Resource(0x21337ca, "Button arrow right", Resource.Type.TYPE_PIXMAPLIST, 0x2126203)));
	    editor.add(new DefaultMutableTreeNode(new Resource(0x213396a, "Button arrow left", Resource.Type.TYPE_PIXMAPLIST, 0x2126203)));
	    editor.add(new DefaultMutableTreeNode(new Resource(0x2133b0a, "?", Resource.Type.TYPE_PIXMAPLIST, 0x2126203)));
	    editor.add(new DefaultMutableTreeNode(new Resource(0x2133e26, "?", Resource.Type.TYPE_PIXMAPLIST, 0x2126203)));
	    editor.add(new DefaultMutableTreeNode(new Resource(0x2133b0a, "?", Resource.Type.TYPE_PIXMAPLIST, 0x2126203)));
	    editor.add(new DefaultMutableTreeNode(new Resource(0x87251, "Flags", Resource.Type.TYPE_PIXMAPLIST, 0x2126203)));
	    editor.add(new DefaultMutableTreeNode(new Resource(0x2134722, "Car numbers", Resource.Type.TYPE_PIXMAPLIST, 0x2126203)));
	    editor.add(new DefaultMutableTreeNode(new Resource(0x213a67b, "Button (red)", Resource.Type.TYPE_PIXMAPLIST, 0x2126203)));
	    //editor.add(new DefaultMutableTreeNode(new Resource(0x40, "alphabet", Resource.Type.TYPE_PIXMAPLIST, 0x2126203)));
	    
	    
	    var pcx = new DefaultMutableTreeNode("Auto-detected PCX");
	    top.add(pcx);
	    FileInputStream input;
	    long pos = 0;
		try {
			input = new FileInputStream(file);
			
			input.getChannel().position(0);
			while (input.available() > 4) {
				input.getChannel().position(pos);
				
				int b = input.read();
				if (b == 0xa) {
					b = input.read();
					if (b == 5) {
						b = input.read();
						if (b == 1) {
							b = input.read();
							if (b == 8) {
								System.out.println(String.format("found 0a 05 01 08 at %x", pos));
								if (pos >= 4) {
									pcx.add(new DefaultMutableTreeNode(new Resource(pos-4, "", Resource.Type.TYPE_PIXMAP, 0x2126203)));	
									model.reload();
								}
							}
						}
					}
				}
				
				
				pos++;
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
	    tree.expandPath(new TreePath(editor.getPath()));
	    tree.expandPath(new TreePath(merch.getPath()));
	    tree.expandPath(new TreePath(palettesCategory.getPath()));
	    tree.expandPath(new TreePath(pixmapsCategory.getPath()));
	    
	}
	
	public void valueChanged(TreeSelectionEvent e) {
		//Returns the last path element of the selection.
		//This method is useful only when the selection model allows a single selection.
		var node = (DefaultMutableTreeNode)
		               tree.getLastSelectedPathComponent();

		if (node == null)
			//Nothing is selected.     
			return;

		Object nodeInfo = node.getUserObject();
		if (node.isLeaf()  && !(nodeInfo instanceof String)) {
			Resource r = (Resource)nodeInfo;
			content.setText(r.getName());
			if (r.getType() == Resource.Type.TYPE_PALETTE) {
				try {
					var v = new PaletteViewer(file, r.getOffset());
					contentScroll = new JScrollPane(v, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
					        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					propertiesSplit.setLeftComponent(contentScroll);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			} else if (r.getType() == Resource.Type.TYPE_PIXMAP) {
				try {
					var p = new PaletteViewer(file, r.getPaletteOffset());
					var v = new ImageViewer(file, r.getOffset(), p.colors);
					
					contentScroll = new JScrollPane(v, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
					        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					propertiesSplit.setLeftComponent(contentScroll);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else if (r.getType() == Resource.Type.TYPE_PIXMAPLIST) {
				try {
					var p = new PaletteViewer(file, r.getPaletteOffset());
					var v = new PixmapListViewer(file, r.getOffset(), p.colors);
					contentScroll = new JScrollPane(v, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
					        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					propertiesSplit.setLeftComponent(contentScroll);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} else {
			 
		}
	}
	
	protected static short readShort(FileInputStream input) throws IOException  {
		byte[] bytes = input.readNBytes(2);
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		return bb.getShort();
	}

	protected static int readInt(FileInputStream input) throws IOException  {
		byte[] bytes = input.readNBytes(4);
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		return bb.getInt();
	}
	
	protected byte readByte(FileInputStream input) throws IOException {
		byte[] bytes = input.readNBytes(1);
		return bytes[0];
	}

	
}
