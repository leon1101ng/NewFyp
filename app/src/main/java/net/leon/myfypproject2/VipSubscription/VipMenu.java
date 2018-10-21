package net.leon.myfypproject2.VipSubscription;

import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.leon.myfypproject2.FanClub.FanClubMainMenu;
import net.leon.myfypproject2.R;
import net.leon.myfypproject2.UserAccount.UserSetup;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class VipMenu extends AppCompatActivity {
    private Button buyvipbtn;
    private DatabaseReference UserRef;
    private FirebaseAuth mAuth;
    private String Current_user;
    private CircleImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_menu);

        mAuth = FirebaseAuth.getInstance();
        Current_user = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Current_user);

        buyvipbtn = (Button)findViewById(R.id.PurchaseVipBtn);
        buyvipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comfirmation();
            }
        });

        backBtn = (CircleImageView)findViewById(R.id.BackToMenu);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void Comfirmation() {
        AlertDialog.Builder builder;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){
            builder = new AlertDialog.Builder(this,android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }

        builder.setTitle("Buy Vip")
                .setMessage("20000 Coins to Purchase Vip")
                .setIcon(android.R.drawable.ic_menu_view)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CheckVip();


                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                })
                .show();
    }

    private void CheckVip() {

        UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    if(dataSnapshot.hasChild("Vip")){
                        int vip = 1;
                        int UserVip =  dataSnapshot.child("Vip").getValue(Integer.class);
                        if(UserVip != vip){
                            if(dataSnapshot.hasChild("InAppCredit")){
                                int VipPrice = 42000;
                                int credit =  dataSnapshot.child("InAppCredit").getValue(Integer.class);
                                int EndCredit = 0;
                                if(credit < VipPrice){
                                    Toast.makeText(VipMenu.this,"Insufficient Credit", Toast.LENGTH_SHORT).show();
                                }else {
                                    EndCredit = credit - VipPrice;
                                    PurchaseVip(EndCredit);
                                }
                            }

                        }else {
                            Toast.makeText(VipMenu.this,"You Are Already A VIP", Toast.LENGTH_SHORT).show();
                        }
                    }



                }else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void PurchaseVip(int EndCredit) {
        int vip = 1;
        HashMap userMap = new HashMap();
        userMap.put("InAppCredit", EndCredit);
        userMap.put("Vip", vip);
        UserRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    BackToVipMenu();
                    Toast.makeText(VipMenu.this, "Your Are Vip now", Toast.LENGTH_SHORT).show();
                }else {
                    String message = task.getException().getMessage();
                    Toast.makeText(VipMenu.this,"Failed To Purchase"+message,Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void BackToVipMenu() {
    }
}
