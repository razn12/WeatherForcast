package com.example.weatherforcast;

import java.util.ArrayList;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Result;

import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.helpers.DefaultHandler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	Spinner country_sp, city_sp;
	TextView temp, hum, vis;
	String getCountry,weather,t,h,vi,city;
	TextToSpeech t1;
	Boolean isInternetPresent;
	ConnectionDetector cd;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		isInternetPresent = cd.isConnectingToInternet();
		if (!isInternetPresent) {
			//Toast.makeText(getApplication(),"No internet connection",Toast.LENGTH_SHORT).show();
			// Internet Connection is not present
			AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
			builder1.setTitle("Internet Connection Error");
			builder1.setMessage("Please connect to working Internet connection and Try Again. ");

			builder1.setCancelable(true);

			builder1.setPositiveButton(
					"0k",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							System.out.println("*****************");

							finish();
							dialog.cancel();
						}
					});
			AlertDialog alert11 = builder1.create();
			alert11.show();
		}
		country_sp = (Spinner) findViewById(R.id.spinner1);
		city_sp = (Spinner) findViewById(R.id.spinner2);

		temp = (TextView) findViewById(R.id.textView3);
		hum = (TextView) findViewById(R.id.textView4);
		vis = (TextView) findViewById(R.id.textView5);


		Country cName = new Country(MainActivity.this);
		cName.execute();

		country_sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				getCountry = country_sp.getSelectedItem().toString();
				MyTask task = new MyTask();
				task.execute();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		city_sp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				city=city_sp.getSelectedItem().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		t1=new TextToSpeech(getApplicationContext(),new TextToSpeech.OnInitListener() {

			@Override
			public void onInit(int status) {
				if(status != TextToSpeech.ERROR) {
					t1.setLanguage(Locale.UK);
				}
			}
		});


	}

	class MyTask extends AsyncTask<Void, Void, Void> {
		String response;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			SOAPRequest req = new SOAPRequest();
			response = req.requestCities(getCountry);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			System.out.println("**********************************************");

			System.out.println(response);

			System.out.println("**********************************************");
			ArrayList<String> list = readXmlData(response, "City");

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, list);
			city_sp.setAdapter(adapter);
		}

	}

	public ArrayList<String> readXmlData(String xml_data, final String tagname) {
		final ArrayList<String> list = new ArrayList<String>();
		try {


			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();

			parser.parse(IOUtils.toInputStream(xml_data), new DefaultHandler() {
				boolean found = false;

				public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {
					if (qName == tagname) {
						found = true;
					}

				}

				;

				public void characters(char[] ch, int start, int length) throws SAXException {
					if (found) {
						list.add(new String(ch, start, length));
					}
				}

				;

				public void endElement(String uri, String localName, String qName) throws SAXException {
					if (qName == tagname) {
						found = false;
					}
				}

				;

			});

		} catch (Exception e) {
			// TODO: handle exception
		}

		return list;
	}

	public void weatherforcast(View v) {
			weather task = new weather(MainActivity.this);
		task.execute();
		}

	class weather extends AsyncTask<Void, Void, Void> {
		String response;
		private ProgressDialog dialog;

		public weather(MainActivity activity) {
			dialog = new ProgressDialog(activity);
		}

		@Override
		protected void onPreExecute() {
			dialog.setTitle("Weather Forcast");
			dialog.setMessage("Fetching data...");
			dialog.show();
		}


		@Override
		protected Void doInBackground(Void... params) {
			SOAPRequest req = new SOAPRequest();
			response = req.requestWeatherInfo(city_sp.getSelectedItem().toString(), country_sp.getSelectedItem().toString());

			System.out.println(response);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			String text;

			if( response.equals("Data Not Found")){
				text= "Sorry data not found exception";
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}
			else {
				t = readXmlData(response, "Temperature").get(0);
				h = readXmlData(response, "RelativeHumidity").get(0);
				vi = readXmlData(response, "Visibility").get(0);
				temp.setText("temp: " + t);
				hum.setText("hum: " + h);
				vis.setText("vis: " + vi);


				if (dialog.isShowing()) {
					dialog.dismiss();
				}
				text = "Namastae! Welcome to weather forcast. " + "The curent temperature of " + city + " is " + t + "with humidity " + h + " and " + " viscosity " + vi + ". Have a nice day!";
				//Toast.makeText(getApplicationContext(), text, 5000).show();
			}

			t1.speak(text, TextToSpeech.QUEUE_FLUSH, null);
		}
	}

	class Country extends AsyncTask<Void, Void, Void>{
		String response;
		private ProgressDialog dialog;

		public Country(MainActivity activity){
			dialog = new ProgressDialog(activity);
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			dialog.setTitle("Country List");
			dialog.setMessage("Fetching data...");
			dialog.show();
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... params) {
			SOAPRequest req=new SOAPRequest();
			response=req.getcountries();
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			System.out.println("**********************************************");

			System.out.println(response);

			System.out.println("**********************************************");
			ArrayList<String> list=readXmlData(response, "Name");

			ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,list);
			country_sp.setAdapter(adapter);
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setMessage("Exit Weather Forecast?");

		builder1.setCancelable(true);

		builder1.setPositiveButton(
				"Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						System.out.println("*****************");

						finish();
						dialog.cancel();
					}
				});

		builder1.setNegativeButton(
				"No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						System.out.println("---------------");
						dialog.cancel();
					}
				});

		AlertDialog alert11 = builder1.create();
		alert11.show();
	}
}

