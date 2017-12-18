/**
 * Created by yyk on 16/02/16.
 */

package com.yyktools.thrustcalc;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CVSWriter {
    private File m_file;
    private String m_err;

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
        m_file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);
    }

    boolean writeLine(String txt)
    {
        try {
            FileWriter f = new FileWriter(m_file, true);
            f.write(txt + "\n");
            f.flush();
            f.close();
            return true;
        }
        catch (IOException e) {
            m_err = e.getMessage();
            return false;
        }
    }

    String GetLastError() {
        return m_err;
    }
}
