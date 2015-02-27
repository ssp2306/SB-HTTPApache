package com.stephensparker.sb_httpapache;


        import java.io.IOException;
        import org.apache.http.HttpResponse;
        import org.apache.http.client.ClientProtocolException;
        import org.apache.http.client.HttpClient;
        import org.apache.http.client.methods.HttpPost;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.apache.http.util.EntityUtils;
        import android.app.Activity;
        import android.os.*;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;

        import android.os.Bundle;
        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

public class MainActivity extends Activity {

    private Button buttonOpenURL, buttonClear;
    TextView txtViewParsedValue;
    private JSONObject jsonObject;

    String str = "";
    String strParsedValue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // find the ID of the TextView
        txtViewParsedValue = (TextView) findViewById(R.id.editTextURLOutput);
        buttonOpenURL = (Button) findViewById(R.id.buttonOpenURL);
        buttonClear = (Button) findViewById(R.id.buttonClear);

        addListenerOnClickOpenURL();
        addListenerOnClickClear();

    }

    public void addListenerOnClickClear() {

        buttonClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // clear the output view
                txtViewParsedValue.setText(null);
            }

        });

    }

    public void addListenerOnClickOpenURL() {

        //Select a specific button to bundle it with the action you want

        buttonOpenURL.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // grab values from the input fields and pass them to the output text
                EditText editURL = (EditText) findViewById(R.id.editURL);
                String eURL = editURL.getText().toString();
                HttpClient myClient = new DefaultHttpClient();
                HttpPost myConnection = new HttpPost(eURL);
                HttpResponse response;
                try {

                    response = myClient.execute(myConnection);
                    str = EntityUtils.toString(response.getEntity(), "UTF-8");
                    //strParsedValue = "Opening..." + eURL + "\n";
                    //txtViewParsedValue.setText(strParsedValue);

                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                    txtViewParsedValue.setText(e.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    txtViewParsedValue.setText(e.toString());
                }

                try{

                    jsonObject = new JSONObject(str);

                    JSONObject objectFamily = jsonObject.getJSONObject("FamilyName");

                    // get the family name from the JSON object
                    strParsedValue = "Meet the " + objectFamily.getString("Family") + " Clan:\n";

                    /// create a sub array from the JSON string
                    JSONObject subObject = objectFamily.getJSONObject("sub");
                    JSONArray subArray = subObject.getJSONArray("subdata");

                    strParsedValue += "There are " + subArray.length() + " people in the family.\n\n";

                    for (int i = 0; i < subArray.length(); i++) {
                        // get the info on each family member
                        strParsedValue += subArray.getJSONObject(i).getString("name");
                        strParsedValue +=" is " + subArray.getJSONObject(i).getString("age");
                        strParsedValue += subArray.getJSONObject(i).getString("gender");
                        strParsedValue += " loves " + subArray.getJSONObject(i).getString("favcolor") + ".\n";
                    }
                    strParsedValue += "\nJSON file we read:\n" + str;
                    txtViewParsedValue.setText(strParsedValue);
                } catch ( JSONException e) {
                    e.printStackTrace();
                    txtViewParsedValue.setText(e.toString());
                }

//
//            private void readStream(InputStream in) {
//                BufferedReader reader = null;
//                try {
//
//                    reader = new BufferedReader(new InputStreamReader(in));
//                    String line = "";
//                    while ((line = reader.readLine()) != null) {
//                        //System.out.println(line);
//                        response += line;
//
//
//                    }
//                    //txtViewParsedValue.setText(response);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    response = "Something Wrong " + e.toString();
//                    txtViewParsedValue.setText(response);
//                } finally {
//                    if (reader != null) {
//                        try {
//                            reader.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            response = "Something Wrong " + e.toString();
//                            txtViewParsedValue.setText(response);
//                        }
//                    }
//                }
//            }


            }

        });

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


