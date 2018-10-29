package net.leon.myfypproject2.UserAccount;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import net.leon.myfypproject2.MainActivity;
import net.leon.myfypproject2.R;

public class Login extends AppCompatActivity {
    private EditText LoginEmail,LoginPassword;
    private Button LoginBtn;
    private ProgressDialog loadingbar;
    private TextView signup;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        LoginEmail = (EditText)findViewById(R.id.LogEmail);
        LoginPassword = (EditText) findViewById(R.id.LogPass);
        LoginBtn =(Button)findViewById(R.id.Logbtn);
        signup = (TextView)findViewById(R.id.Signup);
        mAuth = FirebaseAuth.getInstance();
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
                                inMainMenus();
                                FancyToast.makeText(Login.this,"Login Successfully!",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();

                                Toast.makeText(Login.this,"Login Successfully!", Toast.LENGTH_SHORT).show();
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
}
