package net.leon.myfypproject2.Function;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.leon.myfypproject2.LiveStream.CameraActivity;
import net.leon.myfypproject2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewStream extends Fragment {
    private Button startStream;
    private View inflatedView = null;


    public ViewStream() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       this.inflatedView = inflater.inflate(R.layout.fragment_view_stream, container, false);
        startStream = (Button) inflatedView.findViewById(R.id.Start_stream);
        
        startStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenCamera();
            }
        });
        return inflatedView;
    }

    private void OpenCamera() {
        Intent i = new Intent(getActivity(), CameraActivity.class);
        startActivity(i);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle("Live Stream");
    }

}
