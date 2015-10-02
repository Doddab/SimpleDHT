package edu.buffalo.cse.cse486586.simpledht;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;

import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.widget.TextView;



public class SimpleDhtActivity extends Activity {

    public static TextView DHTProvview;
    //DHTProvview=(TextView) findViewById(R.id.textView1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_dht_main);

        TextView tv = (TextView) findViewById(R.id.textView1);
        DHTProvview=(TextView) findViewById(R.id.textView1);
        tv.setMovementMethod(new ScrollingMovementMethod());

        // To implement the button 3 logic - Test button
        findViewById(R.id.button3).setOnClickListener(
                new OnTestClickListener(tv, getContentResolver()));
        //To check for Insert success and query success messages when clicked

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_simple_dht_main, menu);
        return true;
    }
}
