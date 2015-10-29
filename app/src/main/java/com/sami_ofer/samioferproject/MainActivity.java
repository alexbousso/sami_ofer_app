package com.sami_ofer.samioferproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private TextView testTextView;

    private static final String TAG = "MyActivity";

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient googleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "1NUErTeg8LzHfGo5pLiuaOAnh6YYoOnAdMbzfMmK", "W0tr12z5hSWy5zeGicaJ6IQUa35XnwXpKoB1bdPv");

        // Initialize Google API Client
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .build();

        testTextView = (TextView) findViewById(R.id.textView);
        testTextView.setText("Test area:\n");

//        ParseObject testObject = new ParseObject("TestObject");
//        testObject.put("name", "Alex Bousso");
//        testObject.put("email", "alexbousso@gmail.com");
//        testObject.saveInBackground();
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("TestObject");
//        query.whereEqualTo("name", "Alex Bousso");
//        query.findInBackground(new FindCallback<ParseObject>() {
//            private TextView textView;
//
//            @Override
//            public void done(List<ParseObject> objects, ParseException e) {
//                textView = (TextView) findViewById(R.id.textView);
//                textView.setText("Found:\n");
//                if (e == null) {
//                    for (ParseObject object : objects) {
//                        textView.append(String.format("%s: %s\n", object.getString("name"), object.getString("email")));
//                    }
//                } else {
//                    textView.setText(String.format("ERROR! Got e = %d", e.getCode()));
//                }
//            }
//        });
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

    @Override
    protected void onStart() {
        super.onStart();

        Log.i(TAG, "Connecting to Google...");
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Connection established.");
        testTextView.append("Got here!\n");
        testTextView.append(String.format("Plus.PeopleApi.getCurrentPerson(googleApiClient).getId() = %s\n", Plus.PeopleApi.getCurrentPerson(googleApiClient).getId()));
        testTextView.append(String.format("Plus.PeopleApi.getCurrentPerson(googleApiClient).getName() = %s\n", Plus.PeopleApi.getCurrentPerson(googleApiClient).getName()));
        testTextView.append(String.format("Plus.PeopleApi.getCurrentPerson(googleApiClient).getAccountName() = %s\n", Plus.AccountApi.getAccountName(googleApiClient)));
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Connection failed: " + connectionResult.getErrorMessage());
    }
}
