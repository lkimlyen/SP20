package com.demo.architect.utils.view;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.realm.Realm;

public class FileUtils {
    public static File bitmapToFile(Bitmap bmp, String filePath) {
        //create a file to write bitmap data
        File f = new File(filePath);
        try {
            f.createNewFile();
            //Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, /*ignored for PNG*/100, bos);
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return f;

    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static Bitmap resizeImage(Bitmap image, int maxWidth, int maxHeight) {
        Bitmap resizedImage = null;
        try {
            int imageHeight = image.getHeight();


            if (imageHeight > maxHeight)
                imageHeight = maxHeight;
            int imageWidth = (imageHeight * image.getWidth())
                    / image.getHeight();

            if (imageWidth > maxWidth) {
                imageWidth = maxWidth;
                imageHeight = (imageWidth * image.getHeight())
                        / image.getWidth();
            }

            if (imageHeight > maxHeight)
                imageHeight = maxHeight;
            if (imageWidth > maxWidth)
                imageWidth = maxWidth;


            resizedImage = Bitmap.createScaledBitmap(image, imageWidth,
                    imageHeight, true);
        } catch (OutOfMemoryError e) {

            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resizedImage;
    }
    public static String exportRealmFile(String path) {
        final Realm realm = Realm.getDefaultInstance();
        String filePath = "";
        try {

            File fileMain = new File(path);
            if (!fileMain.exists()){
                fileMain.mkdirs();
            }
            final File file = new File(path);
            if (file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }

            realm.writeCopyTo(file);
            filePath = file.getPath();
        } catch (Exception e) {
            realm.close();
            e.printStackTrace();
        }
        return filePath;
    }



}
