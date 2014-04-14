package com.nasa.landslide360;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.james.mime4j.stream.NameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
public class ReportLandSlide extends Activity{

	Bitmap bmp;
	ImageView picture;
	EditText  comments,area,country,type,cause;
	Button sendReport;
	List<NameValuePair> values;
	String userComments;
	ProgressDialog progressDialog;
	Handler mHandler;
	Runnable mUpdateResults;
	String result = "";
	
	String gotlat;
	String gotlon;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_land_slide);
		
		gotlat=getIntent().getStringExtra("lat");
		gotlon=getIntent().getStringExtra("lon");
		
		
		Toast.makeText(getApplicationContext(), gotlat+" "+gotlon, Toast.LENGTH_LONG).show();
		bmp = Constants.bmp;
		comments = (EditText)findViewById(R.id.et_comments);
		area = (EditText)findViewById(R.id.editText2);
		country = (EditText)findViewById(R.id.editText1);
		type = (EditText)findViewById(R.id.editText3);
		sendReport = (Button)findViewById(R.id.bt_report_upload);
		cause = (EditText)findViewById(R.id.editText4);
		
		
		
		userComments = comments.getText().toString();
		
		
		picture  =(ImageView)findViewById(R.id.iv_captured);
		picture.setImageBitmap(bmp);
		
		
		
		
		sendReport.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				values = new ArrayList<NameValuePair>();

				

				values.add(new NameValuePair("uploadedfile",Constants.path));

				values.add(new NameValuePair("comments", comments.getText().toString()));
				values.add(new NameValuePair("lat", gotlat));
				values.add(new NameValuePair("lon", gotlon));
				values.add(new NameValuePair("country", country.getText().toString()));
				values.add(new NameValuePair("event_type", type.getText().toString()));
				values.add(new NameValuePair("region", area.getText().toString()));
				
				
				post(values);
			}
		});
		
		
		
		
		
	}
	
	public void post(final List<NameValuePair> nameValuePairs) {
		// Setting progressDialog properties
		progressDialog = ProgressDialog.show(ReportLandSlide.this, "",
				"Syncing Book Data...");

		mHandler = new Handler();
		// Function to run after thread
		mUpdateResults = new Runnable() {
			public void run() {

				progressDialog.dismiss();

				try {
					JSONObject jsonResult = new JSONObject(result);

					System.out.println("Return JSON Object: "
							+ jsonResult.toString());

					String msg = jsonResult.getString("message");

					if (msg.equals("success")) {

						Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
						
						
					} else {
						
					}

				} catch (Exception e) {
					
				}

			}
		};
		new Thread() {
			@Override
			public void run() {

				HttpClient httpClient = new DefaultHttpClient();
				HttpContext localContext = new BasicHttpContext();
				HttpPost httpPost = new HttpPost("http://mobioapp.net/apps/landslide360/image_upload.php");

				try {
					MultipartEntity entity = new MultipartEntity(
							HttpMultipartMode.BROWSER_COMPATIBLE);

					for (int index = 0; index < nameValuePairs.size(); index++) {
						if (nameValuePairs.get(index).getName()
								.equalsIgnoreCase("uploadedfile")) {
							// If the key equals to "image", we use FileBody
							// to transfer the data
							entity.addPart(
									nameValuePairs.get(index).getName(),
									new FileBody(new File(nameValuePairs.get(
											index).getValue())));
						} else {
							// Normal string data
							entity.addPart(nameValuePairs.get(index).getName(),
									new StringBody(nameValuePairs.get(index)
											.getValue()));
						}
					}

					httpPost.setEntity(entity);

					HttpResponse response = httpClient.execute(httpPost,
							localContext);
					HttpEntity result_entity = response.getEntity();
					String htmlResponse = EntityUtils.toString(result_entity);
					result = htmlResponse;
					//Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
					
					System.out.println("SYNC:::" + result);
					// server = true;
				} catch (IOException e) {
					e.printStackTrace();
					// server = false;
				}

				// dismiss the progress dialog

				// Calling post function
				mHandler.post(mUpdateResults);

			}
		}.start();

	}

}
