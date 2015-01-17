package org.happysanta.messenger.messages.attach;

import java.io.File;

/**
 * Created by Jesus Christ. Amen.
 */
public interface AttachListener {

    void onFileAttached(File... file);

    void onPictureAttached(File... pictureFile);

    void onVideoAttached(File... videoFile);

    void onAudioAttached(File... audioFile);

    void onGeoAttached(AttachGeo geo);


}
