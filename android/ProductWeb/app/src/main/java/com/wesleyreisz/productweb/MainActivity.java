package com.wesleyreisz.productweb;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public final static String apiURL = "http://reisz-web-api.azurewebsites.net/api/product";

    private List<Product> _productItems;
    private ListView _listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        _listView = (ListView)findViewById(R.id.listViewContent);

        //setup listview click event
        ListView listView = (ListView)findViewById(R.id.listViewContent);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(_productItems!=null){
                    TextView textView = (TextView)findViewById(R.id.textView);
                    Product product = _productItems.get(position);
                    textView.setText(product.getName() + " " + product.getCategory() + " $" +product.getPrice());
                }else{
                    new CallAPI().execute();
                }
            }
        });

        //call AsyncTask
        new CallAPI().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            new CallAPI().execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected class CallAPI extends AsyncTask<Void, Void, List<Product>> {
        @Override
        protected List<Product> doInBackground(Void... params) {
            List<Product> products = new ArrayList<Product>();

            try {
                URL url = new URL(apiURL);
                URLConnection urlc = url.openConnection();
                BufferedReader bfr = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
                String line;

                while ((line = bfr.readLine()) != null) {
                    Product product = null;
                    JSONArray jsa = new JSONArray(line);
                    for (int i = 0; i < jsa.length(); i++) {
                        JSONObject jo = (JSONObject) jsa.get(i);
                        product = new Product();
                        product.setId(jo.getInt("Id"));
                        product.setName(jo.getString("Name"));
                        product.setCategory(jo.getString("Category"));
                        product.setPrice(jo.getDouble("Price"));

                        products.add(product);
                    }

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return products;
        }

        protected void onPostExecute(List<Product> products) {
            _productItems = products;

            Log.d("test", "results: " + products.toString());
            List<String>names = new ArrayList<String>();
            for(Product product : products){
                names.add(product.getName());
            }
            ArrayAdapter<String> namesAdapter =
                    new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, names);
            _listView.setAdapter(namesAdapter);
        }

    }
}
