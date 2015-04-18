package org.happysanta.messenger.posts;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.sdk.api.model.VKApiComment;
import com.vk.sdk.api.model.VKCommentArray;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseViewHolder;
import org.happysanta.messenger.core.util.BitmapUtil;
import org.happysanta.messenger.core.util.TimeUtils;
import org.happysanta.messenger.core.views.TintImageView;
import org.happysanta.messenger.user.ProfileActivity;

/**
 * Created by admin on 17.04.2015.
 */
public class CommentHolder extends BaseViewHolder {
    private ImageView photoView;
    private TextView nameView;
    private final TextView textView;
    final TextView dateView;
    private TextView likesCountView;
    private TintImageView likeView;
    private View btnLike;
    private View btnToUser;
    private TextView toUserView;

    public CommentHolder(View itemView) {
        super(itemView);
        nameView = (TextView) itemView.findViewById(R.id.user_name);
        textView = (TextView) itemView.findViewById(R.id.comment_text);
        photoView = (ImageView) itemView.findViewById(R.id.user_photo);
        dateView = (TextView) itemView.findViewById(R.id.comment_date);
        likesCountView = (TextView) itemView.findViewById(R.id.comment_likes_count);
        likeView = (TintImageView) itemView.findViewById(R.id.comment_likes);
        btnLike = itemView.findViewById(R.id.btn_like);
        btnToUser = itemView.findViewById(R.id.btn_to_user);
        toUserView = (TextView) itemView.findViewById(R.id.comment_to_user);

    }

    public void bind(final VKApiComment comment) {
        nameView.setText("" + comment.from_id);
        photoView.setImageBitmap(BitmapUtil.circle(R.drawable.user_placeholder));
        textView.setText(comment.text);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(ProfileActivity.openProfile(getContext(), comment.from_id));
            }
        });

        dateView.setText(TimeUtils.format(comment.date * 1000, getContext()));
        if(comment.reply_to_user != 0) {
            toUserView.setText("to " + comment.reply_to_user);
        } else {
            btnToUser.setVisibility(View.GONE);
        }

        btnToUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "return to user comment", Toast.LENGTH_SHORT).show();
            }
        });

        likesCountView.setText("" + comment.likes);

        if(comment.likes != 0) {
                likesCountView.setTextColor(getContext().getResources().getColor(R.color.post_item_blue));
                likeView.setTint(getContext().getResources().getColor(R.color.post_item_blue));
        } else {
            btnLike.setVisibility(View.GONE);
        }
    }
}
