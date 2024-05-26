package com.example.php_example_one;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.PixelCopy;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private Button btnopen;
    private Button btrserche;
    private ListView LIST;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.edttext);
        btrserche = findViewById(R.id.btnserche);
        btnopen = findViewById(R.id.btn);
        requestQueue = Volley.newRequestQueue(this);
        LIST = findViewById(R.id.LIST);
    }

    public void btnserche(View view) {

        String TITLE = editText.getText().toString().trim();

        String URL = "http://10.0.2.2:80/Rest/info.php?cat=" + TITLE;

        JsonArrayRequest R = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                ArrayList<String> list = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {  // Use response.length() instead of list.size()
                    try {
                        JSONObject o = response.getJSONObject(i);
                        list.add(o.getString("title"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, list);
                LIST.setAdapter(adapter);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error: " + error.toString());
                    }
                });

        requestQueue.add(R);
    }

    public void btnopen(View view) {

        Intent i = new Intent(MainActivity.this, AddActivity.class);

        startActivity(i);
    }

    public void btndelete(View view) {
        String TITLE = editText.getText().toString().trim();
        String URL = "http://10.0.2.2:80/Rest/DELE.php?cat=" + TITLE;

        StringRequest R = new StringRequest(Request.Method.DELETE, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Handle the response here
                Log.i("Volley", "Response: " + response);
                Toast.makeText(getApplicationContext(), "Record deleted successfully", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", "Error: " + error.toString());
                Toast.makeText(getApplicationContext(), "Error deleting record", Toast.LENGTH_SHORT).show();
            }
        });

        // Instantiate the RequestQueue.

        // Add the request to the RequestQueue.
        requestQueue.add(R);
    }
}
