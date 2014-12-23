package org.happysanta.messenger.messages;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiMessages;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKApiMessage;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseActivity;
import org.happysanta.messenger.core.util.Dimen;
import org.happysanta.messenger.messages.conversations.ConversationFragment;

public class ChatActivity extends BaseActivity {

    public static final String ARG_DIALOGID = "arg_dialogid";
    public static final java.lang.String ARG_ISCHAT = "arg_ischat";
    private static final String ARG_TITLE = "arg_title";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        setContentView(R.layout.activity_chat);





        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        if (null != actionbar) {
            actionbar.setHomeButtonEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            ((Toolbar)findViewById(R.id.toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }


        final Button btnSend = (Button) findViewById(R.id.send_button);
        final EditText editMessageText = (EditText) findViewById(R.id.message_box);

        Bundle bundle = getIntent().getExtras();
        final int userId = bundle.getInt(ARG_DIALOGID);
        String title = bundle.getString(ARG_TITLE,"Dialog");
        setTitle(title);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, ConversationFragment.getInstance(bundle))
                    .commit();
        }
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKApiMessage message = new VKApiMessage();
                message.user_id = userId;
                message.body = editMessageText.getText().toString();
                VKRequest request = new VKApiMessages().send(message);
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        editMessageText.setText(null);
                    }

                    @Override
                    public void onError(VKError error) {
                        super.onError(error);
                    }
                });
            }});
    }


    public static Intent getActivityIntent(Context context, VKApiDialog dialog) {
        Intent intent = new Intent(context, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_DIALOGID, dialog.getId());
        bundle.putBoolean(ARG_ISCHAT, dialog.isChat());
        bundle.putString(ARG_TITLE, dialog.title);
        intent.putExtras(bundle);
        return intent;
    }
}
