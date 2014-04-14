package com.nasa.landslide360;





import android.graphics.Bitmap;
import android.os.Environment;

public final class Constants {

	public static String book_image_path = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/Image_Folder/";

	public static String path = null;

	public static boolean crop = false;

	public static String photo_image_path = null;

	
	public static Bitmap bmp = null;
	/*
	 * Twitter credentials
	 */
	public static final String CONSUMER_KEY = "5lHxUXOtsAGrKwzprseFaw";
	public static final String CONSUMER_SECRET = "JqaiUgWCW8H4aEqPQ9dJM96Fh7aqGoOOqGiFVp4UA";
	// Public arralist of gallery and photo


	private Constants() {
	}

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}

	public static class Extra {
		public static final String IMAGES = "com.nostra13.example.universalimageloader.IMAGES";
		public static final String IMAGE_POSITION = "com.nostra13.example.universalimageloader.IMAGE_POSITION";
	}
}
