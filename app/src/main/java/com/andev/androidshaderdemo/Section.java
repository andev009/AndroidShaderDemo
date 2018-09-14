package com.andev.androidshaderdemo;


import java.util.ArrayList;
import java.util.List;

public class Section {
	List<String> sectionList = new ArrayList<>();

	public Section(){
		sectionList.add("SimpleRender");
		sectionList.add("SimpleTextureRender");
		sectionList.add("MultiTextureRender");
		sectionList.add("TwoTextureRender");
		sectionList.add("TwoFilterRender");
	}

	List<String> getSectionList(){
		return sectionList;
	}
}
