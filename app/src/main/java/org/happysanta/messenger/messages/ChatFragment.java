package org.happysanta.messenger.messages;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.happysanta.messenger.R;
import org.happysanta.messenger.messages.core.Message;
import org.happysanta.messenger.messages.core.MessagesAdapter;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChatFragment extends Fragment {

    public ChatFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        ArrayList<Message> messages = new ArrayList<Message>();
        messages.add(new Message(){{ text ="Привет."; in = true; }});
        messages.add(new Message(){{ text ="Здарова!"; in = false; }});
        messages.add(new Message(){{ text ="Как дела?."; in = true; }});
        messages.add(new Message(){{ text ="Збс."; in = false; }});

        ListView messagesList = (ListView) rootView.findViewById(R.id.messages_list);
        messagesList.setAdapter(new MessagesAdapter(getActivity(), messages));

        return rootView;
    }
}
