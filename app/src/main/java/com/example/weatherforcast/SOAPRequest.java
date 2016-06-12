package com.example.weatherforcast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class SOAPRequest {

	public String requestCities(String country){

	String URL = "http://www.webservicex.net/globalweather.asmx?WSDL";
	String SOAP_ACTION1 = "http://www.webserviceX.NET/GetCitiesByCountry";
    String NAMESPACE = "http://www.webserviceX.NET";
    String METHOD_NAME1 = "GetCitiesByCountry";
    String result_="";  
	   
	
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);       
        request.addProperty("CountryName", country);
		
      //Declare the version of the SOAP request
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
      
       envelope.setOutputSoapObject(request);
       envelope.dotNet = true;
      
       try {
             HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
             androidHttpTransport.call(SOAP_ACTION1, envelope);
            
             // Get the SoapResult from the envelope body.
             SoapObject result = (SoapObject)envelope.bodyIn;
    if(result != null)
             {
                   //Get the first property and change the label text
                  // txtCel.setText(result.getProperty(0).toString());
           	  
           	  result_=result.getProperty(0).toString();
             }  
             else
             {
                 result_="no data found";  
             }
       } catch (Exception e) {
             e.printStackTrace();
       } 
	return result_;  
	} 


	
	

	public String requestWeatherInfo(String city,String country){
		
	String SOAP_ACTION1 = "http://www.webserviceX.NET/GetWeather";
    String NAMESPACE = "http://www.webserviceX.NET";
    String METHOD_NAME1 = "GetWeather";
    String URL = "http://www.webservicex.net/globalweather.asmx?WSDL";
	String result_="";  
	   
	
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);       
        
		request.addProperty("CityName", city);
		request.addProperty("CountryName", country);
		
      //Declare the version of the SOAP request
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
      
       envelope.setOutputSoapObject(request);
       envelope.dotNet = true;
      
       try {
             HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            
             //this is the actual part that will call the webservice
             androidHttpTransport.call(SOAP_ACTION1, envelope);
            
             // Get the SoapResult from the envelope body.
             SoapObject result = (SoapObject)envelope.bodyIn;
    if(result != null)
             {
                   //Get the first property and change the label text
                  // txtCel.setText(result.getProperty(0).toString());
           	  
           	  result_=result.getProperty(0).toString();
             }  
             else
             {
                 result_="no data found";  
             }
       } catch (Exception e) {
             e.printStackTrace();
       } 
	return result_;  
	}


    public String getcountries(){

        String URL = "http://www.webservicex.net/country.asmx?WSDL";
        String SOAP_ACTION1 = "http://www.webserviceX.NET/GetCountries";
        String NAMESPACE = "http://www.webserviceX.NET";
        String METHOD_NAME1 = "GetCountries";
        String result_="";


        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);


        //Declare the version of the SOAP request
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;

        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION1, envelope);

            // Get the SoapResult from the envelope body.
            SoapObject result = (SoapObject)envelope.bodyIn;
            if(result != null)
            {
                //Get the first property and change the label text
                // txtCel.setText(result.getProperty(0).toString());

                result_=result.getProperty(0).toString();
            }
            else
            {
                result_="no data found";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result_;
    }


}
