package org.happysanta.messenger.Photo;

import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.happysanta.messenger.R;

import java.io.IOException;

/**
 * Created by Jesus Christ. Amen.
 */
public class MakePhotoFragment extends Fragment implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private final static String DEBUG_TAG = "MakePhotoFragment";
    private Camera camera;
    private SurfaceView preview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_camera,container,false);

        Button button = (Button) rootView.findViewById(R.id.camera_button);

        Button anotherButton = (Button) rootView.findViewById(R.id.another);






        preview = (SurfaceView) rootView.findViewById(R.id.surface);
        SurfaceHolder surfaceHolder = preview.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        anotherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rootView.findViewById(R.id.extra_container).setVisibility(View.VISIBLE);
                getFragmentManager().beginTransaction().replace(R.id.extra_container, new FrontCamera()).commit();


            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, null, new PhotoHandler(getActivity()));

            }
        });
        return rootView;

    }





    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try
        {
            camera.setPreviewDisplay(holder);
            camera.setPreviewCallback(this);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Camera.Size previewSize = camera.getParameters().getPreviewSize();
        float aspect = (float) previewSize.width / previewSize.height;

        int previewSurfaceWidth = preview.getWidth();
        int previewSurfaceHeight = preview.getHeight();

        ViewGroup.LayoutParams lp = preview.getLayoutParams();

        // здесь корректируем размер отображаемого preview, чтобы не было искажений

        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)
        {
            // портретный вид
            camera.setDisplayOrientation(90);
            lp.height = previewSurfaceHeight;
            lp.width = (int) (previewSurfaceHeight / aspect);

        }
        else
        {
            // ландшафтный
            camera.setDisplayOrientation(0);
            lp.width = previewSurfaceWidth;
            lp.height = (int) (previewSurfaceWidth / aspect);
        }

        preview.setLayoutParams(lp);
        camera.startPreview();
    }
    @Override
    public void onResume()
    {
        super.onResume();
        camera = Camera.open();
    }
    @Override
    public void onPause()
    {
        super.onPause();

        if (camera != null)
        {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

    }
}