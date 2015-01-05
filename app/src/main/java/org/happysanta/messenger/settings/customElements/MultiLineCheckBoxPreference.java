package org.happysanta.messenger.settings.customElements;

import android.content.Context;
import android.preference.CheckBoxPreference;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by alex on 05/01/15.
 */
public class MultiLineCheckBoxPreference extends CheckBoxPreference {


    public MultiLineCheckBoxPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MultiLineCheckBoxPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MultiLineCheckBoxPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiLineCheckBoxPreference(Context context) {
        super(context);
    }

    @Override
    protected void onBindView(@NonNull View view) {
        super.onBindView(view);

        TextView titleView = (TextView) view.findViewById(android.R.id.title);

        if (titleView != null) {

            titleView.setSingleLine(false);
        }
    }
}
