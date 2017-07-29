package com.pythonanywhere.ticatwolves.freeeth;

import android.app.ActivityManager;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ticat on 13/7/17.
 */


public class HomeContent extends Fragment {
    View myview;
    ProgressBar progressBarCircle;
    Button withdraw,claim;
    TextView timer,tbugs;
    Handler handler = new Handler();
    private static final String TAG = "MainActivity";
    int sec,min,bugs;
    Boolean done=true;
    String show_time,tsec,tmin;

    static String amount="";

    //Full Screen ADS
    private InterstitialAd mInterstitialAd;

    String updated ="";


    //private InterstitialAd mInterstitialAd;
    private DatabaseReference mDatabase;

    SharedPreferences sh1;


    //Save local storage
    SharedPreferences sh;
    SharedPreferences.Editor myeditor;

    ConnectivityManager myconnManager;
    NetworkInfo myinfo;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.content_home,container,false);
        return myview;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        if(isMyservice(TimeService.class)){
            //Toast.makeText(getActivity(),"running lul",Toast.LENGTH_SHORT).show();
        }
        else {
            //Toast.makeText(getActivity(),"i started",Toast.LENGTH_SHORT).show();
            Intent service=new Intent(getActivity(),TimeService.class);
            getActivity().startService(service);
            Log.i("You Destroy","Noob be right back");
        }

        getActivity().setTitle(R.string.title_activity_home);

        sh = getActivity().getSharedPreferences("time",MODE_PRIVATE);
        myeditor = sh.edit();

        tbugs = (TextView)getActivity().findViewById(R.id.balance);
        claim = (Button)getActivity().findViewById(R.id.claimbug);
        withdraw = (Button)getActivity().findViewById(R.id.withdraw);

        timer = (TextView)getActivity().findViewById(R.id.timer);
        claim.setEnabled(false);
        claim.setBackgroundResource(R.color.black_overlay);
        progressBarCircle = (ProgressBar) getActivity().findViewById(R.id.progressBarCircle);
        progressBarCircle.setProgress(0);
        claim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myconnManager = (ConnectivityManager)getActivity().getSystemService(CONNECTIVITY_SERVICE);
                myinfo = myconnManager.getActiveNetworkInfo();

                Toast.makeText(getActivity(),"Your claim is ready",Toast.LENGTH_SHORT).show();
                //ADS here
                if(myinfo!=null && myinfo.isConnected()) {
                    FetchAds f = new FetchAds(getActivity());
                    f.execute();

                }else {
                    Toast.makeText(getActivity(),"Check your internet connection",Toast.LENGTH_SHORT).show();
                }

            }
        });
        //handler.post(abc);
        sh1 = getActivity().getApplicationContext().getSharedPreferences("time",MODE_APPEND);

        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile");
        // .getKey("dName");


        mDatabase.child("amount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                amount = (String) snapshot.getValue();
                Log.i("Motherofhell",amount);
                tbugs.setText(amount);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(getApplicationContext(),"Connect to internet",Toast.LENGTH_SHORT).show();
            }
        });

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(!(Integer.parseInt(amount)<200000)){
                        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Profile").child("claim").push().setValue(tbugs.getText().toString());
                        FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Profile").child("amount").setValue("0");
                        Toast.makeText(getActivity(),"Your Withdraw will take 7 days to complete",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getActivity(),"Withdraw amount should be more then 200000 bugs",Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Toast.makeText(getActivity(),"Check internet connection",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        handler.post(abc);
    }

    Runnable abc = new Runnable() {
        @Override
        public void run() {
            String settime = sh1.getString("showtime","00:00");
            //int time=0;
            int a = sh1.getInt("perc",0);
            int percentage = (a*100)/1800;
            percentage = 100-percentage;
            //tbugs.setText(String.valueOf(a));
            progressBarCircle.setProgress(percentage);
            timer.setText(settime);
            if(settime.equals("claim")){
                claim.setEnabled(true);
                claim.setBackgroundResource(R.drawable.custom_buttons);
            }
            else {
                claim.setEnabled(false);
            }
            handler.postDelayed(abc,1000);
        }
    };

    private boolean isMyservice(Class<?> serviceClass){
        ActivityManager manager = (ActivityManager)getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service: manager.getRunningServices(Integer.MAX_VALUE)){
            if(serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /*public void changeda() {
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


        myeditor.putInt("seconds",60);
        myeditor.putInt("minutes",29);

//        myeditor.putInt("bugs",500);
        //       myeditor.commit();
        TimeService.done = false;
        //super.onDestroy();
        //Intent service=new Intent(this,TimeService.class);
        //onDestroy();
        //this.startService(service);
    }*/
}




