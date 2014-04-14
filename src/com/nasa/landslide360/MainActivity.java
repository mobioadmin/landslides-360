package com.nasa.landslide360;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;



public class MainActivity extends Activity {
 

Button ReportLandSlide;
Button ShowLandSlide;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    ReportLandSlide = (Button)findViewById(R.id.bt_report);
    ShowLandSlide = (Button)findViewById(R.id.bt_show_land);
    
    
    ReportLandSlide.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent i = new Intent();
			i.setClass(MainActivity.this, ShowLocationActivity.class);
			i.putExtra("CatagoryType", "Land");
			
			startActivity(i);
			
			
		}
	});
    
    ShowLandSlide.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent i = new Intent();
			i.setClass(MainActivity.this, ShowLandSlide.class);
			i.putExtra("CatagoryType", "Land");
			
			startActivity(i);
			
			
		}
	});

  
  
  
      
    }
}