package com.droidkit.pickers;

import android.content.Context;
import android.content.Intent;

import com.droidkit.pickers.file.FilePickerActivity;
import com.droidkit.pickers.map.MapPickerActivity;

/**
 * Created by ex3ndr on 14.10.14.
 */
public class Intents {
    public static Intent pickFile(Context context) {
        return new Intent(context, FilePickerActivity.class);
    }


    public static Intent pickLocation(Context context) {
        return new Intent(context, MapPickerActivity.class);
    }
    /*
    public static Intent pickVideo(Context context) {
        return new Intent(context, VideoPickerActivity.class);
    }

    public static Intent pickGallery(Context context) {
        return new Intent(context, GalleryPickerActivity.class);
    }

    public static Intent pickAudio(Context context) {
        return new Intent(context, AudioPickerActivity.class);
    }

    public static Intent pickPhoto(Context context) {
        return new Intent(context, PhotoPickerActivity.class);
    }
    */
}
