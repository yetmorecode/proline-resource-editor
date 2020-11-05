# PROLINE Resource File Format

This specification refers to resource files as used by the game F1 manager professional. The file extension is *.rsc.

## Signature

The resource files start with the signature "PROLINE Resource File  (c) 1997 by PROLINE Software GmbH"

![Alt text](signature.png?raw=true "Signature")

## General Structure

The different resources within a single file a referenced directly by the executable, leaving the file itself without any
map of itself. It is impossible to reconstruct the resource files without reverse engineering the offsets of different resources from the game executable. However, it's not too hard to make sense of them.

So far I've seen three different resource types used:

* VGA color palettes
* Fullscreen (640x480) pixmaps
* Tilemaps (variable size)

Byte order is little-endian, e.g. a dword of value 0x12345678 would be found as byte sequence "78 56 34 12" in the file, a word value 0x1234 as "34 12", etc.

## VGA color palettes

Color palettes are described by their size folled by simply 256 rgb colors as fed into the VGA DAC (256 * 3 bytes).

```
ddw size
db COLOR0_R
db COLOR0_G
db COLOR0_B
db COLOR1_R
db COLOR1_G
db COLOR1_B
db COLOR2_R
db COLOR2_G
db COLOR2_B
...
```

So far I've only seen full color palettes of ```size == 0x300``` (256 * 3). Some of those colors are dynamically overwritten when the palette is loaded, for example color 0  is usually cleared out to black or team specific colors are loaded into some part of the palette.

The color palette for the main menu background is found at 0x1014d36 (0x300 bytes in size) and shown below:

![Alt text](palette.png?raw=true "Palette")

Below is an decompiled and mostly annotated excerpt from the code used for loading palettes from resource files:

![Alt text](palette_code.png?raw=true "Palette Loading Code")

- At line 17 ```size``` is read.
- At line 18 The palette itself of given ```size``` is read
- Below that point you can see how certain colors are overwritten (lines 23-25 or 34 to 36), or how a whole block of colors is overwritten (lines 28-31).

The full code is quite more complex and seems to handle a lot of dynamic edge cases I've not been able to fully understand yet.

## Fullscreen pixmaps

Fullyscreen pixmaps are stored in a packed format (combining repeated pixles into a two byte sequence of color and repetitions) described by a simple header followed by the actual image data. The image data is simply a list of pixels directly written into the framebuffer and referencing index colors from the VGA palette (meaning each pixel is described as a 0-255 1-byte color number, while the 3 bytes for R, G and B values are described somewhere else in the palette).

```
ddw      SIZE
db[128]  HEADER
db[SIZE] PIXEL_COLORS
```

```SIZE``` describes the actual size of header + image data in the resource file. 

```HEADER``` is a 128 byte structure, containing the width and height of the image, but otherwise unused in the actual game as far as I can see.

```
0  ddw	dword		
4  ddw	dword		
8  dw	word	width	
10 dw	word	height	
12 db[116]	byte[116]	unused
```

Since repeated pixles are packed, the size of the image data can be considerably smaller than
the actual amount of screen to fill (640x480 pixels). The 6th bit in the color number is actually used to indicate repetions for pixels. When the bit is set (```color & 0xc0 == 0xc0```), the byte (masked by ```0x3f```) describes a number of repetitions and the next byte describes the pixels' color. If the bit is not set, the byte simply describes the pixels color.

This can be seen for example in the main background (offset 0xa76d4d, palette 0x1014d36):

![Alt text](fullscreen_packing.png?raw=true "Fullscreen Packing")

The pixel data highlighted of ```c3 31 c4 33``` will actually be unpacked to ```31 31 31 33 33 33 33``` as seen in the decompliation below:

![Alt text](fullscreen_code.png?raw=true "Fullscreen Image Loading Code")

- Lines 56-57 open the file and seek to the resource offset
- Lines 58 reads the size of the complete resource (header + image data)
- Lines 59-63 allocate memory for the resource and read it
- Line 66 sets up the first 128 bytes (0x80) as a header structure
- Line 67 advances ```resource_buffer_offset``` to the actual pixel data
- Lines 69 and following loop through the pixel data
- Lines 73 - 79 unpack a byte if the 6th bit is set
- Lines 80-85 write a simple color value (without unpkacing)

## Tilemaps

```
ddw unknown
ddw width
ddw height
ddw number of items
for each item:
ddw           data_size
db[data_size] data
```

```

```


