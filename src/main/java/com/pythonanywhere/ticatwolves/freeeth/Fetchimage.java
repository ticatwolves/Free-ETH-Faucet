package com.pythonanywhere.ticatwolves.freeeth;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import com.pythonanywhere.ticatwolves.freeeth.HomeActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ticat on 16/7/17.
 */

class Fetchimage extends AsyncTask<String,Void,Bitmap> {
    Context ctx;
    Fetchimage(Context ct){
        ctx = ct;
    }
    @Override
    protected Bitmap doInBackground(String... strings) {
        String s1 = strings[0];
        InputStream in;
        try {
            URL url = new URL(s1);
            HttpURLConnection mycon = (HttpURLConnection) url.openConnection();
            mycon.setConnectTimeout(20000);
            mycon.setReadTimeout(10000);
            mycon.setRequestMethod("GET");
            mycon.connect();
            in = mycon.getInputStream();
            Bitmap bit = BitmapFactory.decodeStream(in);
            return bit;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Bitmap bitmap){
        //HomeActivity.myimage.setImageBitmap(bitmap);
        ImageSaver img = new ImageSaver(ctx).
                setFileName("myImage.png").
                setDirectoryName("images").
                save(bitmap);
        Toast.makeText(ctx,"Saved",Toast.LENGTH_SHORT).show();
        HomeActivity.myimage.setImageBitmap(bitmap);
    }
}