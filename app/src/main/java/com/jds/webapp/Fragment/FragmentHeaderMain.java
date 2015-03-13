package com.jds.webapp.Fragment;

import android.content.Context;
import android.opengl.Visibility;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

import com.jds.webapp.R;


public class FragmentHeaderMain extends Fragment implements OnKeyListener  {
    View btn1, btn2, btn3, btnClose;
    View mVw;
    View searchView;
    EditText searchText;
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
            btn3 = view.findViewById(R.id.btnSearch);
            btnClose = view.findViewById(R.id.closeBtn);
            searchView = view.findViewById(R.id.searchLayout);
            searchText = (EditText) view.findViewById(R.id.searchText);
            btnHomePressed();
            view.findViewById(R.id.btnHomePressed).setVisibility(View.VISIBLE);
            view.findViewById(R.id.btnSavedPressed).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.btnSearchPressed).setVisibility(View.INVISIBLE);

            btn1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new FragmentListArticle())
                            .commit();
                    btnHomePressed();
                    mVw.findViewById(R.id.btnHomePressed).setVisibility(View.VISIBLE);
                    mVw.findViewById(R.id.btnSavedPressed).setVisibility(View.INVISIBLE);
                    mVw.findViewById(R.id.btnSearchPressed).setVisibility(View.INVISIBLE);
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
                    mVw.findViewById(R.id.btnSearchPressed).setVisibility(View.INVISIBLE);
                }
            });
        btn3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchView.setVisibility(View.VISIBLE);
                btnSearchPressed();
                mVw.findViewById(R.id.btnHomePressed).setVisibility(View.INVISIBLE);
                mVw.findViewById(R.id.btnSavedPressed).setVisibility(View.INVISIBLE);
                mVw.findViewById(R.id.btnSearchPressed).setVisibility(View.VISIBLE);

            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchView.setVisibility(View.GONE);
                btnHomePressed();
                mVw.findViewById(R.id.btnHomePressed).setVisibility(View.VISIBLE);
                mVw.findViewById(R.id.btnSavedPressed).setVisibility(View.INVISIBLE);
                mVw.findViewById(R.id.btnSearchPressed).setVisibility(View.INVISIBLE);
                // Hides keyboard
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchText.getWindowToken(),0);
                searchText.setText("");
                getActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.container, new FragmentListArticle()).commit();
            }
        });
        searchText.setOnKeyListener(this);

            return view;
    }
    private void btnHomePressed(){

        btn1.setSelected(true);
        btn1.setEnabled(false);
        btn1.setClickable(false);
        btn2.setSelected(false);
        btn2.setEnabled(true);
        btn2.setClickable(true);
        btn3.setSelected(false);
        btn3.setEnabled(true);
        btn3.setClickable(true);
    }
    private void btnSavedPressed(){
        btn1.setSelected(false);
        btn1.setEnabled(true);
        btn1.setClickable(true);
        btn2.setSelected(true);
        btn2.setEnabled(false);
        btn2.setClickable(false);
        btn3.setSelected(false);
        btn3.setEnabled(true);
        btn3.setClickable(true);
    }
    private void btnSearchPressed(){
        btn1.setSelected(false);
        btn1.setEnabled(true);
        btn1.setClickable(true);
        btn2.setSelected(false);
        btn2.setEnabled(true);
        btn2.setClickable(true);
        btn3.setSelected(true);
        btn3.setEnabled(false);
        btn3.setClickable(false);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mVw = getView();
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        Log.v("FragmentHeader", "OnKey");
        if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
                keyCode == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            //"http://m.matome.id/search/midori"
            String strSearch = searchText.getText().toString();
            Bundle args= new Bundle();
            args.putString("keyword",strSearch);
            FragmentListArticle fragmentListArticle = new FragmentListArticle();
            fragmentListArticle.setArguments(args);
            getActivity().getSupportFragmentManager()
                    .beginTransaction().replace(R.id.container, fragmentListArticle,"search").commit();
        }
        return false;
    }
}
