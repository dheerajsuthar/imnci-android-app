package org.dheeraj.imnci;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class TakePicture extends Activity {
	private static final String PICTURES_DIR = "/Pictures/";
	private static final String JPEG_FILE_PREFIX = "Imnci";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	private ImageView imagePreview;
	private Button buttonTakePic;
	private final int REQ_CODE = 200;
	private DbHelper dbHelper;
	private Spinner spinnerId;
	private SQLiteDatabase dbReader;
	private Cursor cursor;
	private ArrayList<String> idList;
	private ArrayAdapter<String> adapter;
	private String id;
	private String tableName = "pictures";
	private Uri imgURI;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.take_picture);
		id = null;
		imagePreview = (ImageView) findViewById(R.id.imagePic);
		buttonTakePic = (Button) findViewById(R.id.buttonTakePic);
		dbHelper = new DbHelper(this);
		spinnerId = (Spinner) findViewById(R.id.spinnerId);
		dbReader = dbHelper.getReadableDatabase();
		String columns[] = { "mid" };

		try {
			cursor = dbReader.query("mother_reg", columns, null, null, null,
					null, "mid DESC");
			if (cursor.moveToFirst()) {
				idList = new ArrayList<String>();
				do {
					idList.add(cursor.getString(cursor.getColumnIndex("mid")));
				} while (cursor.moveToNext());
				cursor.close();
				adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_spinner_item, idList);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinnerId.setAdapter(adapter);
				spinnerId
						.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub
								id = arg0.getItemAtPosition(arg2).toString();
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub

							}
						});

			} else {
				Toast.makeText(this, "No mother registration currently exists",
						Toast.LENGTH_LONG).show();
				return;
			}
		} finally {
			if (dbReader != null) {
				dbReader.close();
			}
		}
		buttonTakePic.setOnClickListener(new OnClickListener() {

			private Intent intent;
			private File imgFile;
			
			private SQLiteDatabase dbWriter;
			private ContentValues cv;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (id == null) {
					Toast.makeText(TakePicture.this,
							"Please select mother id first", Toast.LENGTH_LONG)
							.show();
					return;
				}
				imgFile = getImageFile();
				imgURI = Uri.fromFile(imgFile);

				dbWriter = dbHelper.getWritableDatabase();
				cv = new ContentValues();
				cv.put("mid", id);
				cv.put("uri", imgURI.getPath());
				try {
					dbWriter.insertOrThrow(tableName, null, cv);
				} finally {
					if (dbWriter != null)
						dbWriter.close();
				}
				intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imgURI);
				startActivityForResult(intent, REQ_CODE);
			}
		});

	}

	File getImageFile() {
		File storageDir = Environment.getExternalStorageDirectory();
		/*
		 * if(!storageDir.exists()){ try { storageDir.createNewFile();
		 * storageDir.mkdirs(); } catch (IOException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); } }
		 */
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		File image = null;
		try {
			image = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX,
					storageDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String mCurrentPhotoPath = image.getAbsolutePath();
		Toast.makeText(this, mCurrentPhotoPath, Toast.LENGTH_LONG).show();
		return image;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_CODE) {
			if (resultCode == RESULT_OK) {
				// Bundle extras = data.getExtras();
				// previewImage(extras.get("data"));
				Toast.makeText(this, "Picture saved", Toast.LENGTH_SHORT)
						.show();
				previewImage();
			}
		}
	}

	private void previewImage() {
		// TODO Auto-generated method stub
		Bitmap bmpFile = BitmapFactory.decodeFile(imgURI.getPath());
		imagePreview.setImageBitmap(bmpFile);
	}
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unbindDrawables(findViewById(R.id.takePicViewRoot));
		System.gc();
	}

	private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unbindDrawables(findViewById(R.id.takePicViewRoot));
		System.gc();
	}

}
