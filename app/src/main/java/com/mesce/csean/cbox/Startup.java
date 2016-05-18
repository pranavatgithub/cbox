package com.mesce.csean.cbox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Startup extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
    }

    public void show(View view) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();

        EditText editText=(EditText)findViewById(R.id.phonenumber);
        String phonenumber1=editText.getText().toString();
        editor.putString("key_name1", phonenumber1);

        editor.putString("key_name2","set");
        editor.commit();

        String test=pref.getString("key_name1",null);
        Toast.makeText(getApplicationContext(), test, Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(view.getContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }
}
