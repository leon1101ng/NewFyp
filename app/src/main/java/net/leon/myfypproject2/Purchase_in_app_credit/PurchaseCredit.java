package net.leon.myfypproject2.Purchase_in_app_credit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import net.leon.myfypproject2.Common.Config;
import net.leon.myfypproject2.MainActivity;
import net.leon.myfypproject2.Model.UserClass;
import net.leon.myfypproject2.R;
import net.leon.myfypproject2.UserAccount.UserSetup;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class PurchaseCredit extends AppCompatActivity {
    private TextView Mycoin;
    private Button tu1,tu2,tu3,tu4,tu5,tu6;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef,AddCreditRef;
    private String CurrentUser,CurrentDate,CurrentTime,Postrandomname;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);
    private static final int PAYPAL_REQUEST_CODE = 1;
    private static final int PAYPAL_REQUEST_CODE2 = 2;
    private static final int PAYPAL_REQUEST_CODE3 = 3;
    private static final int PAYPAL_REQUEST_CODE4 = 4;
    private static final int PAYPAL_REQUEST_CODE5 = 5;
    private static final int PAYPAL_REQUEST_CODE6 = 6;
    private CircleImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_credit);
        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(CurrentUser);
        AddCreditRef = FirebaseDatabase.getInstance().getReference().child("AddCreditRecord").child(CurrentUser);
        Calendar caldate = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyyy");
        CurrentDate = currentdate.format(caldate.getTime());

        Calendar caltime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");
        CurrentTime = currenttime.format(caltime.getTime());
        Random rand = new Random();

        int  n = rand.nextInt(50) + 1;
        Postrandomname = CurrentDate + CurrentTime + n;

        back = (CircleImageView)findViewById(R.id.BackToMenu);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.hasChild("InAppCredit")){
                        int credit = dataSnapshot.child("InAppCredit").getValue(Integer.class);
                        Mycoin.setText(String.valueOf(credit));


                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        Intent i = new Intent(this, PayPalService.class);
        i.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(i);

        Mycoin = (TextView)findViewById(R.id.MyCoin);
        tu1 = (Button)findViewById(R.id.RM4_90);
        tu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processPayment();
            }
        });
        tu2 = (Button)findViewById(R.id.RM19_90);
        tu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processPayment1();
            }
        });
        tu3 = (Button)findViewById(R.id.RM39_90);
        tu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processPayment2();
            }
        });
        tu4 = (Button)findViewById(R.id.RM79_90);
        tu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processPayment3();
            }
        });
        tu5 = (Button)findViewById(R.id.RM99_90);
        tu5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processPayment4();

            }
        });
        tu6 = (Button)findViewById(R.id.RM199_90);
        tu6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processPayment5();
            }
        });

    }

    private void processPayment5() {
        AlertDialog.Builder builder;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){
            builder = new AlertDialog.Builder(this,android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }

        builder.setTitle("Buy Coin")
                .setMessage("Do You Want To Purchase 230000 coins ? ")
                .setIcon(android.R.drawable.ic_input_add)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        double money = 199.90;
                        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(money),
                                "MYR",
                                "Buy 230000 Coin",
                                PayPalPayment.PAYMENT_INTENT_SALE);
                        Intent topaypal = new Intent(getApplication(), PaymentActivity.class);
                        topaypal.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
                        topaypal.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
                        startActivityForResult(topaypal,PAYPAL_REQUEST_CODE6);


                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                })
                .show();
    }

    private void processPayment4() {
        AlertDialog.Builder builder;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){
            builder = new AlertDialog.Builder(this,android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }

        builder.setTitle("Buy Coin")
                .setMessage("Do You Want To Purchase 110000 coins ? ")
                .setIcon(android.R.drawable.ic_input_add)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        double money = 99.90;
                        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(money),
                                "MYR",
                                "Buy 110000 Coin",
                                PayPalPayment.PAYMENT_INTENT_SALE);
                        Intent topaypal = new Intent(getApplication(), PaymentActivity.class);
                        topaypal.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
                        topaypal.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
                        startActivityForResult(topaypal,PAYPAL_REQUEST_CODE5);


                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                })
                .show();
    }

    private void processPayment3() {
        AlertDialog.Builder builder;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){
            builder = new AlertDialog.Builder(this,android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }

        builder.setTitle("Buy Coin")
                .setMessage("Do You Want To Purchase 85000 coins ? ")
                .setIcon(android.R.drawable.ic_input_add)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        double money = 79.90;
                        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(money),
                                "MYR",
                                "Buy 85000 Coin",
                                PayPalPayment.PAYMENT_INTENT_SALE);
                        Intent topaypal = new Intent(getApplication(), PaymentActivity.class);
                        topaypal.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
                        topaypal.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
                        startActivityForResult(topaypal,PAYPAL_REQUEST_CODE4);


                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                })
                .show();
    }

    private void processPayment2() {
        AlertDialog.Builder builder;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){
            builder = new AlertDialog.Builder(this,android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }

        builder.setTitle("Buy Coin")
                .setMessage("Do You Want To Purchase 42000 coins ? ")
                .setIcon(android.R.drawable.ic_input_add)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        double money = 39.90;
                        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(money),
                                "MYR",
                                "Buy 42000 Coin",
                                PayPalPayment.PAYMENT_INTENT_SALE);
                        Intent topaypal = new Intent(getApplication(), PaymentActivity.class);
                        topaypal.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
                        topaypal.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
                        startActivityForResult(topaypal,PAYPAL_REQUEST_CODE3);


                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                })
                .show();
    }

    private void processPayment1() {
        AlertDialog.Builder builder;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){
            builder = new AlertDialog.Builder(this,android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }

        builder.setTitle("Buy Coin")
                .setMessage("Do You Want To Purchase 20000 coins ? ")
                .setIcon(android.R.drawable.ic_input_add)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        double money = 19.90;
                        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(money),
                                "MYR",
                                "Buy 20000 Coin",
                                PayPalPayment.PAYMENT_INTENT_SALE);
                        Intent topaypal = new Intent(getApplication(), PaymentActivity.class);
                        topaypal.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
                        topaypal.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
                        startActivityForResult(topaypal,PAYPAL_REQUEST_CODE2);


                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                })
                .show();
    }

    private void processPayment() {
        AlertDialog.Builder builder;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){
            builder = new AlertDialog.Builder(this,android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }

        builder.setTitle("Buy Coin")
                .setMessage("Do You Want To Purchase 4000 coins ? ")
                .setIcon(android.R.drawable.ic_input_add)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        double money = 4.90;
                        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(money),
                                "MYR",
                                "Buy 4000 Coin",
                                PayPalPayment.PAYMENT_INTENT_SALE);
                        Intent topaypal = new Intent(getApplication(), PaymentActivity.class);
                        topaypal.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
                        topaypal.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
                        startActivityForResult(topaypal,PAYPAL_REQUEST_CODE);


                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                })
                .show();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PAYPAL_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirmation != null){
                        UpdateBalance();

                }
            }
        }if(requestCode == PAYPAL_REQUEST_CODE2){
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    UpdateBalance1();

                }
            }
        }if(requestCode == PAYPAL_REQUEST_CODE3){
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    UpdateBalance2();

                }
            }
        }if(requestCode == PAYPAL_REQUEST_CODE4){
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    UpdateBalance3
                            ();

                }
            }
        }if(requestCode == PAYPAL_REQUEST_CODE5){
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    UpdateBalance4();

                }
            }
        }if(requestCode == PAYPAL_REQUEST_CODE6){
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    UpdateBalance5();

                }
            }
        }
    }

    private void UpdateBalance2() {
        UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.hasChild("InAppCredit")){
                        int credit = dataSnapshot.child("InAppCredit").getValue(Integer.class);
                        int newcredit = credit;
                        storedata2(newcredit);

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void storedata2(int newcredit) {
        int topup = 42000;
        int mycredit = topup + newcredit;
        AddCreditRef.child("InAppCredit").setValue(mycredit);
        AddCreditRef.child("TopUpRecord").child(CurrentUser + Postrandomname).child("Value").setValue(topup);
        AddCreditRef.child("TopUpRecord").child(CurrentUser + Postrandomname).child("Time").setValue(CurrentTime);
        AddCreditRef.child("TopUpRecord").child(CurrentUser + Postrandomname).child("Date").setValue(CurrentDate);
        finalbal();
    }

    private void UpdateBalance3() {
        UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.hasChild("InAppCredit")){
                        int credit = dataSnapshot.child("InAppCredit").getValue(Integer.class);
                        int newcredit = credit;
                        storedata3(newcredit);

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void storedata3(int newcredit) {
        int topup = 85000;
        int mycredit = topup + newcredit;
        AddCreditRef.child("InAppCredit").setValue(mycredit);
        AddCreditRef.child("TopUpRecord").child(CurrentUser + Postrandomname).child("Value").setValue(topup);
        AddCreditRef.child("TopUpRecord").child(CurrentUser + Postrandomname).child("Time").setValue(CurrentTime);
        AddCreditRef.child("TopUpRecord").child(CurrentUser + Postrandomname).child("Date").setValue(CurrentDate);
        finalbal();
    }

    private void UpdateBalance4() {
        UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.hasChild("InAppCredit")){
                        int credit = dataSnapshot.child("InAppCredit").getValue(Integer.class);
                        int newcredit = credit;
                        storedata4(newcredit);

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void storedata4(int newcredit) {
        int topup = 110000;
        int mycredit = topup + newcredit;
        AddCreditRef.child("InAppCredit").setValue(mycredit);
        AddCreditRef.child("TopUpRecord").child(CurrentUser + Postrandomname).child("Value").setValue(topup);
        AddCreditRef.child("TopUpRecord").child(CurrentUser + Postrandomname).child("Time").setValue(CurrentTime);
        AddCreditRef.child("TopUpRecord").child(CurrentUser + Postrandomname).child("Date").setValue(CurrentDate);
        finalbal();
    }

    private void UpdateBalance5() {
        UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.hasChild("InAppCredit")){
                        int credit = dataSnapshot.child("InAppCredit").getValue(Integer.class);
                        int newcredit5 = credit;
                        storedata5(newcredit5);

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void storedata5(int newcredit) {
        int topup = 230000;
        int mycredit = topup + newcredit;
        AddCreditRef.child("InAppCredit").setValue(mycredit);
        AddCreditRef.child("TopUpRecord").child(CurrentUser + Postrandomname).child("Value").setValue(topup);
        AddCreditRef.child("TopUpRecord").child(CurrentUser + Postrandomname).child("Time").setValue(CurrentTime);
        AddCreditRef.child("TopUpRecord").child(CurrentUser + Postrandomname).child("Date").setValue(CurrentDate);
        finalbal();
    }

    private void UpdateBalance1() {
        UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.hasChild("InAppCredit")){
                        int credit = dataSnapshot.child("InAppCredit").getValue(Integer.class);
                        int newcredit = credit;
                        storedata1(newcredit);

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void storedata1(int newcredit) {
        int topup = 20000;
        int mycredit = topup + newcredit;
        AddCreditRef.child("InAppCredit").setValue(mycredit);
        AddCreditRef.child("TopUpRecord").child(CurrentUser + Postrandomname).child("Value").setValue(topup);
        AddCreditRef.child("TopUpRecord").child(CurrentUser + Postrandomname).child("Time").setValue(CurrentTime);
        AddCreditRef.child("TopUpRecord").child(CurrentUser + Postrandomname).child("Date").setValue(CurrentDate);
        finalbal();
    }


    private void UpdateBalance() {
        UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.hasChild("InAppCredit")){
                        int credit = dataSnapshot.child("InAppCredit").getValue(Integer.class);
                        int newcredit = credit;
                        storedata(newcredit);

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void storedata(int newcredit) {
        int topup = 4800;
        int mycredit = topup + newcredit;
        AddCreditRef.child("InAppCredit").setValue(mycredit);
        AddCreditRef.child("TopUpRecord").child(CurrentUser + Postrandomname).child("Value").setValue(topup);
        AddCreditRef.child("TopUpRecord").child(CurrentUser + Postrandomname).child("Time").setValue(CurrentTime);
        AddCreditRef.child("TopUpRecord").child(CurrentUser + Postrandomname).child("Date").setValue(CurrentDate);
        finalbal();
    }

    private void finalbal() {
        AddCreditRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.hasChild("InAppCredit")){
                        int credit = dataSnapshot.child("InAppCredit").getValue(Integer.class);
                        UserRef.child("InAppCredit").setValue(credit);
                        Mycoin.setText(String.valueOf(credit));
                    }


                }
            }





            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
