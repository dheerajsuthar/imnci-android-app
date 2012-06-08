package org.dheeraj.imnci;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class GeoLocator extends Service {

	String serviceProvider;
	LocationManager lmngr;
	Location location;
	String locServiceURL;
	public static final String tag = "geoLocator";

	@Override
	public void onCreate() {
		Log.d(tag, "in create");
		// TODO Auto-generated method stub
		super.onCreate();
		
		

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.d(tag, "in destroy");

		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		Log.d(tag, "in start");
		super.onStart(intent, startId);
		if (((ImnciApplication) getApplication()).getAppAttribs()
				.isGSMAvailable())
			this.serviceProvider = LocationManager.GPS_PROVIDER;
		else
			this.serviceProvider = LocationManager.NETWORK_PROVIDER;
		this.locServiceURL = ((ImnciApplication) getApplication())
				.getAppAttribs().getLocUrl();
		Log.d(tag, this.serviceProvider);
		

		lmngr = (LocationManager) GeoLocator.this
				.getSystemService(Context.LOCATION_SERVICE);
		LocationListener locListener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				locationUpdater lup = new locationUpdater(
						GeoLocator.this.locServiceURL, location);
				lup.start();
			}

		};

		lmngr.requestLocationUpdates(this.serviceProvider, 0, 0, locListener);

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}

class locationUpdater extends Thread {

	String locServiceURL;
	Location location;

	public locationUpdater(String locUrl, Location location) {
		// TODO Auto-generated constructor stub
		this.locServiceURL = locUrl;
		this.location = location;

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		sendLocationToPortal(location);
	}

	protected void sendLocationToPortal(Location location) {
		// TODO Auto-generated method stub
		String longitude = "" + location.getLongitude();
		String latitude = "" + location.getLatitude();

		Log.d("geolocator-service", longitude + " " + latitude);
		String url = this.locServiceURL;
		Log.d("geolocator-thread:url", url);
		String response;

		try {
			URLConnection httpConn = new URL(url).openConnection();
			httpConn.setDoOutput(true);
			writeToServer(httpConn, "lat="+latitude+"&long="+longitude);
			
			Log.d("geolocator-thread","written to server waiting for outpu");
			response = readFromServer(httpConn);
			Log.d("geolocator-thread", "Server output"+response);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();

		}
	}

	private void writeToServer(URLConnection httpConn, String param
			) throws IOException {
		// TODO Auto-generated method stub
		
		OutputStreamWriter writer = new OutputStreamWriter(
				httpConn.getOutputStream());
		writer.write(param);
		writer.close();
	}

	private String readFromServer(URLConnection httpConn) throws IOException {
		// TODO Auto-generated method stub
		String response = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(
				httpConn.getInputStream()));
		String temp;
		while ((temp = br.readLine()) != null)
			response += temp;
		br.close();
		return response;
	}

}