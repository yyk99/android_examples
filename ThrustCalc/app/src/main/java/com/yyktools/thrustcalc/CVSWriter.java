package com.yyktools.thrustcalc;

import android.os.Environment;

/**
 * Created by yyk on 16/02/16.
 */
public class CVSWriter {
    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    CVSWriter (String filename)
    {

    }

    boolean writeLine(String txt)
    {
        return true;
    }
}
