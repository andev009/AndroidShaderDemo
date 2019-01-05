package com.andev.androidshaderdemo;


import java.util.ArrayList;
import java.util.List;

public class Section {
	List<String> sectionList = new ArrayList<>();

	public Section(){
		sectionList.add("SimpleRender");
		sectionList.add("SimpleTextureRender");
		sectionList.add("MultiTextureRender");
		sectionList.add("SharpRender");
		sectionList.add("TwoFilterRender");
		sectionList.add("Mosaic");
		sectionList.add("Sobel Edge Detector");
		sectionList.add("Canny Edge Detector");
		sectionList.add("Camera");
		sectionList.add("Native OpenGL ES");
		sectionList.add("Java EGL");
		sectionList.add("Color Lookup Table(LUT)");
		sectionList.add("Scale Animation");
		sectionList.add("Flash White");
		sectionList.add("Burr");
		sectionList.add("Soul out");
		sectionList.add("Shake");
		sectionList.add("Split Screen 1");
		sectionList.add("Split Screen 2");
		sectionList.add("Watermark");
		sectionList.add("Record");
	}

	List<String> getSectionList(){
		return sectionList;
	}
}
