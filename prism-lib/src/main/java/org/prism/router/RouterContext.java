package org.prism.router;

import android.content.Context;
import android.content.Intent;

/**
 * Created by fushenghua on 2017/1/14.
 */

public class RouterContext {

    private Intent intent;
    private Context context;
    private String uri;

    public static RouterContext builder() {
        return new RouterContext();
    }

    public RouterContext() {

    }

    RouterContext(Context context) {
        this.context = context;
    }


    public Intent getIntent() {
        return intent;
    }

    public RouterContext setIntent(Intent intent) {
        this.intent = intent;
        return this;
    }

    public Context getContext() {
        return context;
    }

    public RouterContext setContext(Context context) {
        this.context = context;
        return this;
    }

    public RouterContext setUri(String uri) {
        this.uri = uri;
        return this;
    }

    public String getUri() {
        return uri;
    }
}
