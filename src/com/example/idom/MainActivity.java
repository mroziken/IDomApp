package com.example.idom;

import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;


public class MainActivity extends Activity {
	
   final Context context = this;
   private String url = "http://192.168.1.135:8080/get_layout";
   private HandleJSON obj;

@Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      //setContentView(R.layout.activity_main);      
      
      ScrollView sv = new ScrollView(this);
      
      LinearLayout ll = new LinearLayout(this);
      ll.setOrientation(LinearLayout.VERTICAL);
      
      try {
		parseJSONObject(ll,url);
      } catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
      }
      

      sv.addView(ll);

      this.setContentView(sv);
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items 
      //to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }
   
   public static void restartActivity(Activity act){

       Intent intent=new Intent();
       intent.setClass(act, act.getClass());
       act.startActivity(intent);
       act.finish();

}

   public void parseJSONObject(LinearLayout ll, String url) throws JSONException{
	  ArrayList<JSONObject> menu = new ArrayList<JSONObject>();
	  ArrayList<String> uniqMenuItems = new ArrayList<String>();
	  String [][] menuDetails = {};
      obj = new HandleJSON(url);
      obj.fetchJSON();

      while(obj.parsingComplete & obj.exceptionFlg);
      
      if (! obj.exceptionFlg){
    	  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context); 
    	  alertDialogBuilder.setTitle("Alert");
    	  alertDialogBuilder.setMessage("Problem z połączeniem z iDom Koordynator");
		  alertDialogBuilder.setCancelable(false);
		  alertDialogBuilder.setPositiveButton("Wyjdź",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, close
					// current activity
					MainActivity.this.finish();
				}
			  });
		  alertDialogBuilder.setNegativeButton("Próbuj ponownie",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, just close
					// the dialog box and do nothing
					dialog.cancel();
					restartActivity(MainActivity.this);
				}
			});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
		}
      
      menu=obj.getRetArray();
      
      menuDetails = new String[menu.size()][6];
      
      int x=0;
      for (JSONObject obj : menu){
    	  
    	  String UpperLevel = obj.getString("UpperLevel");
    	  if (! uniqMenuItems.contains(UpperLevel) & !UpperLevel.equals("ROOT")){
    		  uniqMenuItems.add(UpperLevel);
    	  }
    	  menuDetails[x][0]=UpperLevel;
    	  menuDetails[x][1] = obj.getString("ElementName");
    	  menuDetails[x][2] = obj.getString("ElementType");
    	  menuDetails[x][3] = obj.getString("CommandOn");
    	  menuDetails[x][4] = obj.getString("CommandOff");
    	  menuDetails[x][5] = obj.getString("State");
    	  x++;
      }
      
      
      for (String item : uniqMenuItems){
    	  TextView tv = new TextView(this);
    	  tv.setText(item);
    	  ll.addView(tv);
    	  for (int j=0; j<=menuDetails.length-1; j++){
    		  if (menuDetails[j][0].equals(item)){
    			  switch (menuDetails[j][2]){
    			  case "TextView": 		TextView  tv1 = new TextView(this);
    			  				    	tv1.setText("     "+menuDetails[j][1]+":		"+menuDetails[j][5]);
    			  				    	ll.addView(tv1);
    			  				    	break;
    			  case "Button": 		Button bt1 = new Button(this);
    			  						bt1.setText(menuDetails[j][1]);
    			  						bt1.setOnClickListener(new listenerButtonClick(menuDetails[j][3]));
    			  						ll.addView(bt1);
    			  						break;
    			  case "ToggleButton":	ToggleButton tb1 = new ToggleButton(this);
    			  						tb1.setText(menuDetails[j][1]);
    			  						tb1.setOnClickListener(new listenerToggleButtonClick(tb1,menuDetails[j][3],menuDetails[j][4],menuDetails[j][5]));
    			  						ll.addView(tb1);
    			  						break;
    			  case "SeekBar":		SeekBar sb1 = new SeekBar(this);
    			  						ll.addView(sb1);
    			  						break;
    			  
    			  default:			System.out.println("Invalid ElementyType");
    			  }
    		  }
    	  }
      }
      
   }
   
}