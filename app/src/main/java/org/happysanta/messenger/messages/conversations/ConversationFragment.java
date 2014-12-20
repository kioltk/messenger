package org.happysanta.messenger.messages.conversations;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiMessages;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.KeyboardUtil;
import org.happysanta.messenger.Photo.MakePhotoFragment;
import org.happysanta.messenger.R;
import org.happysanta.messenger.messages.ChatActivity;
import org.happysanta.messenger.messages.core.MessagesAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class ConversationFragment extends Fragment {

    public ConversationFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        final ListView messagesList = (ListView) rootView.findViewById(R.id.messages_list);
        final TextView statusView = (TextView) rootView.findViewById(R.id.status);
        final EditText box = (EditText) rootView.findViewById(R.id.send_box);
        int userId = getArguments().getInt(ChatActivity.ARG_USERID, 0);

        Button photoButton = (Button) rootView.findViewById(R.id.make_photo_camera);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                View photoView = inflater.inflate(R.layout.dialog_fast_photo, null);

                AlertDialog photoDialog = new AlertDialog.Builder(getActivity())
                        .setView(photoView)
                        .create();
                photoDialog.show();
                getFragmentManager().beginTransaction()
                        .replace(R.id.extra_container, new MakePhotoFragment())
                        .commit();




                KeyboardUtil.hide(box, getActivity());

            }
        });

        statusView.setText("loading");

        new VKApiMessages().getHistory(userId).executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                VKList<VKApiMessage> messages = (VKList<VKApiMessage>) response.parsedModel;
                messagesList.setAdapter(new MessagesAdapter(getActivity(), messages.toArrayList()));
                if(messages.isEmpty()){
                    statusView.setText("Start the conversation");
                }else{
                    statusView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(VKError error) {
                statusView.setText(error.toString());

            }
        });




        return rootView;
    }

    public static ConversationFragment getInstance(Bundle extras) {
        ConversationFragment fragment = new ConversationFragment();
        fragment.setArguments(extras);
        return fragment;
    }
    public static ConversationFragment getInstance(int userId){
        Bundle bundle = new Bundle();
        bundle.putInt(ChatActivity.ARG_USERID, userId);
        return getInstance(bundle);
    }
}
