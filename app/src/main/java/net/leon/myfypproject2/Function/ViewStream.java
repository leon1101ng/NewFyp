package net.leon.myfypproject2.Function;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.library.banner.BannerLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import net.leon.myfypproject2.Adapter.WebBannerAdapter;
import net.leon.myfypproject2.LiveStream.PlayStreamVideo;
import net.leon.myfypproject2.Model.LiveStream;
import net.leon.myfypproject2.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewStream extends Fragment {
    private RecyclerView livestreamlist;
    private Button startStream;
    private View inflatedView = null;
    private DatabaseReference LiveStreamRef;
    private FirebaseAuth mAuth;
    private String CurrentID, stream = "Streaming";


    public ViewStream() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       this.inflatedView = inflater.inflate(R.layout.fragment_view_stream, container, false);
        mAuth = FirebaseAuth.getInstance();
        CurrentID = mAuth.getCurrentUser().getUid();
       LiveStreamRef = FirebaseDatabase.getInstance().getReference().child("LiveStream");

        BannerLayout recyclerBanner =  inflatedView.findViewById(R.id.recycler1);
        List<String> list = new ArrayList<>();
        list.add("http://www.stufftaiwan.com/wp-content/uploads/2018/03/cover-800x475.jpg");
        list.add("https://startuplatte.com/wp-content/uploads/2016/05/Facebook-Live-Verified-Accounts-1080x663.jpg");
        list.add("https://cdn2.ettoday.net/images/2274/d2274603.jpg");
        list.add("https://www.justmcu.mcu.edu.tw/wp-content/uploads/2017/08/%E7%9B%B4%E6%92%AD%E5%BB%A3%E5%91%8A.png");
        WebBannerAdapter webBannerAdapter=new WebBannerAdapter(getContext(),list);
        recyclerBanner.setAdapter(webBannerAdapter);

        livestreamlist = (RecyclerView)inflatedView.findViewById(R.id.all_stream_list2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        livestreamlist.setLayoutManager(linearLayoutManager);
       
       DisplayAllStream();

        return inflatedView;
    }

    private void DisplayAllStream() {
        Query livestreamquery = LiveStreamRef.orderByChild("Status").startAt(stream).endAt(stream + "\uf8ff");
        FirebaseRecyclerAdapter<LiveStream,LiveStreamHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<LiveStream, LiveStreamHolder>(
                LiveStream.class,
                R.layout.livelayout,
                LiveStreamHolder.class,
                livestreamquery) {
            @Override
            protected void populateViewHolder(LiveStreamHolder viewHolder, LiveStream model, int position) {
                final String postkey = getRef(position).getKey();

                viewHolder.setFullname(model.getFullname());
                viewHolder.setLiveTitle(model.getLiveTitle());
                viewHolder.setPostImage(getContext(),model.getPostImage());
                viewHolder.postedimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent commentsIntent = new Intent(getActivity(), PlayStreamVideo.class);
                        commentsIntent.putExtra("PostKey", postkey);
                        startActivity(commentsIntent);
                    }
                });
            }
        };
        livestreamlist.setAdapter(firebaseRecyclerAdapter);
    }

    public static class LiveStreamHolder extends RecyclerView.ViewHolder{
        View mView;
        private ImageView postedimg;
        public LiveStreamHolder(View itemView){
            super(itemView);
            mView = itemView;
            postedimg = (ImageView) mView.findViewById(R.id.liveuserpic);
        }

        public void setFullname(String fullname) {
            TextView Liveuser = (TextView)mView.findViewById(R.id.StreamUserName);
            Liveuser.setText(fullname);

        }

        public void setLiveTitle(String liveTitle) {
            TextView livetex = (TextView)mView.findViewById(R.id.streamcap);
            livetex.setText(liveTitle);

        }

        public void setPostImage(Context ctx,String postImage) {
            Glide.with(ctx)
                    .load(postImage)
                    .into(postedimg);

        }

    }



    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle("The Starry");
    }

}
