package demo.prism.org.test_submodule1;

import android.os.Bundle;
import android.util.Log;

import org.prism.annotation.Protocol;

import demo.prism.org.aninterface.IBoostEngine;


/**
 * Created by fushenghua on 2017/1/15.
 */

@Protocol("BoostEngine")
public class BoostEngine implements IBoostEngine {


    @Override
    public void sayHello(String msg) {

        Log.e("he", msg);
    }

    @Override
    public void sayHello(Bundle bundle) {

        Log.e("he", bundle.getString("key"));
    }
}
