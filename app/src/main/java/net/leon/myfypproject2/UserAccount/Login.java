package net.leon.myfypproject2.UserAccount;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.shashank.sony.fancytoastlib.FancyToast;

import net.leon.myfypproject2.MainActivity;
import net.leon.myfypproject2.R;

public class Login extends AppCompatActivity {
    private EditText LoginEmail,LoginPassword;
    private Button LoginBtn;
    private ProgressDialog loadingbar;
    private TextView signup;
    private FirebaseAuth mAuth;
    private ImageView loggmail;
    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleSignInClient1;
    private GoogleSignInClient mGoogleSignInClient;
    private  static final String TAG = "Login";
    private Boolean emailAddressChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        LoginEmail = (EditText)findViewById(R.id.LogEmail);
        LoginPassword = (EditText) findViewById(R.id.LogPass);
        LoginBtn =(Button)findViewById(R.id.Logbtn);
        signup = (TextView)findViewById(R.id.Signup);
        mAuth = FirebaseAuth.getInstance();
        loggmail = (ImageView)findViewById(R.id.loggmail);
        loadingbar = new ProgressDialog(this);
        signup.setOnClickListener(new View.OnClickListener() { //Link User To Register Page
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserLogin();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

       mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        loggmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }

    private void VerifyEmailAddress(){
        FirebaseUser user = mAuth.getCurrentUser();
        emailAddressChecker = user.isEmailVerified();

        if(emailAddressChecker){
            inMainMenus();

        }else
        {
            FancyToast.makeText(this,"Please Verify Your Account!",FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();
            mAuth.signOut();

        }
    }




    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            inMainMenus();
        }
    }

    private void UserLogin() {
        String email = LoginEmail.getText().toString();
        String password = LoginPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            FancyToast.makeText(this,"Please Enter Your Email!",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
        }else if (TextUtils.isEmpty(password)){
            FancyToast.makeText(this,"Please Enter Your Password!",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
        }else {
            loadingbar.setTitle("Login In");
            loadingbar.setMessage("Please Wait!");
            loadingbar.show();
            loadingbar.setCanceledOnTouchOutside(true);
            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                VerifyEmailAddress();
                                loadingbar.dismiss();
                            }else {
                                String message = task.getException().getMessage();
                                FancyToast.makeText(Login.this,"Login Failed :"+ message,FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                                loadingbar.dismiss();
                            }
                        }
                    });
        }
    }

    private void inMainMenus() {
        Intent SetupUser = new Intent(Login.this, MainActivity.class);
        SetupUser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(SetupUser);
        finish();
    }

    private void toLogin() {
        Intent SetupUser = new Intent(Login.this, Login.class);
        SetupUser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(SetupUser);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                FancyToast.makeText(Login.this,"Please wait, while we are getting your auth result",FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();
            }else {

                FancyToast.makeText(Login.this,"Failed to get Auth Result",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();

            }


        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "signInWithCredential:success");
                            inMainMenus();


                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            String message = task.getException().toString();
                            toLogin();
                            FancyToast.makeText(Login.this,"Not Authenticated, Please try Again" + message,FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                        }

                    }
                });
    }
}
