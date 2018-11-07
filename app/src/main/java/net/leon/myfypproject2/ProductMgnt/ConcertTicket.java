package net.leon.myfypproject2.ProductMgnt;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.leon.myfypproject2.R;

import java.util.ArrayList;

import fancytoastlib.shashank.sony.my.FancyToast;

public class ConcertTicket extends AppCompatActivity {
    private ImageView s1,s2,s3,s4,s5,s6,s7,s8,s9,s10,s11,s12,s13,s14,s15,s16,s17,s18,s19,s20,s21,s22,s23,s24,s25,
    s26,s27,s28,s29,s30,s31,s32,s33,s34,s35,s36,s37,s38,s39,s40,s41,s42,s43,s44,s45,s46,s47,s48;
    Boolean CheckLike = false;
    private DatabaseReference TicketRef,UserRef,ProductsViewRef;
    private FirebaseAuth mAuth;
    private String CurrentUser,postKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concert_ticket);
        mAuth = FirebaseAuth.getInstance();

        postKey = getIntent().getExtras().get("PostKey").toString();
        CurrentUser = mAuth.getCurrentUser().getUid();
        ProductsViewRef = FirebaseDatabase.getInstance().getReference().child("Products").child(postKey).child("TicketSeats");
        TicketRef = FirebaseDatabase.getInstance().getReference().child("Ticket");
        s1 = (ImageView)findViewById(R.id.seat1);
        s2 = (ImageView)findViewById(R.id.seat2);
        s3 = (ImageView)findViewById(R.id.seat3);
        s4 = (ImageView)findViewById(R.id.seat4);
        s5 = (ImageView)findViewById(R.id.seat5);
        s6 = (ImageView)findViewById(R.id.seat6);
        s7 = (ImageView)findViewById(R.id.seat7);
        s8 = (ImageView)findViewById(R.id.seat8);
        s9 = (ImageView)findViewById(R.id.seat9);
        s10 = (ImageView)findViewById(R.id.seat10);
        s11 = (ImageView)findViewById(R.id.seat11);
        s12 = (ImageView)findViewById(R.id.seat12);
        s13 = (ImageView)findViewById(R.id.seat13);
        s14 = (ImageView)findViewById(R.id.seat14);
        s15 = (ImageView)findViewById(R.id.seat15);
        s16 = (ImageView)findViewById(R.id.seat16);
        s17 = (ImageView)findViewById(R.id.seat17);
        s18 = (ImageView)findViewById(R.id.seat18);
        s19 = (ImageView)findViewById(R.id.seat19);
        s20 = (ImageView)findViewById(R.id.seat20);
        s21 = (ImageView)findViewById(R.id.seat21);
        s22 = (ImageView)findViewById(R.id.seat22);
        s23 = (ImageView)findViewById(R.id.seat23);
        s24 = (ImageView)findViewById(R.id.seat24);
        s25 = (ImageView)findViewById(R.id.seat25);
        s26 = (ImageView)findViewById(R.id.seat26);
        s27 = (ImageView)findViewById(R.id.seat27);
        s28 = (ImageView)findViewById(R.id.seat28);
        s29 = (ImageView)findViewById(R.id.seat29);
        s30 = (ImageView)findViewById(R.id.seat30);
        s31 = (ImageView)findViewById(R.id.seat31);
        s32 = (ImageView)findViewById(R.id.seat32);
        s33 = (ImageView)findViewById(R.id.seat33);
        s34 = (ImageView)findViewById(R.id.seat34);
        s35 = (ImageView)findViewById(R.id.seat35);
        s36 = (ImageView)findViewById(R.id.seat36);
        s37 = (ImageView)findViewById(R.id.seat37);
        s38 = (ImageView)findViewById(R.id.seat38);
        s39 = (ImageView)findViewById(R.id.seat39);
        s40 = (ImageView)findViewById(R.id.seat40);
        s41 = (ImageView)findViewById(R.id.seat41);
        s42 = (ImageView)findViewById(R.id.seat42);
        s43 = (ImageView)findViewById(R.id.seat43);
        s44 = (ImageView)findViewById(R.id.seat44);
        s45 = (ImageView)findViewById(R.id.seat45);
        s46 = (ImageView)findViewById(R.id.seat46);
        s47 = (ImageView)findViewById(R.id.seat47);
        s48 = (ImageView)findViewById(R.id.seat48);
        final String i1 = "1";
        final String i2 = "2";
        final String i3 = "3";
        final String i4 = "4";
        final String i5 = "5";
        final String i6 = "6";
        final String i7 = "7";
        final String i8 = "8";
        final String i9 = "9";
        final String i10 = "10";

        final ArrayList<String> ar = new ArrayList<String>();
        ar.add(i1);
        setSeatStatus(i1);
        setSeatStatus2(i2);
        setSeatStatus3(i3);
        setSeatStatus4(i4);
        setSeatStatus5(i5);


        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                ProductsViewRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        CheckLike = true;


                                        if(CheckLike.equals(true)){

                                            if(dataSnapshot.child(i1).exists()){
                                                FancyToast.makeText(ConcertTicket.this,"Booked Seats", FancyToast.LENGTH_LONG, FancyToast.INFO,true).show();
                                                CheckLike = false;
                                            }else {
                                                AlertDialog.Builder builder;
                                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){
                                                    builder = new AlertDialog.Builder(ConcertTicket.this,android.R.style.Theme_Material_Dialog_Alert);
                                                } else {
                                                    builder = new AlertDialog.Builder(ConcertTicket.this);
                                                }

                                                builder.setTitle("Book Seat")
                                                        .setMessage("You Cant Cancel Seats After Comfirm! ")
                                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, final int i) {
                                                                checkUser1(i1);
                                                            }
                                                        })
                                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {


                                                            }
                                                        })
                                                        .show();

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });










            }

            private void checkUser1(String i1) {
                ProductsViewRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            String user = dataSnapshot.child("TicketUser").getValue().toString();
                            if(user.equals(CurrentUser)){
                                FancyToast.makeText(ConcertTicket.this,"You Only Can Choose 1 Place", FancyToast.LENGTH_LONG, FancyToast.ERROR,true).show();
                            }
                        }else {
                            FancyToast.makeText(ConcertTicket.this, "Booked Seats", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                            ProductsViewRef.child(i1).child(CurrentUser).setValue(true);
                            ProductsViewRef.child(i1).child("Status").setValue("Booked");
                            ProductsViewRef.child("TicketUser").setValue(CurrentUser);
                            CheckLike = false;
                        }



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ProductsViewRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        CheckLike = true;


                        if(CheckLike.equals(true)){

                            if(dataSnapshot.child(i2).exists()){
                                FancyToast.makeText(ConcertTicket.this,"Booked Seats", FancyToast.LENGTH_LONG, FancyToast.INFO,true).show();
                                CheckLike = false;
                            }else {
                                AlertDialog.Builder builder;
                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){
                                    builder = new AlertDialog.Builder(ConcertTicket.this,android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(ConcertTicket.this);
                                }

                                builder.setTitle("Book Seat")
                                        .setMessage("You Cant Cancel Seats After Comfirm! ")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, final int i) {
                                                checkUser2(i2);


                                        }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {


                                            }
                                        })
                                        .show();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });










            }
        });

        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                ProductsViewRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        CheckLike = true;


                        if(CheckLike.equals(true)){

                            if(dataSnapshot.child(i3).exists()){
                                FancyToast.makeText(ConcertTicket.this,"Booked Seats", FancyToast.LENGTH_LONG, FancyToast.INFO,true).show();
                                CheckLike = false;
                            }else {
                                AlertDialog.Builder builder;
                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){
                                    builder = new AlertDialog.Builder(ConcertTicket.this,android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(ConcertTicket.this);
                                }

                                builder.setTitle("Book Seat")
                                        .setMessage("You Cant Cancel Seats After Comfirm! ")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, final int i) {
                                                checkUser3(i3);
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {


                                            }
                                        })
                                        .show();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });










            }

            private void checkUser3(String i3) {
                ProductsViewRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            String user = dataSnapshot.child("TicketUser").getValue().toString();
                            if(user.equals(CurrentUser)){
                                FancyToast.makeText(ConcertTicket.this,"You Only Can Choose 1 Place", FancyToast.LENGTH_LONG, FancyToast.ERROR,true).show();
                            }
                        }else {
                            FancyToast.makeText(ConcertTicket.this, "Booked Seats", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                            ProductsViewRef.child(i3).child(CurrentUser).setValue(true);
                            ProductsViewRef.child(i3).child("Status").setValue("Booked");
                            ProductsViewRef.child("TicketUser").setValue(CurrentUser);
                            CheckLike = false;
                        }



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        s4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                ProductsViewRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        CheckLike = true;


                        if(CheckLike.equals(true)){

                            if(dataSnapshot.child(i4).exists()){
                                FancyToast.makeText(ConcertTicket.this,"Booked Seats", FancyToast.LENGTH_LONG, FancyToast.INFO,true).show();
                                CheckLike = false;
                            }else {
                                AlertDialog.Builder builder;
                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){
                                    builder = new AlertDialog.Builder(ConcertTicket.this,android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(ConcertTicket.this);
                                }

                                builder.setTitle("Book Seat")
                                        .setMessage("You Cant Cancel Seats After Comfirm! ")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, final int i) {
                                               checkUser4(i4);
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {


                                            }
                                        })
                                        .show();

                            }
                        }
                    }

                    private void checkUser4(String i4) {
                        ProductsViewRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists())
                                {
                                    String user = dataSnapshot.child("TicketUser").getValue().toString();
                                    if(user.equals(CurrentUser)){
                                        FancyToast.makeText(ConcertTicket.this,"You Only Can Choose 1 Place", FancyToast.LENGTH_LONG, FancyToast.ERROR,true).show();
                                    }
                                }else {
                                    FancyToast.makeText(ConcertTicket.this, "Booked Seats", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                                    ProductsViewRef.child(i4).child(CurrentUser).setValue(true);
                                    ProductsViewRef.child(i4).child("Status").setValue("Booked");
                                    ProductsViewRef.child("TicketUser").setValue(CurrentUser);
                                    CheckLike = false;
                                }



                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });










            }
        });

        s5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                ProductsViewRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        CheckLike = true;


                        if(CheckLike.equals(true)){

                            if(dataSnapshot.child(i5).exists()){
                                FancyToast.makeText(ConcertTicket.this,"Booked Seats", FancyToast.LENGTH_LONG, FancyToast.INFO,true).show();
                                CheckLike = false;
                            }else {
                                AlertDialog.Builder builder;
                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){
                                    builder = new AlertDialog.Builder(ConcertTicket.this,android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(ConcertTicket.this);
                                }

                                builder.setTitle("Book Seat")
                                        .setMessage("You Cant Cancel Seats After Comfirm! ")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, final int i) {
                                                checkUser5(i5);
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {


                                            }
                                        })
                                        .show();

                            }
                        }
                    }

                    private void checkUser5(String i5) {
                        ProductsViewRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists())
                                {
                                    String user = dataSnapshot.child("TicketUser").getValue().toString();
                                    if(user.equals(CurrentUser)){
                                        FancyToast.makeText(ConcertTicket.this,"You Only Can Choose 1 Place", FancyToast.LENGTH_LONG, FancyToast.ERROR,true).show();
                                    }
                                }else {
                                    FancyToast.makeText(ConcertTicket.this, "Booked Seats", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                                    ProductsViewRef.child(i5).child(CurrentUser).setValue(true);
                                    ProductsViewRef.child(i5).child("Status").setValue("Booked");
                                    ProductsViewRef.child("TicketUser").setValue(CurrentUser);
                                    CheckLike = false;
                                }



                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });










            }
        });



    }

    private void checkUser2(final String i2) {

        ProductsViewRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String user = dataSnapshot.child("TicketUser").getValue().toString();
                    if(user.equals(CurrentUser)){
                        FancyToast.makeText(ConcertTicket.this,"You Only Can Choose 1 Place", FancyToast.LENGTH_LONG, FancyToast.ERROR,true).show();
                    }
                }else {
                    FancyToast.makeText(ConcertTicket.this, "Booked Seats", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                    ProductsViewRef.child(i2).child(CurrentUser).setValue(true);
                    ProductsViewRef.child(i2).child("Status").setValue("Booked");
                    ProductsViewRef.child("TicketUser").setValue(CurrentUser);
                    CheckLike = false;
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setSeatStatus5(final String i5) {
        ProductsViewRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(i5).exists()){
                    s5.setBackgroundColor(Color.parseColor("#FF0000"));
                }else {
                    s5.setBackgroundColor(Color.parseColor("#228B22"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
    }

    private void setSeatStatus4(final String i4) {
        ProductsViewRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(i4).exists()){
                    s4.setBackgroundColor(Color.parseColor("#FF0000"));
                }else {
                    s4.setBackgroundColor(Color.parseColor("#228B22"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
    }

    private void setSeatStatus3(final String i3) {
        ProductsViewRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(i3).exists()){
                    s3.setBackgroundColor(Color.parseColor("#FF0000"));
                }else {
                    s3.setBackgroundColor(Color.parseColor("#228B22"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
    }


    private void setSeatStatus2(final String i2) {
        ProductsViewRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(i2).exists()){
                    s2.setBackgroundColor(Color.parseColor("#FF0000"));
                }else {
                    s2.setBackgroundColor(Color.parseColor("#228B22"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
    }


    private void setSeatStatus(final String i1) {
        ProductsViewRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(i1).exists()){
                    s1.setBackgroundColor(Color.parseColor("#FF0000"));
                }else {
                    s1.setBackgroundColor(Color.parseColor("#228B22"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
    }
}
