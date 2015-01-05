package org.happysanta.messenger.settings.customElements;

import android.content.Context;
import android.preference.PreferenceCategory;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import org.happysanta.messenger.R;

/**
 * Created by alex on 04/01/15.
 */

public class StyledPreferencesCategory extends PreferenceCategory {

    public StyledPreferencesCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onBindView(@NonNull View view) {
        super.onBindView(view);

        TextView titleView = (TextView) view.findViewById(android.R.id.title);

        titleView.setTextColor(getContext().getResources().getColor(R.color.vk_color));
    }
}
