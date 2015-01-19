package org.happysanta.messenger.core;

import android.content.Intent;

/**
 * Created by Jesus Christ. Amen.
 */
public interface ActivityResultCatcher {
    // returns true if result catched
    boolean onActivityResult(int requestCode, int resultCode, Intent data);

}
