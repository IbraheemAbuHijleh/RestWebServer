package com.example.php_example_one;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {
    private EditText edt;
    private EditText edt2;
    private EditText edt3;
    private Button btn;

    private RequestQueue re;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        edt = findViewById(R.id.edttext);
        edt2 = findViewById(R.id.editTextText2);
        edt3 = findViewById(R.id.editTextText3);
        btn = findViewById(R.id.button);
        re = Volley.newRequestQueue(AddActivity.this);
        Intent i = getIntent();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String I = edt.getText().toString().trim();
                String cat = edt2.getText().toString().trim();
                String pag = edt3.getText().toString().trim();
                String URL = "http://10.0.2.2:80/Rest/post.php";

                StringRequest o = new StringRequest(Request.Method.POST, URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Log the response for debugging
                                Log.d("Response", response);
                                try {
                                    JSONObject o = new JSONObject(response);
                                    // Check if "message" key exists in the JSON response
                                    if (o.has("message")) {
                                        String message = o.getString("message");
                                        Toast.makeText(AddActivity.this, message, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AddActivity.this, "Key 'message' not found in response", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(AddActivity.this, "Response parsing error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Volley", "Error: " + error.toString());
                                Toast.makeText(AddActivity.this, "Request error", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("title", I);
                        params.put("cat", cat);
                        params.put("Pag", pag);
                        return params;
                    }
                };


                re.add(o);

            }
        });
    }
}
