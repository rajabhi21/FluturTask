package abhi.example.com.fluturtask;

import android.app.DownloadManager;
import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import static abhi.example.com.fluturtask.SplashActivity.mp;

public class MainActivity extends AppCompatActivity {

    public static int position;
    public String urlr="https://api.genderize.io/?name=",url;

    TextView prediction;
    EditText name;
    InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_main);

        if(position!=0){
            mp.start();
            mp.seekTo(position);
        }

        name = (EditText)findViewById(R.id.name);
        prediction = (TextView)findViewById(R.id.prediction);
        final ImageView button = (ImageView)findViewById(R.id.button);
        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        name.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    hideKeybord(v);
                    button.performClick();
                    return true;
                }
                return false;
            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeybord(v);

                prediction.setVisibility(View.GONE);

                String text= name.getText().toString();
                String []names = text.split(" ");
                String nameOfUser = names[0].toLowerCase();

                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

                url = urlr+nameOfUser.toLowerCase();

                downloadTask(requestQueue);



            }
        });



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

    public void downloadTask(RequestQueue requestQueue){


        JsonObjectRequest objectRequest=new JsonObjectRequest(Request.Method.GET, this.url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    String genderOfUser = response.getString("gender");

                    double pro = response.getDouble("probability");

                    String probab=Double.toString(pro);

                    if(pro==1.0||pro==0.0)
                        probab=Integer.toString((int)pro);

                    if(genderOfUser.equals("female"))
                        genderOfUser = "girl";
                    else if(genderOfUser.equals("male"))
                        genderOfUser = "boy";

                    prediction.setText(genderOfUser.toUpperCase()+"\n"+"PROBABILITY: "+probab);

                    prediction.setVisibility(View.VISIBLE);





                }
                catch (JSONException e) {
                    e.printStackTrace();
                    prediction.setText("SORRY\nNO PREDICTION");
                    prediction.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(objectRequest);

    }

    public void hideKeybord(View view) {
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

}
