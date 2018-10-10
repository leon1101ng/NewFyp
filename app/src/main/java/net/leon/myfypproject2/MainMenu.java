package net.leon.myfypproject2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jgabrielfreitas.core.BlurImageView;

import net.leon.myfypproject2.UserInterface.UserInterface;

public class MainMenu extends AppCompatActivity {
    private ImageButton userprofile,userhome,dailynews;
    private BlurImageView bgiblur;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("大象直播");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        userprofile = (ImageButton)findViewById(R.id.UserProfile2);
        userprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Userprofile = new Intent(MainMenu.this, UserInterface.class);
                startActivity(Userprofile);
            }
        });
        userhome = (ImageButton)findViewById(R.id.UserHome);
        userhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Userhome = new Intent(MainMenu.this, MainActivity.class);
                startActivity(Userhome);
            }
        });
        dailynews = (ImageButton)findViewById(R.id.news);


    }
}
