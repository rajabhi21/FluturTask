package abhi.example.com.fluturtask;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import static abhi.example.com.fluturtask.MainActivity.position;

public class SplashActivity extends AppCompatActivity {

    public static MediaPlayer mp;
    String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_splash);

        if(mp==null) {
            mp = MediaPlayer.create(this, R.raw.xyz);
            mp.setLooping(true);
            mp.start();
        }
        else{
            mp.start();
            mp.seekTo(position);
        }

        final TextView w = (TextView) findViewById(R.id.w);
        TextView e1 = (TextView) findViewById(R.id.e1);
        TextView l = (TextView) findViewById(R.id.l);
        TextView c = (TextView) findViewById(R.id.c);
        TextView o = (TextView) findViewById(R.id.o);
        TextView m = (TextView) findViewById(R.id.m);
        TextView e2 = (TextView) findViewById(R.id.e2);

        {
            w.clearAnimation();

            TranslateAnimation transAnim = new TranslateAnimation(0, 0,
                    0, getDisplayHeight()/2);
            transAnim.setStartOffset(500);
            transAnim.setDuration(3000);
            transAnim.setFillAfter(true);
            transAnim.setInterpolator(new BounceInterpolator());
            transAnim.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    //Log.i(TAG, "Starting button dropdown animation");

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    if(TAG.equals("e2")){
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent homeIntent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(homeIntent);
                        position = mp.getCurrentPosition();
                        finish();
                    }
                }
            });





            w.startAnimation(transAnim);
            c.startAnimation(transAnim);
            e1.startAnimation(transAnim);
            o.startAnimation(transAnim);
            l.startAnimation(transAnim);
            m.startAnimation(transAnim);
            TAG="e2";
            e2.startAnimation(transAnim);


        }

    }

    private int getDisplayHeight() {

        return this.getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.pause();
        position=mp.getCurrentPosition();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mp.start();
        mp.seekTo(position);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        position = mp.getCurrentPosition();
    }
}
