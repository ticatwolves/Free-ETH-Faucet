package com.pythonanywhere.ticatwolves.freeeth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pythonanywhere.ticatwolves.freeeth.user.User;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    SignInButton signInButton;
    int RC_SIGN_IN=1;
    GoogleApiClient mGoogleApiClient;
    String TAG = "Error";

    private DatabaseReference mDatabase,mDatabaseu;


    SharedPreferences sh;
    SharedPreferences.Editor myeditor;

    FirebaseAuth.AuthStateListener mAuthListner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInButton = (SignInButton)findViewById(R.id.loginbutt);

        mAuth = FirebaseAuth.getInstance();

        sh = getSharedPreferences("time",MODE_PRIVATE);
        myeditor = sh.edit();

        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent (MainActivity.this,HomeActivity.class).putExtra("name",mAuth.getCurrentUser().getDisplayName()).putExtra("email",mAuth.getCurrentUser().getEmail()));
                    finish();
                }
            }
        };
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(MainActivity.this,"An Error Occur", Toast.LENGTH_SHORT).show();
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {

                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();

                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.i("FUDI","Logged in");
                            FirebaseUser user = mAuth.getCurrentUser();
                            final String userDisplayName = user.getDisplayName();
                            final String userEmail = user.getEmail();
                            final String userPhotoUrl = user.getPhotoUrl().toString();
                            final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            final User auser = new User(userDisplayName,userEmail,userPhotoUrl,userId);

                            FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Profile").setValue(auser);
                            myeditor.putString("Name",userDisplayName);
                            myeditor.putString("Email",userEmail);
                            Log.i("Fudi","Not Found");
                            Fetchimage f=new Fetchimage(getApplicationContext());
                            f.execute(userPhotoUrl);

/*                            Log.i("FUDI","DATA getten Done");
                            //Updation
                            mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile").child("Email");
                            Log.i("Fuckers",mDatabase.toString());
                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if(dataSnapshot.exists()){
                                        Log.i("Fudi","Found in db");
                                        Fetchimage f=new Fetchimage(getApplicationContext());
                                        f.execute(userPhotoUrl);
                                        //Toast.makeText(getApplicationContext(),"You already exist",Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Profile").setValue(auser);
                                        myeditor.putString("Name",userDisplayName);
                                        myeditor.putString("Email",userEmail);
                                        Log.i("Fudi","Not Found");
                                        Fetchimage f=new Fetchimage(getApplicationContext());
                                        f.execute(userPhotoUrl);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
*/
                            //myRef.setValue("Hello, World!");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }


}
