package org.happysanta.messenger.sandbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vk.sdk.api.model.VKApiUserFull;

import org.happysanta.messenger.R;
import org.happysanta.messenger.chatheads.ChatHeadsManager;
import org.happysanta.messenger.core.BaseFragment;
import org.happysanta.messenger.messages.ChatActivity;

/**
 * Created by Jesus Christ. Amen.
 */
public class SandboxFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sandbox, null);

        findViewById(R.id.show_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(ChatActivity.ARG_DIALOGID, 51916034);
                bundle.putBoolean(ChatActivity.ARG_ISCHAT, false);
                bundle.putString(ChatActivity.ARG_TITLE, "Korol");
                bundle.putString(ChatActivity.ARG_SUBTITLE, "Sandbox");
                bundle.putString(ChatActivity.ARG_LOGO, null);
                SparseArray<VKApiUserFull> sparseArray = new SparseArray<>();
                sparseArray.put(51916034, new VKApiUserFull(){{ id = 51916034; first_name = "Korol"; last_name = "ololo"; photo_200 = ""; }});
                bundle.putSparseParcelableArray(ChatActivity.ARG_CHAT_PARTICIPANTS, sparseArray);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
        findViewById(R.id.add_chathead).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ChatHeadsManager.showBackground();
                ChatHeadsManager.showChatHeads();
            }
        });
        return rootView;
    }

}
