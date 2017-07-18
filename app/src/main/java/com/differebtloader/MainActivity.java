package com.differebtloader;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.differebtloader.databinding.ActivityMainBinding;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    TwitterAuthClient mTwitterAuthClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        if(session!=null){
            binding.tvResult.setText(session.getUserName()+"\n"+session.getUserId());
        }
        mTwitterAuthClient= new TwitterAuthClient();

        binding.btnLogin.setOnClickListener(v -> {
            loginToTwitter();
        });

        binding.btnEmail.setOnClickListener(v -> {
            getEmail();
        });

        binding.btnLogout.setOnClickListener(v -> {
            logoutFromTwitter();
        });

    }

    private void logoutFromTwitter() {
        TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
        if (twitterSession != null) {
            TwitterAuthClient authClient = new TwitterAuthClient();
            authClient.cancelAuthorize();
            TwitterCore.getInstance().getSessionManager().clearActiveSession();
            binding.tvResult.setText("");
        }
    }




    private void getEmail() {
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        TwitterAuthToken authToken = session.getAuthToken();
        String token = authToken.token;
        String secret = authToken.secret;
        TwitterAuthClient authClient = new TwitterAuthClient();
        authClient.requestEmail(session, new Callback<String>() {
            @Override
            public void success(Result<String> result) {
               String text= binding.tvResult.getText().toString();
                binding.tvResult.setText(text+"\n"+result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
            }
        });
    }

    private void loginToTwitter() {
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();

        if(session==null) {
            mTwitterAuthClient.authorize(this, new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    binding.tvResult.setText(result.data.getUserName()+"\n"+result.data.getUserId());
                }

                @Override
                public void failure(TwitterException exception) {

                }
            });
        }else {
            binding.tvResult.setText(session.getUserName()+"\n"+session.getUserId());
            Toast.makeText(this,"All ready login",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result to the login button.
        mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
    }
}
