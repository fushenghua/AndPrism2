package org.prism.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.prism.annotation.Protocol;
import org.prism.protocol.PrismProtocol;

import demo.prism.org.aninterface.IBoostEngine;

@Protocol("mactivity")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_class_sub).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrismProtocol.create(IBoostEngine.class).sayHello("hahaha Mainactivity");
            }
        });
    }
}
