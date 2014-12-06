package org.happysanta.messenger.Photo;

import android.content.Context;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Jesus Christ. Amen.
 */
public class PhotoHandler implements Camera.PictureCallback {
    private static final String LOG_TAG = "PhotoHandler";
    private final Context context;

    public PhotoHandler (Context context){
        this.context = context;
    }

    @Override
    public void onPictureTaken(byte[] paramArrayOfByte, Camera paramCamera)
    {

        try
        {
            File saveDir = new File(Environment.getExternalStorageDirectory(),"vkmessenger");

            if (!saveDir.exists())
            {
                saveDir.mkdirs();
            }

            FileOutputStream os = new FileOutputStream(String.format(saveDir.getAbsolutePath()+"/vkphoto_%d.jpg", System.currentTimeMillis()));
            os.write(paramArrayOfByte);
            os.close();
        }
        catch (Exception e)
        {
            Log.e(LOG_TAG,"Saving error",e);
        }


        // после того, как снимок сделан, показ превью отключается. необходимо включить его
        paramCamera.startPreview();
    }

    public File getDir() {
        File sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(sdDir, "CameraAPIDemo");

    }

}