package com.andev.androidshaderdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.andev.androidshaderdemo.activity.FilterActivity;

public class MainActivity extends AppCompatActivity {
	ListView listView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listView = (ListView) findViewById(R.id.listView);
		final Section section = new Section();
		listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, section.getSectionList()));

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				toOtherActivity(section.getSectionList().get(position));
			}
		});
	}

	private void toOtherActivity(String section) {
		if ("SimpleRender".equals(section)) {
			Intent intent = new Intent(MainActivity.this, FilterActivity.class);
			intent.putExtra("type", 0);
			startActivity(intent);
		} else if ("SimpleTextureRender".equals(section)) {
			Intent intent = new Intent(MainActivity.this, FilterActivity.class);
			intent.putExtra("type", 1);
			startActivity(intent);
		} else if ("MultiTextureRender".equals(section)) {
			Intent intent = new Intent(MainActivity.this, FilterActivity.class);
			intent.putExtra("type", 2);
			startActivity(intent);
		} else if ("SharpRender".equals(section)) {
			Intent intent = new Intent(MainActivity.this, FilterActivity.class);
			intent.putExtra("type", 3);
			startActivity(intent);
		} else if ("TwoFilterRender".equals(section)) {
			Intent intent = new Intent(MainActivity.this, FilterActivity.class);
			intent.putExtra("type", 4);
			startActivity(intent);
		}else if ("Mosaic".equals(section)) {
			Intent intent = new Intent(MainActivity.this, FilterActivity.class);
			intent.putExtra("type", 5);
			startActivity(intent);
		}else if ("Sobel Edge Detector".equals(section)) {
			Intent intent = new Intent(MainActivity.this, FilterActivity.class);
			intent.putExtra("type", 6);
			startActivity(intent);
		}else if ("Canny Edge Detector".equals(section)) {
			Intent intent = new Intent(MainActivity.this, FilterActivity.class);
			intent.putExtra("type", 7);
			startActivity(intent);
		}

	}

}
