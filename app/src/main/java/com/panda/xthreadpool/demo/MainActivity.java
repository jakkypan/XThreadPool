package com.panda.xthreadpool.demo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.panda.xthreadpool.ThreadCallback;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		App.getExecutors().setThreadTag("test")
				.setThreadCallback(new ThreadCallback() {
					@Override
					public void onError(Thread thread, String msg, Throwable t) {

					}

					@Override
					public void onCompleted(Thread thread) {

					}

					@Override
					public void onStart(Thread thread) {

					}
				}).execute(() -> System.out.println("do something..."));
	}
}