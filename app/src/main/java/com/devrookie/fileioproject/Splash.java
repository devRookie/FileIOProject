package com.devrookie.fileioproject;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final ImageView logo = (ImageView)findViewById(R.id.logo_splash);
        final Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_anim);
        logo.startAnimation(fadeIn);
//**Start thread
        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(500);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent gameMenu = new Intent ("com.devrookie.fileioproject.MAINACTIVITY2");
                    startActivity(gameMenu);
                }//finally
            }//run+-
        };//Thread
        timer.start();
    }//onCreate
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }//onPause
}