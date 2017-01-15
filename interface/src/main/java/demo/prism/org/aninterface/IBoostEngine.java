package demo.prism.org.aninterface;

import android.os.Bundle;

import org.prism.annotation.Caller;

/**
 * Created by fushenghua on 2017/1/15.
 */

@Caller("sayHello")
public interface IBoostEngine {

    void sayHello(String msg);

    void sayHello(Bundle bundle);
}
