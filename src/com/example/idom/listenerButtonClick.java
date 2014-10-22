package com.example.idom;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public  class listenerButtonClick implements OnClickListener
{
	String url;

  public listenerButtonClick(String url) {
	  this.url=url;
  }
  
  public void onClick(View v) {
      HandleJSON obj = new HandleJSON(url);
      obj.fetchJSON();
      while(obj.parsingComplete);
      String YY = obj.getYear();
      String MM = obj.getMonth();
      String DD = obj.getDay();
      String HH = obj.getHour();
      String mm = obj.getMinute();
      String SS = obj.getSeconds();
      String ms = obj.getMSeconds();
      if (checkResult(v,YY,MM,DD,HH,mm,SS,ms)){
    	  Toast.makeText(v.getContext(), "Command excuted successfully on host", Toast.LENGTH_LONG).show();
      }
      else{
    	  Toast.makeText(v.getContext(), "Problem with processing command on host", Toast.LENGTH_LONG).show();
      }
  }

private boolean checkResult(View v,String YY, String MM, String DD, String HH, String mm, String SS, String ms) {

	String url="http://192.168.1.135:8080/check_cmd_proc?";
	url=url+"YY="+YY+"&MM="+MM+"&DD="+DD+"&HH="+HH+"&mm="+mm+"&SS="+SS+"&ms="+ms;
	Log.v("listenerButtonClick",url);
	Toast.makeText(v.getContext(), url, Toast.LENGTH_LONG).show();
	HandleJSON obj = new HandleJSON(url);
    obj.fetchJSON();
    while(obj.parsingComplete);
	return obj.getResult();
}

  	
}
