package com.example.enchanterswapna.geneotp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import swarajsaaj.smscodereader.interfaces.OTPListener;
import swarajsaaj.smscodereader.receivers.OtpReader;

public class NextActivity extends AppCompatActivity implements OTPListener {

    EditText ed1;
    Button bt1;
    String stnum,stotp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        ed1=(EditText)findViewById(R.id.editText);
        bt1=(Button)findViewById(R.id.button1);
        OtpReader.bind(NextActivity.this,"SWAPNA");
        Intent myint=getIntent();
        stnum=myint.getStringExtra("stname");

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stotp=ed1.getText().toString();
                if(stotp.equals("")){
                    Toast.makeText(getApplicationContext(),"Please Enter",Toast.LENGTH_SHORT).show();
                }
                else
                   insert_service(stnum,stotp);
            }
        });
    }
    private void insert_service(final String usernum,final String userotp) {

        StringRequest stringreqs = new StringRequest(Request.Method.POST, Global_Url.LOGIN_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean abc = jObj.getBoolean("exits");
                    if (abc)
                    {
                        JSONObject users = jObj.getJSONObject("user_det");
                        String numb1=users.getString("numbr");
                        String name = users.getString("otp");
                        //Toast.makeText(getApplicationContext(), "User Exists already with  mobile number!", Toast.LENGTH_SHORT).show();
                        //String maill = users.getString("password");
                        // String stname = users.getString("studentname");

                        Intent intent=new Intent(NextActivity.this,SecondActivity.class);
                        intent.putExtra("otp", name);
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
                uandme.put("usernumm", usernum);
                uandme.put("otp",userotp);
                //uandme.put("password2", password1);
                return uandme;
            }
        };
        AppController.getInstance().addToRequestQueue(stringreqs);

    }

    @Override
    public void otpReceived(String messageText) {
        ed1.setText(messageText);
    }
}
