package com.jaj.aho.osframe.util;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;


/**
 * NotificationAdmain辅助类
 */
public class DeleteService extends IntentService {

	public DeleteService() {
		super("");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i("LOG", "===========deleteService");
	}

}
