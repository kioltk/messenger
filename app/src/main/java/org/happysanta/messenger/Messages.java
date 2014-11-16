package org.happysanta.messenger;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Messages extends Activity {

    String[] messagesInOut = {"message.in", "message.out"};
    int flags = 0;
    boolean messageIn = true;
    boolean messageOut = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_message);

        ListView lvMessages = (ListView) findViewById(R.id.item_message);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.fragment_messages, messagesInOut);

        lvMessages.setAdapter(adapter);

        if (messageIn) {
            flags = 1;
        } else if (messageOut) {
            flags = 0;
        }


    }
}