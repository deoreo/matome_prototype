package com.jds.webapp.Fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jds.webapp.R;


public class FragmentTop extends Fragment {
    Button btn1, btn2;
    View mVw;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_fragment_top, container, false);
        if (container == null) {
            return null;
        }
        btn1 = (Button) view.findViewById(R.id.btn1);
        btn2 = (Button) view.findViewById(R.id.btn2);
        Btn1Pressed();
        view.findViewById(R.id.btn1Pressed).setVisibility(View.VISIBLE);
        view.findViewById(R.id.btn2Pressed).setVisibility(View.INVISIBLE);
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new FragmentHome())
                        .commit();
                Btn1Pressed();
                mVw.findViewById(R.id.btn1Pressed).setVisibility(View.VISIBLE);
                mVw.findViewById(R.id.btn2Pressed).setVisibility(View.INVISIBLE);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new FragmentListArticle())
                        .commit();
                Btn2Pressed();
                mVw.findViewById(R.id.btn1Pressed).setVisibility(View.INVISIBLE);
                mVw.findViewById(R.id.btn2Pressed).setVisibility(View.VISIBLE);
            }
        });
        return view;
    }

    private void Btn1Pressed(){

        btn1.setSelected(true);
        btn1.setEnabled(false);
        btn1.setClickable(false);
        btn2.setSelected(false);
        btn2.setEnabled(true);
        btn2.setClickable(true);
    }
    private void Btn2Pressed(){

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
