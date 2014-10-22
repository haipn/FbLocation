package com.eddy.fblocation;

import android.app.Application;

import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.sromku.simple.fb.utils.Logger;

/**
 * 
 * @author haipn
 * 
 */
public class MyApplication extends Application {
	private static final String APP_NAMESPACE = "haipnlogin";

	@Override
	public void onCreate() {
		super.onCreate();

		// set log to true
		Logger.DEBUG_WITH_STACKTRACE = true;

		// initialize facebook configuration
		Permission[] permissions = new Permission[] { Permission.EMAIL,
				Permission.USER_FRIENDS, Permission.USER_BIRTHDAY,
				Permission.USER_LOCATION };
		SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
				.setAppId(getString(R.string.app_id))
				.setNamespace(APP_NAMESPACE).setPermissions(permissions)
				.setAskForAllPermissionsAtOnce(false).build();

		SimpleFacebook.setConfiguration(configuration);
	}
}
