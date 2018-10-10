package net.leon.myfypproject2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import net.leon.myfypproject2.Event.CreateEvent;
import net.leon.myfypproject2.FanClub.CreateClub;
import net.leon.myfypproject2.FanClub.FanClubMainMenu;
import net.leon.myfypproject2.Function.VIewVideo;
import net.leon.myfypproject2.Function.ViewImage;
import net.leon.myfypproject2.ProductMgnt.MyProductsView;
import net.leon.myfypproject2.ProductMgnt.ViewProduct;
import net.leon.myfypproject2.Function.ViewStream;
import net.leon.myfypproject2.Function.homefragment;
import net.leon.myfypproject2.ProductMgnt.UploadProducts;
import net.leon.myfypproject2.Purchase_in_app_credit.Payment;
import net.leon.myfypproject2.Purchase_in_app_credit.PurchaseCredit;
import net.leon.myfypproject2.UserAccount.Login;
import net.leon.myfypproject2.UserAccount.UserSetup;
import net.leon.myfypproject2.UserInterface.UserInterface;
import net.leon.myfypproject2.VipSubscription.VipMenu;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    private Button ProfileEdit,UserInterface;
    private CircleImageView NavProfileImg;
    private TextView NavProfileUsername,NavProfilename;



    String currentUserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        BottomNavigationView bnv = (BottomNavigationView) findViewById(R.id.nav_btn);
        bnv.setOnNavigationItemSelectedListener(navlistener);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        NavProfileImg = (CircleImageView)navView.findViewById(R.id.NavUserImage);
        NavProfileUsername = (TextView)navView.findViewById(R.id.Navprofileusername);
        NavProfilename = (TextView) navView.findViewById(R.id.Navprofilename);
        ProfileEdit = (Button)navView.findViewById(R.id.ProfileEdit);
        ProfileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, UserSetup.class);
                startActivity(i);
            }
        });


        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    if(dataSnapshot.hasChild("Fullname")){
                        String name = dataSnapshot.child("Fullname").getValue().toString();
                        NavProfilename.setText(name);
                    }
                    if(dataSnapshot.hasChild("username")){
                        String username = dataSnapshot.child("username").getValue().toString();
                        NavProfileUsername.setText(username);
                    }
                    if(dataSnapshot.hasChild("ProfilePicture")){
                        String profileimg = dataSnapshot.child("ProfilePicture").getValue().toString();
                        Picasso.get().load(profileimg).placeholder(R.drawable.userprofile).into(NavProfileImg);
                    }else {
                        Toast.makeText(MainActivity.this, "Profile name do not exists", Toast.LENGTH_SHORT).show();
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        unCheckAllMenuItems(navigationView.getMenu());

    }



    private BottomNavigationView.OnNavigationItemSelectedListener navlistener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedfragment = null;

            switch (item.getItemId()) {
                case R.id.image:
                    selectedfragment = new ViewImage();
                    break;
                case R.id.home:
                    selectedfragment = new homefragment();
                    break;
                case R.id.stream:
                    selectedfragment = new ViewStream();
                    break;
                case R.id.video:
                    selectedfragment = new VIewVideo();
                    break;
                case R.id.shop:
                    selectedfragment = new ViewProduct();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout3, selectedfragment).addToBackStack(null).commit();
            return true;
        }

    };
    private void unCheckAllMenuItems(@NonNull final Menu nav_menu) {
        int size = nav_menu.size();
        for (int i = 0; i < size; i++) {
            final MenuItem item =nav_menu.getItem(i);
            if(item.hasSubMenu()) {
                // Un check sub menu items
                unCheckAllMenuItems(item.getSubMenu());
            } else {
                item.setChecked(false);
            }
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }

    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            ToLogin();
        }else {
            UserChecking();
        }
    }

    private void UserChecking() {
        final String current_user_id = mAuth.getCurrentUser().getUid();

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(current_user_id)){
                    UserSetup();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void UserSetup() {
        Intent setuppage = new Intent(MainActivity.this, UserSetup.class);
        setuppage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setuppage);
        finish();
    }

    private void ToLogin() {
        Intent loginPage = new Intent(MainActivity.this,Login.class);
        loginPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginPage);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.User_profile_Interface) {
            Intent i = new Intent(MainActivity.this, UserInterface.class);
            startActivity(i);
            return true;
        } else if (id == R.id.Search_User){
            Intent i = new Intent(MainActivity.this, FindUserActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_event) {
            Intent i = new Intent(MainActivity.this, CreateEvent.class);
            startActivity(i);
            // Handle the camera action
        } else if (id == R.id.nav_fanclub) {
            Intent i = new Intent(MainActivity.this, PurchaseCredit.class);
            startActivity(i);

        } else if (id == R.id.nav_message) {

        } else if (id == R.id.nav_vipsubscription){
            Intent i = new Intent( MainActivity.this , VipMenu.class);
            startActivity(i);

        }else if (id == R.id.nav_profile) {
            Intent i = new Intent(MainActivity.this, UserInterface.class);
            startActivity(i);

        } else if (id == R.id.Logout) {
            Logout();

        }else if (id == R.id.nav_uploadproducts){
            Intent i = new Intent( MainActivity.this , MyProductsView.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void Logout() {
        AlertDialog.Builder builder;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){
            builder = new AlertDialog.Builder(this,android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }

        builder.setTitle("Logout")
                .setMessage("Do you want to Logout ? ")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAuth.signOut();
                        ToLogin();

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
