package Utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import math.Vector3;

public class ObjReader {
	public ArrayList<Vector3> allVectors = new ArrayList<Vector3>();
	public ArrayList<Vector3> faceVectors = new ArrayList<Vector3>();
	public ArrayList<Vector3> getVerticesFromObjFile(String file){
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String str = br.readLine();
			float x = 0;
			float y = 0;
			float z = 0;
			while(str!=null) {
				String[] arr = str.split(" ");
				if(arr[0].equals("v")) {
					x = Float.parseFloat(arr[1]);
					z = Float.parseFloat(arr[2]);
					y = Float.parseFloat(arr[3]);
					allVectors.add(new Vector3(x,y,z));
				}
				if(arr[0].equals("f")) {
					String[] arr2;
					for(int i = 2;i<arr.length-1;i++) {
						arr2 = arr[i].split("/");
						faceVectors.add(allVectors.get(Integer.parseInt(arr2[0])-1));
						arr2 = arr[i+1].split("/");
						faceVectors.add(allVectors.get(Integer.parseInt(arr2[0])-1));
						arr2 = arr[1].split("/");
						faceVectors.add(allVectors.get(Integer.parseInt(arr2[0])-1));
					}
				}
				str = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return faceVectors;	
	}
}