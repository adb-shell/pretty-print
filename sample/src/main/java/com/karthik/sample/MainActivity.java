package com.karthik.sample;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.karthik.pretty_annotation.Pretty;

@Pretty(headerName = "MainActivity")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Bundle bundle = new Bundle();
        bundle.putString("hello","text");
        bundle.putString("world","text");
        bundle.putInt("number",2);
        PrettyPrinterExtras.MainActivityprintExtras(bundle);
        findViewById(R.id.clickActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                intent.putExtra("string","string value");
                intent.putExtra("int",12);
                intent.putExtra("float",12.f);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        findViewById(R.id.clickFragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSample sample = new FragmentSample();
                sample.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction =
                        fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container,sample);
                fragmentTransaction.addToBackStack("frag");
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();
        }else{
            super.onBackPressed();
        }
    }
}
