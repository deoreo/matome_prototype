package com.jds.webapp.Fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jds.webapp.PageManager;
import com.jds.webapp.R;


public class FragmentHeaderMain extends Fragment {
    View btnHome, btnSaved, btnClose;
    View btnHomePressed, btnSavedPressed;
    View mVw;
    String fromFragment;

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
        btnHome = view.findViewById(R.id.btnHome);
        btnSaved = view.findViewById(R.id.btnSaved);

        btnHomePressed = view.findViewById(R.id.btnHomePressed);
        btnSavedPressed = view.findViewById(R.id.btnSavedPressed);
        btnClose = view.findViewById(R.id.closeBtn);

        checkFromFragment();
        //btnHomePressed();

        btnHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new FragmentListArticle())
                        .commit();
                btnHomePressed();

            }
        });
        btnSaved.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new FragmentListSavedArticle())
                        .commit();
                btnSavedPressed();
            }
        });

    return view;
}


    private void btnHomePressed() {

        btnHome.setSelected(true);
        btnHome.setEnabled(false);
        btnHome.setClickable(false);
        btnSaved.setSelected(false);
        btnSaved.setEnabled(true);
        btnSaved.setClickable(true);
        btnHomePressed.setVisibility(View.VISIBLE);
        btnSavedPressed.setVisibility(View.INVISIBLE);
        PageManager.getInstance().fromFragment = "FragmentHome";
    }

    private void btnSavedPressed() {
        btnHome.setSelected(false);
        btnHome.setEnabled(true);
        btnHome.setClickable(true);
        btnSaved.setSelected(true);
        btnSaved.setEnabled(false);
        btnSaved.setClickable(false);
        btnHomePressed.setVisibility(View.INVISIBLE);
        btnSavedPressed.setVisibility(View.VISIBLE);
        PageManager.getInstance().fromFragment = "FragmentSaved";
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mVw = getView();
    }

    private void checkFromFragment() {
        fromFragment = PageManager.getInstance().fromFragment;
        if (fromFragment.equals("FragmentMain") || fromFragment.equals("FragmentHome")) {
            btnHomePressed();
        } else if (fromFragment.equals("FragmentSaved")) {
            btnSavedPressed();
        }
    }
}
