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

import org.happysanta.messenger.R;

/**
 * Created by Jesus Christ. Amen.
 */
public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

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
