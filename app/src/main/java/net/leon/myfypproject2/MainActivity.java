package net.leon.myfypproject2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.library.banner.BannerLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.picasso.Picasso;

import net.leon.myfypproject2.Adapter.WebBannerAdapter;
import net.leon.myfypproject2.CelebritySchedule.ManageSchedule;
import net.leon.myfypproject2.CelebritySchedule.ScheduleList;
import net.leon.myfypproject2.Event.EventMenu;
import net.leon.myfypproject2.FanClub.FanClubMainMenu;
import net.leon.myfypproject2.Function.VIewVideo;
import net.leon.myfypproject2.Function.ViewImage;
import net.leon.myfypproject2.Function.ViewProduct;
import net.leon.myfypproject2.Function.ViewStream;
import net.leon.myfypproject2.Function.homefragment;
import net.leon.myfypproject2.LiveStream.CameraActivity;
import net.leon.myfypproject2.ProductMgnt.MyProductsView;
import net.leon.myfypproject2.ProductMgnt.UserCart;
import net.leon.myfypproject2.UserAccount.Login;
import net.leon.myfypproject2.UserAccount.UserSetup;
import net.leon.myfypproject2.UserInterface.Imagepost;
import net.leon.myfypproject2.UserInterface.StatusPost;
import net.leon.myfypproject2.UserInterface.VideoPost;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener   {
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    private Button ProfileEdit,UserInterface;
    private CircleImageView NavProfileImg;
    private TextView NavProfileUsername,NavProfilename;
    private BottomNavigationView bnv;
    private Dialog myDialog,myDialog2;





    String currentUserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BannerLayout recyclerBanner =  findViewById(R.id.recycler);
        List<String> list = new ArrayList<>();
        list.add("https://partyflock.nl/images/party/351031_1000x544_469818/K-Pop-and-KHipHop.jpg");
        list.add("http://blog.trazy.com/wp-content/uploads/2016/12/seoul-music-awards-2018.jpg");
        list.add("https://s-media-cache-ak0.pinimg.com/originals/89/2e/0b/892e0bf0220f805349cb26910de2df8d.jpg");
        list.add("https://www.c-k-jpopnews.fr/wp-content/uploads/ckjpopnews_mnet-asian-music-awards.jpg");
        list.add("http://theseoulstory.com/wp-content/uploads/2018/06/ww1.jpg");
        list.add("https://1.bp.blogspot.com/-DJ_uszjaCwM/VtxQJSyNe_I/AAAAAAAAAUk/2FhTEcagl5U/s1600/LOGO%2BLOG.jpg");
        WebBannerAdapter webBannerAdapter=new WebBannerAdapter(this,list);

        recyclerBanner.setAdapter(webBannerAdapter);





        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        bnv = (BottomNavigationView) findViewById(R.id.nav_btn);
        bnv.setOnNavigationItemSelectedListener(navlistener);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bnv.setItemIconTintList(null);
        bnv.getMenu().findItem(R.id.stream).setChecked(true);
        myDialog = new Dialog(MainActivity.this);
         myDialog2 = new Dialog(MainActivity.this);



        View navView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        NavProfileImg = (CircleImageView)navView.findViewById(R.id.NavUserImage);
        NavProfileUsername = (TextView)navView.findViewById(R.id.Navprofileusername);
        NavProfilename = (TextView) navView.findViewById(R.id.Navprofilename);
        ProfileEdit = (Button)navView.findViewById(R.id.ProfileEdit);
        navigationView.setItemIconTintList(null);
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
                        FancyToast.makeText(MainActivity.this,"Profile name do not exists",FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        unCheckAllMenuItems(navigationView.getMenu());
        


    }
    public void ShowPopup(MenuItem item){
        ImageView stream,imagepost,videopost,statuspost;
        myDialog.setContentView(R.layout.popoutfunction);
        stream = (ImageView)myDialog.findViewById(R.id.gotostream);
        imagepost = (ImageView)myDialog.findViewById(R.id.gotopostimage);
        videopost = (ImageView)myDialog.findViewById(R.id.gotovideo);
        statuspost = (ImageView)myDialog.findViewById(R.id.gotostatus);
        stream.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                FancyToast.makeText(MainActivity.this,"Live Stream",FancyToast.LENGTH_LONG,FancyToast.DEFAULT,true).show();
                return true;
            }
        });
        imagepost.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                FancyToast.makeText(MainActivity.this,"Post Image",FancyToast.LENGTH_LONG,FancyToast.DEFAULT,true).show();
                return true;
            }
        });
        videopost.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                FancyToast.makeText(MainActivity.this,"Post Video",FancyToast.LENGTH_LONG,FancyToast.DEFAULT,true).show();
                return true;
            }
        });
        statuspost.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                FancyToast.makeText(MainActivity.this,"Post Status",FancyToast.LENGTH_LONG,FancyToast.DEFAULT,true).show();
                return true;
            }
        });


        imagepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Imagepost.class);
                startActivity(i);
            }
        });
        videopost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, VideoPost.class);
                startActivity(i);
            }
        });
        statuspost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, StatusPost.class);
                startActivity(i);
            }
        });



        myDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        myDialog.show();
    }



    private BottomNavigationView.OnNavigationItemSelectedListener navlistener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedfragment = null;
            final int mMenuId = item.getItemId();
            for (int i = 0; i < bnv.getMenu().size(); i++) {
                MenuItem menuItem = bnv.getMenu().getItem(i);
                boolean isChecked = menuItem.getItemId() == item.getItemId();
                menuItem.setChecked(isChecked);
            }

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
       if (id == R.id.Search_User){
            Intent i = new Intent(MainActivity.this, FindUserActivity.class);
            startActivity(i);
            return true;
        } else if(id == R.id.Function){

       }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_event) {
            Intent i = new Intent(MainActivity.this, EventMenu.class);
            startActivity(i);
            // Handle the camera action
        } else if (id == R.id.nav_fanclub) {
            Intent i = new Intent(MainActivity.this, FanClubMainMenu.class);
            startActivity(i);

        } else if (id == R.id.nav_message) {

        } else if (id == R.id.nav_celebrityschedule){
            Intent i = new Intent(MainActivity.this, ScheduleList.class);
            startActivity(i);

        }else if (id == R.id.nav_schedule){


            UsersRef.child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){

                        if(dataSnapshot.hasChild("Type")){
                            String type = "Celebrity";
                            String usertype = dataSnapshot.child("Type").getValue().toString();
                            if(usertype.equals(type)){
                                Intent i = new Intent(MainActivity.this, ManageSchedule.class);
                                startActivity(i);
                            }else {
                                FancyToast.makeText(MainActivity.this,"Only Celebrity Can Manage Schedule",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        }else if(id == R.id.nav_addcart){
            Intent i = new Intent( MainActivity.this , UserCart.class);
            startActivity(i);
        }
                else if (id == R.id.nav_vipsubscription){
            Intent i = new Intent( MainActivity.this , Infinite_card.class);
            startActivity(i);

        }else if (id == R.id.nav_profile) {
            Intent i = new Intent(MainActivity.this, net.leon.myfypproject2.UserInterface.UserInterface.class);
            startActivity(i);

        } else if (id == R.id.Logout) {
            Logout();

        }else if (id == R.id.nav_uploadproducts){
            UsersRef.child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){

                        if(dataSnapshot.hasChild("Vip")){
                            int vip = 1;
                            int UserVip =  dataSnapshot.child("Vip").getValue(Integer.class);
                            if(UserVip == vip){
                                Intent i = new Intent( MainActivity.this , MyProductsView.class);
                                startActivity(i);
                            }else {
                                FancyToast.makeText(MainActivity.this,"Only Vip Can Manage Products",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
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


    public void Streampopup(View view) {
        Button ToLive;
        myDialog2.setContentView(R.layout.streamtitledialog);
        ToLive = (Button)myDialog2.findViewById(R.id.NexttoStream);

        ToLive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   EditText livetitle  = (EditText)myDialog2.findViewById(R.id.LiveTitle);
                    String livetext = livetitle.getText().toString();;
                    if(TextUtils.isEmpty(livetext)){
                        FancyToast.makeText(MainActivity.this,"Please Enter Live Title",FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();
                    }else {
                        Intent i = new Intent(MainActivity.this, CameraActivity.class);
                        i.putExtra("LiveTitle", livetext);
                        startActivity(i);
                    }

                }


            });

        myDialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        myDialog2.show();

    }




}
