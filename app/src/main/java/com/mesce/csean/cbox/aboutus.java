package com.mesce.csean.cbox;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.widget.ImageView;
import android.widget.VideoView;

public class aboutus extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        ImageView imageView=(ImageView)findViewById(R.id.image);
        imageView.setImageResource(R.mipmap.log2);



    }

}
