package com.xtel.goeuro.test;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;

public class voice extends Activity{
	 private static final int REQUEST_CODE = 1234;
	 int match = 0;
	 public void onCreate(Bundle savedInstanceState)
	    {
	        super.onCreate(savedInstanceState);
	        GameView myobj=new GameView(this, voice.this); 
	        startVoiceRecognitionActivity();
	    }    
	 public void startVoiceRecognitionActivity()
	    {	    
	        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	        startActivityForResult(intent, REQUEST_CODE);
	    }

	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		
	    if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
	    {     
	        ArrayList<String> matches = data.getStringArrayListExtra(
	                RecognizerIntent.EXTRA_RESULTS);

	    	if (matches.contains("up")) 
			match =1;
		if (matches.contains("down")) 
			match =2;
		if (matches.contains("left")) 
			match=4;
		if (matches.contains("right")) 
			match =3;	
	    }
	    super.onActivityResult(requestCode, resultCode, data);
           finish();
	}

    public  int getVariable()
    {
        return match;
    }
} 