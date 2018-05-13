package com.example.kevinbarbian14.dispatchaces;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Google Sign-in screen for dispatcher user to sign into the app with.
 * Requires augieaardvark@gmail.com to sign in
 * Date: 5/13/2018
 * @author  Tyler May, Kevin Barbian, Megan Janssen, Tan Nguyen
 */

public class Google_SignIn extends AppCompatActivity {

    final static int PERMISSION_ALL = 1;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    final static String[] PERMISSIONS = {android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION};
    private SignInButton signInButton;
    private Button aboutPageButton;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;
    private static final String TAG = "Sign in Activity";
    private FirebaseAuth.AuthStateListener authStateListener;

    //ONLY EMAIL allowed to sign in to the app
    private static final String signInEmail = "augieaardvark@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_signin_layout);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()!=null && FirebaseAuth.getInstance().getCurrentUser().getEmail().toLowerCase().contains(signInEmail))
                    startActivity(new Intent(Google_SignIn.this,MainActivity.class));
                else if (firebaseAuth.getCurrentUser()!=null && !FirebaseAuth.getInstance().getCurrentUser().getEmail().toLowerCase().contains(signInEmail)) {
                    Toast toast = Toast.makeText(getBaseContext(), "Please use the augieaardvark email!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    FirebaseAuth.getInstance().signOut();
                }

            }
        };


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage(Google_SignIn.this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(Google_SignIn.this, "Error", Toast.LENGTH_LONG).show();

            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        signInButton = (SignInButton) findViewById(R.id.google_btn);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Auth.GoogleSignInApi.signOut(googleApiClient);
                    signIn();

            }
        });

        aboutPageButton = (Button) findViewById(R.id.about_btn);
        aboutPageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Google_SignIn.this,AboutPageActivity.class));
            }
        });
    }


    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }


    private void signIn() {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /*
     * After the user selects their email and chooses to log in, this code is executed to sign
     * them into their google account.
     * @param  requestCode used to identify the previous activity that was executed
     * @param  resultCode used to indicate whether or not the activity succeeded
     * @param data used to identify which intent was selected
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthWithGoogle(account);
            }else{

                //TODO: google sign in failed
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount account){

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:failed");
                            Toast.makeText(Google_SignIn.this, "Authentication:Failed",Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }
    /*
     * User can exit out of the app by clicking the back button
     */
    public void onBackPressed() {

        moveTaskToBack(true);
    }

}
