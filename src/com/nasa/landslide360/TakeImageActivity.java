package com.nasa.landslide360;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class TakeImageActivity extends Activity {
	Button takeImage;
	String path;
	String pathIm = null;
	int desiredImageWidth = 1024;
	int desiredImageHeight = 768;
	String ext;
	Uri imageUri;
	Bitmap bmp;
	int cameraData = 0;
	
	
	String gotlat;
	String gotlon;

	String book_image_path = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/Image_Folder/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.take_image_layout);
		
		
		gotlat= getIntent().getStringExtra("lat");
		 gotlon =getIntent().getStringExtra("lon");
		
		takeImage = (Button) findViewById(R.id.bt_take_pic);
	
		takeImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar c = Calendar.getInstance();

				SimpleDateFormat df3 = new SimpleDateFormat(
						"yyyy_MM_dd_HH_mm_ss");
				ext = df3.format(c.getTime());

				File direct = new File(book_image_path);

				if (!direct.exists()) {
					if (direct.mkdir()) {
						// directory is created;
					}

				}
				pathIm = ext + ".jpg";
				File file = new File(book_image_path + pathIm);

				imageUri = Uri.fromFile(file);
				path = file.getAbsolutePath();
				Constants.path = path;
				Intent i = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(i, cameraData);
			}
		});

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		Toast.makeText(getApplicationContext(), "RESULT CODE"+(resultCode == Activity.RESULT_OK), Toast.LENGTH_LONG).show();
		
		if (requestCode == cameraData) {

			if (resultCode == RESULT_OK) {
				

				
					
					bmp = decodeScaledBitmapFromSdCard(Constants.path, desiredImageWidth,
							desiredImageHeight);
					bmp = Bitmap.createScaledBitmap(bmp, desiredImageWidth,
							desiredImageHeight, true);

					
					Constants.bmp = bmp;
					
				
					
					System.out.print("CONVERT IMAGE");

					Intent iC = new Intent(TakeImageActivity.this,
							ReportLandSlide.class);
					
					iC.putExtra("lat", gotlat);
					iC.putExtra("lon", gotlon);
					
					//iC.putExtra("bmp_img", Constants.bmp);
					startActivity(iC);

			/*	} catch (Exception e) {
					e.printStackTrace();
					Log.d("IMAGESHOW", e.toString());

				}*/

			}
		}

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (Constants.bmp != null) {
			Bitmap new_bmp = Constants.bmp;
			

		}
		
	}

	public Bitmap decodeScaledBitmapFromSdCard(String filePath, int reqWidth,
			int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	public int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}



}
