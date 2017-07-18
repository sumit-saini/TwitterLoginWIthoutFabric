package com.differebtloader;

import android.app.Application;
import android.util.Log;

import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import static com.twitter.sdk.android.core.Twitter.initialize;

/**
 * Created by mobua05 on 18/7/17.
 */

public class TwitterApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initialize(this);
        TwitterAuthConfig twitterAuthConfig= new TwitterAuthConfig("CONSUMER_KEY", "CONSUMER_SECRET");
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(twitterAuthConfig)
                .debug(true)
                .build();
        initialize(config);
    }
}
