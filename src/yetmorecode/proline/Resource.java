package yetmorecode.proline;

public class Resource {

	public enum Type {
		TYPE_PALETTE,
		TYPE_PIXMAP,
		TYPE_PIXMAPLIST
	}
	
	public Resource(long offset, String name, Type type) {
		setOffset(offset);
		setName(name);
		setType(type);
	}
	
	public Resource(long offset, String name, Type type, long paletteOffset) {
		setOffset(offset);
		setName(name);
		setType(type);
		setPaletteOffset(paletteOffset);
	}
	
	private long offset;
	private long paletteOffset;
	private String name;
	private Type type;
	
	public long getOffset() {
		return offset;
	}
	public void setOffset(long offset) {
		this.offset = offset;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	
	public long getPaletteOffset() {
		return paletteOffset;
	}
	public void setPaletteOffset(long paletteOffset) {
		this.paletteOffset = paletteOffset;
	}
	public String toString() {
		return String.format("%s (%x)", getName(), getOffset());
	}
}
