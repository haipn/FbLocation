package com.eddy.fblocation;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.entities.Profile.Properties;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnProfileListener;

public class MainActivity extends Activity {

	protected static final String NAME = "name";
	protected static final String BIRTHDAY = "birthday";
	private Button mBtnLogin;
	private SimpleFacebook mSimpleFacebook;
	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"com.eddy.fblocation", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("KeyHash:",
						Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		mSimpleFacebook = SimpleFacebook.getInstance(this);
		mProgressDialog = new ProgressDialog(this);
		mBtnLogin = (Button) findViewById(R.id.btnLogin);
		setLoginFacebook();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mSimpleFacebook = SimpleFacebook.getInstance(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
	}
	/**
	 * Login facebook.
	 */
	private void setLoginFacebook() {
		// Login listener
		final OnLoginListener onLoginListener = new OnLoginListener() {

			@Override
			public void onFail(String reason) {
				// mTextStatus.setText(reason);
				Log.w("haipn", "Failed to login");
				// mProgressDialog.dismiss();
			}

			@Override
			public void onException(Throwable throwable) {
				// mTextStatus.setText("Exception: " + throwable.getMessage());
				Log.e("haipn", "Bad thing happened", throwable);
				// mProgressDialog.dismiss();
			}

			@Override
			public void onThinking() {
				Log.w("haipn", "Thinking....");
				// mProgressDialog.show();
			}

			@Override
			public void onLogin() {
				// loggedInUIState();
				mProgressDialog.dismiss();
				Log.d("haipn", "login fb");
				getProfileFb();
			}

			@Override
			public void onNotAcceptingPermissions(Permission.Type type) {
				Log.d("haipn",
						String.format("You didn't accept %s permissions",
								type.name()));
			}
		};

		mBtnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mProgressDialog.show();
				mSimpleFacebook.login(onLoginListener);
			}
		});
	}
	
	private void getProfileFb() {
		mProgressDialog.show();
		Profile.Properties properties = new Profile.Properties.Builder()
				.add(Properties.ID).add(Properties.FIRST_NAME)
				.add(Properties.BIRTHDAY).add(Properties.LAST_NAME)
				.add(Properties.EMAIL).add(Properties.COVER)
				.add(Properties.WORK).add(Properties.EDUCATION).build();
		mSimpleFacebook.getProfile(properties, onProfileListener);
	}

	OnProfileListener onProfileListener = new OnProfileListener() {
		@Override
		public void onComplete(Profile profile) {
			Log.i("haipn", "My profile id = " + profile.getId() + ", "
					+ profile.getEmail() + ","
					+ mSimpleFacebook.getSession().getAccessToken());
			mProgressDialog.dismiss();
			Intent i = new Intent(MainActivity.this, ConfirmActivity.class);
			i.putExtra(NAME, profile.getFirstName() + " " + profile.getLastName());
			i.putExtra(BIRTHDAY, profile.getBirthday());
			startActivity(i);
			finish();
		}
	};
}
