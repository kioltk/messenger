package org.happysanta.messenger.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.vk.sdk.api.model.VKApiUserFull;

import org.happysanta.messenger.R;
import org.happysanta.messenger.user.UserDialog;

/**
 * Created by Jesus Christ. Amen.
 */
public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        LinearLayout jenyaButton = (LinearLayout) rootView.findViewById(R.id.dev_jenya);
        jenyaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDialog dialog = new UserDialog(getActivity(), 51916034);
                dialog.show();

            }
        });

        LinearLayout artemButton = (LinearLayout) rootView.findViewById(R.id.dev_artem);
        artemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    UserDialog dialog = new UserDialog(getActivity(), 103850036);
                    dialog.show();
            }
        });

        LinearLayout denyaButton = (LinearLayout) rootView.findViewById(R.id.dev_denya);
        denyaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    UserDialog dialog = new UserDialog(getActivity(), 32018303);
                    dialog.show();
                           }
        });

        LinearLayout andreyButton = (LinearLayout) rootView.findViewById(R.id.dev_andrey);
        andreyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    UserDialog dialog = new UserDialog(getActivity(), 121935185);
                    dialog.show();
            }
        });

        Button helpButton = (Button) rootView.findViewById(R.id.help);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.vk.com/help"));
                startActivity(browserIntent);
            }
        });

        Button licenseButton = (Button) rootView.findViewById(R.id.license);
        licenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.vk.com/license"));
                startActivity(browserIntent);
            }
        });

        Button privacyButton = (Button) rootView.findViewById(R.id.privacy);
        privacyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.vk.com/privacy"));
                startActivity(browserIntent);
            }
        });

        Button termsButton = (Button) rootView.findViewById(R.id.terms);
        termsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.vk.com/terms"));
                startActivity(browserIntent);
            }
        });

        Button reportButton = (Button) rootView.findViewById(R.id.report);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.vk.com/report"));
                startActivity(browserIntent);
            }
        });

        return rootView;
    }
}
