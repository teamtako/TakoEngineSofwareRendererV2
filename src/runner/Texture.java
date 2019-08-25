package runner;
import math.Vector2;
import math.Vector4;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


import javax.imageio.ImageIO;

 class Texture {
 String filePath;
 boolean Alpha;
 byte turn[];
 private byte pic[][][];
 int height;
 int width;

Texture(String path) throws IOException{
	filePath=path;
	reload();	
}
//just for debug
public void printRGBAData() {
	String temp="";
	for (int i = 0; i < turn.length; i++) {
		if(i%4==0&&i!=0) {
			System.out.print("|");
		}
		if(i%(width*4)==0&&i!=0) {
			System.out.print("\n");
		}
		temp=Byte.toUnsignedInt(turn[i])+"";
		while(temp.length()!=3) {
			temp="0"+temp;
		}
		System.out.print(temp);
		if((i+1)%4!=0) {
			System.out.print(",");
		}
	}
	System.out.print("|");
}
public Vector4 getPixAt(Vector2 v2) {
	
	Vector4 pixData = new Vector4();
	float converX =v2.x*width;
	float converY =v2.y*height;
	pixData[0]=Byte.toUnsignedInt(pic[(int) converX][(int) converY][0]);
	pixData[1]=Byte.toUnsignedInt(pic[(int) converX][(int) converY][1]);
	pixData[2]=Byte.toUnsignedInt(pic[(int) converX][(int) converY][2]);
	pixData[3]=Byte.toUnsignedInt(pic[(int) converX][(int) converY][3]);
	return pixData;

}
public byte[][][] getRGB3D(){
	return pic;
}
public void load(String path) throws IOException {
		filePath=path;
		reload();
	
	
}
public void reload() throws IOException {
	BufferedImage img;
	img = ImageIO.read(new File(filePath));
	final int w = img.getWidth();
	final int h = img.getHeight();
	width=w;
	height=h;
	System.out.println(filePath+" has been loaded with a size of "+w+"/"+h);
	 pic = new byte[w][h][4];
	 turn = new byte[w*h*4];
	//creates layers separately
	byte[][] red = new byte[w][h];
	byte[][] green = new byte[w][h];
	byte[][] blue = new byte[w][h];
	byte[][] alpha = new byte[w][h];
	//scans image rgba and isolates layers
	for(int x=0; x<w; x++){
	  for(int y=0; y<h; y++){
	     int color = img.getRGB(x,y);
	     alpha[x][y] = (byte)(color>>24);
	     red[x][y] = (byte)(color>>16);
	     green[x][y] = (byte)(color>>8);
	     blue[x][y] = (byte)(color);
	  }
	}

	//puts all layers into 1 3d array
	//Key for pic:
	//pic[x location of pixel][y loc of pixel][layer number(0=r,1=green,2=blue,3=alpha)]
	for (int i = 0; i < w; i++) {
		for (int j = 0; j < h; j++) {
			
				pic[i][j][0]=red[i][j];
				pic[i][j][1]=green[i][j];
				pic[i][j][2]=blue[i][j];
				pic[i][j][3]=alpha[i][j];
				
		}
	} 
	//condenses 3d array into 1d array with 4x the size and organized linearly
	int p=0;
	for (byte[][] bs : pic) {
		for (byte[] bs2 : bs) {
			for (byte bs3 : bs2) {
				turn[p]=(byte)Byte.toUnsignedInt(bs3);
				p++;
			}
		}
	}
}


 }
