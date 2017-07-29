package com.pythonanywhere.ticatwolves.freeeth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ticat on 28/7/17.
 */

public class FetchAds extends AsyncTask<String,String,Void> {
    Context ctx;
    private DatabaseReference mDatabase;

    SharedPreferences sh1;


    //Save local storage
    SharedPreferences sh;
    SharedPreferences.Editor myeditor;

    String updated ="";

    ProgressDialog progress;
    private InterstitialAd mInterstitialAd;
    Boolean loaded=false;
    public FetchAds(Context ct){ctx=ct;}

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(ctx);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        mInterstitialAd = new InterstitialAd(ctx);
        mInterstitialAd.setAdUnitId("ca-app-pub-5759830031625110/2412581946");
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                loaded = true;
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }
        });
    }

    @Override
    protected Void doInBackground(String... strings) {
        while (!(loaded)){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.i("Done","IUHVbn");
        progress.dismiss();
        mInterstitialAd.show();


        //Toast.makeText(getActivity(),"closed", Toast.LENGTH_SHORT).show();
        //myeditor.putBoolean("done",false);
        //myeditor.putInt("minute",0);
        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile").child("amount");
        // .getKey("dName");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updated = String.valueOf(Integer.parseInt(String.valueOf(dataSnapshot.getValue()))+500);
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Profile").child("amount").setValue(updated);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });
        sh = ctx.getSharedPreferences("time",MODE_PRIVATE);
        myeditor = sh.edit();


        myeditor.putInt("seconds",60);
        myeditor.putInt("minutes",29);

//        myeditor.putInt("bugs",500);
        //       myeditor.commit();
        TimeService.done = false;
        //super.onDestroy();
        //Intent service=new Intent(this,TimeService.class);
        //onDestroy();
        //this.startService(service);

    }


}
