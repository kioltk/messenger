package org.happysanta.messenger.settings.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.util.ProfileUtil;

/**
 * Created by Jesus Christ. Amen.
 */
public class LogoutPreference extends DialogPreference {


    private DialogInterface.OnClickListener logoutListener;

    public LogoutPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LogoutPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public LogoutPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LogoutPreference(Context context) {
        super(context);
    }

    @Override
    protected void onBindView(@NonNull View view) {
        super.onBindView(view);

        TextView titleView = (TextView) view.findViewById(android.R.id.title);

        if (titleView != null) {
            titleView.setSingleLine(false);
            titleView.setTextColor(getContext().getResources().getColor(R.color.logout));
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) titleView.getLayoutParams();
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
        }
    }

    public void setOnLogoutListener(DialogInterface.OnClickListener logoutListener) {
        this.logoutListener = logoutListener;
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        builder.setTitle("Confirm")
                .setMessage("Logout from " + ProfileUtil.getUserName() + "?")
                .setPositiveButton("Logout", logoutListener);
    }
}