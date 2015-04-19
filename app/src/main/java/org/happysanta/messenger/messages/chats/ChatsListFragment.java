package org.happysanta.messenger.messages.chats;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import org.happysanta.messenger.core.BaseFragment;
import org.happysanta.messenger.core.BaseViewHolder;
import org.happysanta.messenger.core.util.BitmapUtil;
import org.happysanta.messenger.core.util.ImageUtil;
import org.happysanta.messenger.messages.ChatActivity;

/**
 * Created by Jesus Christ. Amen.
 */
public class ChatsListFragment extends BaseFragment {
    private View rootView;
    private VKList<VKApiDialog> dialogs;
    private TextView status;
    private RecyclerView recycler;
    private LinearLayoutManager recyclerLayoutManager;
    private ConversationsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_chats_list, container, false);
        status = (TextView) rootView.findViewById(R.id.status);
        recycler = (RecyclerView) rootView.findViewById(R.id.recycler);

        recyclerLayoutManager = new LinearLayoutManager(activity);
        recycler.setLayoutManager(recyclerLayoutManager);
        recycler.setHasFixedSize(false);


        new VKApiMessages().getChats().executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                dialogs = (VKList<VKApiDialog>) response.parsedModel;
                ChatsListFragment.this.dialogs = dialogs;
                adapter = new ConversationsAdapter();
                adapter.setHasStableIds(true);
                recycler.setAdapter(adapter);
                status.setVisibility(View.GONE);
            }

            @Override
            public void onError(VKError error) {
                status.setText(error.toString());
            }
        });

        return rootView;
    }

    private class ConversationsAdapter extends RecyclerView.Adapter<ChatsHolder> {
        @Override
        public ChatsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ChatsHolder(View.inflate(activity,R.layout.item_chat, null));
        }

        @Override
        public void onBindViewHolder(ChatsHolder holder, int position) {
            holder.bind(0, dialogs.get(position));
        }

        @Override
        public long getItemId(int position) {
            VKApiDialog dialog = dialogs.get(position);
            return dialog.getId();
        }

        @Override
        public int getItemCount() {
            return dialogs.size();
        }
    }

    private class ChatsHolder extends BaseViewHolder {
        private View btnDialog;
        private TextView titleView;
        private TextView bodyView;
        private final ImageView photoView;

        public ChatsHolder(View itemView) {
            super(itemView);
            btnDialog = itemView.findViewById(R.id.btn_chat);
            titleView = (TextView) itemView.findViewById(R.id.title);
            bodyView = (TextView) itemView.findViewById(R.id.body);
            photoView = (ImageView) itemView.findViewById(R.id.chat_photo);
        }
        public void bind(final int position, final VKApiDialog dialog){
            btnDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogs.get(position);
                    startActivity(ChatActivity.openChat(getActivity(), dialog));
                }
            });
            titleView.setText(dialog.getTitle());
            bodyView.setText(dialog.usersCount+ " в чате");
            if(dialog.getPhoto().equals(null)){
                photoView.setImageBitmap(BitmapUtil.circle(R.drawable.user_placeholder));
            }else {
                // todo group placeholder
                ImageUtil.showFromCache(dialog.getPhoto(), new ImageLoadingListener() {
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
                });
                ImageLoader.getInstance().displayImage(dialog.getPhoto(), photoView);
            }
        }
    }
}
