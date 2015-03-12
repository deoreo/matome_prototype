package com.jds.webapp.Fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jds.webapp.R;


public class FragmentHeaderMain extends Fragment {
    View btn1, btn2;
    View mVw;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_fragment_header_main, container, false);
        if (container == null) {
            return null;
        }
            btn1 = view.findViewById(R.id.btnHome);
            btn2 = view.findViewById(R.id.btnSaved);
            btnHomePressed();
            view.findViewById(R.id.btnHomePressed).setVisibility(View.VISIBLE);
            view.findViewById(R.id.btnSavedPressed).setVisibility(View.INVISIBLE);


            btn1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new FragmentListArticle())
                            .commit();
                    btnHomePressed();
                    mVw.findViewById(R.id.btnHomePressed).setVisibility(View.VISIBLE);
                    mVw.findViewById(R.id.btnSavedPressed).setVisibility(View.INVISIBLE);
                }
            });
            btn2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new FragmentListSavedArticle())
                            .commit();
                    btnSavedPressed();
                    mVw.findViewById(R.id.btnHomePressed).setVisibility(View.INVISIBLE);
                    mVw.findViewById(R.id.btnSavedPressed).setVisibility(View.VISIBLE);
                }
            });
            return view;
    }
    private void btnHomePressed(){

        btn1.setSelected(true);
        btn1.setEnabled(false);
        btn1.setClickable(false);
        btn2.setSelected(false);
        btn2.setEnabled(true);
        btn2.setClickable(true);
    }
    private void btnSavedPressed(){

        btn2.setSelected(true);
        btn2.setEnabled(false);
        btn2.setClickable(false);
        btn1.setSelected(false);
        btn1.setEnabled(true);
        btn1.setClickable(true);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mVw = getView();
    }
}
