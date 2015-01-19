package org.happysanta.messenger.sandbox;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;

import com.vk.sdk.api.model.VKApiUserFull;

import org.happysanta.messenger.R;
import org.happysanta.messenger.chatheads.ChatHeadsManager;
import org.happysanta.messenger.core.BaseFragment;
import org.happysanta.messenger.core.util.Dimen;
import org.happysanta.messenger.messages.ChatActivity;

/**
 * Created by Jesus Christ. Amen.
 */
public class SandboxFragment extends BaseFragment {

    private View typingViewItem3;
    private View typingView;
    private View typingViewItem1;
    private View typingViewItem2;

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


        typingView = findViewById(R.id.typing);
        typingViewItem1 = findViewById(R.id.typing_view_item1);
        typingViewItem2 = findViewById(R.id.typing_view_item2);
        typingViewItem3 = findViewById(R.id.typing_view_item3);
        View typingViewButton = findViewById(R.id.show_typing);
        typingViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTyping();
            }
        });

        return rootView;
    }

    private void showTyping() {
        typingView.setVisibility(View.VISIBLE);
        new Runnable() {

            void animate(final View view){
                view.animate()
                        .translationY(-Dimen.get(R.dimen.typing_item_jumpheight))
                        .setDuration(200)
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                view.clearAnimation();
                                view.animate().translationY(0)
                                        .setDuration(300).setStartDelay(0)
                                        .setInterpolator(new AccelerateInterpolator())
                                        .start();
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        })
                        .start();
            }

            @Override
            public void run() {

                typingViewItem1.clearAnimation();
                typingViewItem2.clearAnimation();
                typingViewItem3.clearAnimation();

                typingViewItem1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animate(typingViewItem1);
                    }
                },0);
                typingViewItem2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animate(typingViewItem2);
                    }
                },150);
                typingViewItem3.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animate(typingViewItem3);
                    }
                },300);

            }
        }.run();

        typingView.postDelayed(new Runnable() {
            @Override
            public void run() {
                typingView.setVisibility(View.GONE);
                typingViewItem1.clearAnimation();
                typingViewItem2.clearAnimation();
                typingViewItem3.clearAnimation();
            }
        }, 1300);
    }

}
