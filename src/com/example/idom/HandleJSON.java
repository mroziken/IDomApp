package com.example.idom;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;

public class HandleJSON {

   private String urlString = null;
   private Boolean result = false;
   private String ts;
   ArrayList<JSONObject> list = new ArrayList<JSONObject>();

   public volatile boolean parsingComplete = true;
   public volatile boolean exceptionFlg = true;
   
   public HandleJSON(String url){
      this.urlString = url;
   }
   public ArrayList<JSONObject> getRetArray(){
      return list;
   }
   
   public Boolean getResult(){
	   return result;
   }
   
   public String getTs(){
	   return ts;
   }
   
   public String getYear(){
	   return ts.substring(0,4);
   }
   public String getMonth(){
	   return ts.substring(5,7);
   }
   public String getDay(){
	   return ts.substring(8,10);
   }
   public String getHour(){
	   return ts.substring(11,13);
   }
   public String getMinute(){
	   return ts.substring(14,16);
   }
   public String getSeconds(){
	   return ts.substring(17,19);
   }
   public String getMSeconds(){
   		return ts.substring(20,26);
   }
   

   @SuppressLint("NewApi")
   public void readAndParseJSON(String in) {
      try {
         JSONObject reader = new JSONObject(in);
         
         result = reader.getBoolean("result");
         
         ts = reader.getString("ts");

         JSONArray menu  = reader.getJSONArray("menu");
         
         for (int i=0; i<menu.length(); i++) {
        	 	JSONObject row = menu.getJSONObject(i);
        	    list.add(row);
         }



         parsingComplete = false;



        } catch (Exception e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
        }

   }
   public void fetchJSON(){
      Thread thread = new Thread(new Runnable(){
         @Override
         public void run() {
        	 try {
        		 URL url = new URL(urlString);
        		 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        		 conn.setReadTimeout(10000 /* milliseconds */);
        		 conn.setConnectTimeout(15000 /* milliseconds */);
        		 conn.setRequestMethod("GET");
        		 conn.setDoInput(true);
        		 // Starts the query
        		 conn.connect();
        		 InputStream stream = conn.getInputStream();

        		 String data = convertStreamToString(stream);

        		 readAndParseJSON(data);
        		 stream.close();

        	 } catch (Exception e) {
        		 e.printStackTrace();
        		 exceptionFlg = false;
            
        	 }
         }
      });

       thread.start(); 		
   }
   static String convertStreamToString(java.io.InputStream is) {
      java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
      return s.hasNext() ? s.next() : "";
   }
}
