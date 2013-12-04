package com.xtel.goeuro.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View {

	public voice v;


	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GameView(Context context) {
		super(context);
	}

	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public GameView(Context context, voice v) {
		super(context);
		this.v=v;

	}
	/*other methods */


	public boolean onTouchEvent(MotionEvent event) {
		voice_input();
		return false;
	}


	public boolean voice_input() {

		int result=0;
		v.startVoiceRecognitionActivity(); 
		result = v.getVariable();	
		/*movements in game based on value of ‘result’ */
		return true;
	}
}