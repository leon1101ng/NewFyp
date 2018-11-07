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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import net.leon.myfypproject2.R;

public class Register extends AppCompatActivity {
    private EditText Useremail,UserPass,UserComPass;
    private Button CreateAcc;
    private ProgressDialog loadingregister;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

         Useremail = (EditText)findViewById(R.id.RegEmail);
         UserPass = (EditText)findViewById(R.id.RegPass);
         UserComPass = (EditText)findViewById(R.id.RegCPass);
         CreateAcc = (Button)findViewById(R.id.Regbtn);
        loadingregister = new ProgressDialog(this);
         CreateAcc.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 CreateAcc();
             }
         });
    }

    private void SendEmailVerifyMessage(){
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                        FancyToast.makeText(Register.this,"Registration Successful, we've sent you a mail. Please check and verify your account!",FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();
                        inMainMenus();
                        mAuth.signOut();
                    }else {
                        String error = task.getException().toString();
                        FancyToast.makeText(Register.this,"Fail To Register " + error,FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();
                        mAuth.signOut();
                    }

                }
            });
        }
    }



    private void CreateAcc() {
        String Regemail = Useremail.getText().toString();
        String Regpass = UserPass.getText().toString();
        String Regcpass = UserComPass.getText().toString();


        if(TextUtils.isEmpty(Regemail)){
            Toast.makeText(this,"Please Enter Your Email !", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Regpass)){
            Toast.makeText(this,"Please Enter Your Password !", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Regcpass)){
            Toast.makeText(this,"Please Enter Your Comfirm Password !", Toast.LENGTH_SHORT).show();
        }else if (!Regpass.equals(Regcpass)){
            Toast.makeText(this,"Password not match! Please Reenter Correct Password.", Toast.LENGTH_SHORT).show();

        }else{
            loadingregister.setTitle("Create New Account");
            loadingregister.setMessage("Please Wait!");
            loadingregister.show();
            loadingregister.setCanceledOnTouchOutside(true);

            mAuth.createUserWithEmailAndPassword(Regemail,Regpass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                SendEmailVerifyMessage();
                                loadingregister.dismiss();
                            }else {
                                String message = task.getException().getMessage();
                                Toast.makeText(Register.this,"Fail To Register : "+ message,Toast.LENGTH_SHORT).show();
                                loadingregister.dismiss();
                            }
                        }
                    });
        }
    }
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            inMainMenus();
        }
    }

    private void inMainMenus() {
        Intent SetupUser = new Intent(Register.this, Login.class);
        SetupUser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(SetupUser);
        finish();
    }


}
