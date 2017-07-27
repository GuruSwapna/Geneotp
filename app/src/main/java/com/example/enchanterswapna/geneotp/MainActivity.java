package com.example.enchanterswapna.geneotp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity  {

    TextView txtnum;
    Button btsnd;
    String ssnd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtnum=(TextView)findViewById(R.id.txt1);
        btsnd=(Button)findViewById(R.id.btn1);
        //OtpReader.bind(this,"SENDER_NUM_HERE");
        btsnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ssnd=txtnum.getText().toString();
                insert_service(ssnd);
            }
        });

    }
//    @Override
//    public void otpReceived(String smsText) {
//        //Do whatever you want to do with the text
//        Toast.makeText(this,"Got "+smsText,Toast.LENGTH_LONG).show();
//        Log.d("Otp",smsText);
//    }


    private void insert_service(final String user1) {

        StringRequest stringreqs = new StringRequest(Request.Method.POST, Global_Url.OTP_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean abc = jObj.getBoolean("exits");
                    if (abc)
                    {
                        JSONObject users = jObj.getJSONObject("user_det");
                        String name = users.getString("numbr");
                        //Toast.makeText(getApplicationContext(), "User Exists already with  mobile number!", Toast.LENGTH_SHORT).show();
                        //String maill = users.getString("password");
                       // String stname = users.getString("studentname");

                        Intent intent=new Intent(MainActivity.this,NextActivity.class);
                        intent.putExtra("stname", name);
                        startActivity(intent);
                    }
                    else
                    {
                        //String msg=jObj.getString("messeade");
                        Toast.makeText(getApplicationContext(), "not valid", Toast.LENGTH_SHORT).show();
                    }
                   /*Intent intent = new Intent(SignUp_Screen.this, Home_Screen.class);
                   intent.putExtra("name", name);
                   intent.putExtra("mail", mail);
                   intent.putExtra("uuidq", uuidq);
                   startActivity(intent);
                   Toast.makeText(getApplicationContext(), "Welcome" + name, Toast.LENGTH_SHORT).show();
*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "INTERNET CONNECTION NOT AVAILABLE", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> uandme = new HashMap<String, String>();
                uandme.put("usernum", user1);
                //uandme.put("password2", password1);
                return uandme;
            }
        };
        AppController.getInstance().addToRequestQueue(stringreqs);

    }
}
