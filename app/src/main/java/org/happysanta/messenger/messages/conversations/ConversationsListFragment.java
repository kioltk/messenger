package org.happysanta.messenger.messages.conversations;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiMessages;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.util.BitmapUtil;
import org.happysanta.messenger.core.util.ImageUtil;
import org.happysanta.messenger.longpoll.LongpollService;
import org.happysanta.messenger.longpoll.listeners.LongpollDialogListener;
import org.happysanta.messenger.longpoll.updates.LongpollNewMessage;
import org.happysanta.messenger.longpoll.updates.LongpollTyping;
import org.happysanta.messenger.messages.ChatActivity;

import java.util.ArrayList;


/**
 * Created by Jesus Christ. Amen.
 */
public class ConversationsListFragment extends Fragment {
    private View rootView;
    private VKList<VKApiDialog> dialogs;
    private TextView status;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_conversations_list, container, false);
        final ListView list = (ListView) rootView.findViewById(R.id.list);
        status = (TextView) rootView.findViewById(R.id.status);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VKApiDialog dialog = dialogs.get(position);
                Intent intent = ChatActivity.getActivityIntent(getActivity(), dialog);
                startActivity(intent);
            }
        });

        new VKApiMessages().getDialogs().executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                VKList<VKApiDialog> messages = (VKList<VKApiDialog>) response.parsedModel;
                dialogs = messages;
                list.setAdapter(new ConversationsAdapter());
                status.setVisibility(View.GONE);

                LongpollService.addGlobalConversationListener(new LongpollDialogListener(0) {
                    @Override
                    public void onNewMessages(ArrayList<LongpollNewMessage> newMessages) {
                        for (LongpollNewMessage newMessage : newMessages) {
                            for (VKApiDialog dialog : dialogs) {
                                if(dialog.dialogId==newMessage.user_id){
                                    dialog.body = newMessage.body;
                                    dialogs.remove(dialog);
                                    dialogs.add(0,dialog);
                                    break;
                                }
                            }
                        }
                        ((BaseAdapter)list.getAdapter()).notifyDataSetChanged();
                    }

                    @Override
                    public void onTyping(ArrayList<LongpollTyping> typing) {

                    }
                });
            }

            @Override
            public void onError(VKError error) {
                status.setText(error.toString());
            }
        });


        return rootView;
    }


    private class ConversationsAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return dialogs.size();
        }

        @Override
        public VKApiDialog getItem(int position) {
            return dialogs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View dialogView = getActivity().getLayoutInflater().inflate(R.layout.item_dialog, null);
            TextView titleView = (TextView) dialogView.findViewById(R.id.title);
            TextView bodyView = (TextView) dialogView.findViewById(R.id.body);
            final ImageView photoView = (ImageView) dialogView.findViewById(R.id.dialog_photo);
            VKApiDialog dialog = getItem(position);


            titleView.setText(dialog.title);
            bodyView.setText(dialog.body);
            ImageUtil.showFromCache(new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    photoView.setImageBitmap(BitmapUtil.circle(loadedImage));
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            }, dialog.getPhoto());

            return dialogView;
        }
    }
}
