package com.nil.code.libs.util;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Mwh on 2017/8/24.
 */

public class FileUtils {
    public static File saveBitmap(Bitmap bm, String filePath) throws Exception {
        File f = new File(filePath);
        if (f.exists()) {
            f.delete();
        }
        FileOutputStream out = new FileOutputStream(f);
        bm.compress(Bitmap.CompressFormat.PNG, 90, out);
        out.flush();
        out.close();
        return f;
    }

    public static boolean saveBytes(byte[] bm, String filePath) throws Exception {
        File f = new File(filePath);
        if (f.exists()) {
            f.delete();
        }
        FileOutputStream out = new FileOutputStream(f);
        out.write(bm);
        out.flush();
        out.close();
        return true;
    }
}
