package com.sami_ofer.samioferproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.sami_ofer.samioferproject.parse.ParseSubscription;

public class MainActivity extends AppCompatActivity {
    private EditText emailEditText;
    private TextView currentSubscriptionText;
    private SharedPreferences userSettings;

    private static final String TAG = "MyActivity";
    private static final String USER_SETTINGS = "user_settings";
    private static final String SUBSCRIPTIONS = "Subscription";
    private static final String EMAIL_COLUMN = "Email";

    private static final String IS_USER_SUBSCRIBED = "isSubscribed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(ParseSubscription.class);
        Parse.initialize(this, "1NUErTeg8LzHfGo5pLiuaOAnh6YYoOnAdMbzfMmK", "W0tr12z5hSWy5zeGicaJ6IQUa35XnwXpKoB1bdPv");

        // Restore preferences
        userSettings = getSharedPreferences(USER_SETTINGS, 0);

        emailEditText = (EditText) findViewById(R.id.emailEditText);
        currentSubscriptionText = (TextView) findViewById(R.id.currentSubscriptionText);

        setGUI();

//        findViewById(R.id.subscribeButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final String email = emailEditText.getText().toString();
//                if (!checkIfValidEmail(email)) {
//                    Log.w(TAG, String.format("Email is not valid: %s", email));
//                    return;
//                }
//                // TODO: In the future, send a validation to check the e-mail
//                subscribeEmail(email);
//            }
//        });

        findViewById(R.id.subscribeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailEditText.getText().toString();
                subscribeEmail(email);
            }
        });

        findViewById(R.id.unsubscribeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unsubscribe();
            }
        });

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



    private void setGUI() {
        setSubscribedEmailText();
    }

    private void setSubscribedEmailText() {
        if (userSettings.getBoolean(IS_USER_SUBSCRIBED, false)) {
            currentSubscriptionText.setText("Subscribed email:\n" + Utils.readPref(this, "email"));
        }
    }

    private void subscribeEmail(final String email) {
        // TODO: potential bug - subscribing and unsubscribing at the same time
        //unsubscribe();
        ParseSubscription.fetchCurrentQuery(email).countInBackground(new CountCallback() {
            @Override
            public void done(int count, ParseException e) {
                if (count == 0) {
                    ParseSubscription sub = new ParseSubscription();
                    if(!sub.setEmail(email)) {
                        Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    sub.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Utils.writePref(getApplicationContext(), "email", email);
                            Toast.makeText(getApplicationContext(), "Subscribed!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Already subscribed", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        ParseObject parseObject = new ParseObject(SUBSCRIPTIONS);
//        parseObject.put(EMAIL_COLUMN, email);
//        parseObject.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                SharedPreferences.Editor editor = userSettings.edit();
//                editor.putBoolean(IS_USER_SUBSCRIBED, true);
//                editor.putString("email", email);
//                editor.commit();
//            }
//        });


    }

    private void unsubscribe() {
//        if (userSettings.getBoolean(IS_USER_SUBSCRIBED, false)) {
//            Log.i(TAG, "User is not subscribed and trying to unsubscribe");
//            return;
//        }
        // TODO: Learn how to remove a row from parse
        // TODO: potential bug - subscribing and unsubscribing at the same time

        ParseQuery<ParseSubscription> query = ParseSubscription.fetchCurrentQuery(Utils.readPref(this, "email"));

        query.getFirstInBackground(new GetCallback<ParseSubscription>() {
            @Override
            public void done(ParseSubscription object, ParseException e) {
                object.deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(getApplicationContext(), "Unsubscribed!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

//        ParseObject parseObject = new ParseObject(SUBSCRIPTIONS);
//        parseObject.remove(userSettings.getString("email", ""));
//        parseObject.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                SharedPreferences.Editor editor = userSettings.edit();
//                editor.putBoolean(IS_USER_SUBSCRIBED, false);
//                editor.putString("email", "");
//                editor.commit();
//            }
//        });
    }
}
