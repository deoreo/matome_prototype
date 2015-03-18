package com.jds.webapp.Fragment;

import android.content.Context;
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
import android.view.View.OnKeyListener;

import com.jds.webapp.PageManager;
import com.jds.webapp.R;


public class FragmentHeaderMain extends Fragment implements OnKeyListener {
    View btnHome, btnSaved, btnSearch, btnClose;
    View btnHomePressed, btnSavedPressed, btnSearchPressed;
    View mVw;
    View searchView;
    EditText searchText;
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
        btnSearch = view.findViewById(R.id.btnSearch);
        btnHomePressed = view.findViewById(R.id.btnHomePressed);
        btnSavedPressed = view.findViewById(R.id.btnSavedPressed);
        btnSearchPressed = view.findViewById(R.id.btnSearchPressed);
        btnClose = view.findViewById(R.id.closeBtn);
        searchView = view.findViewById(R.id.searchLayout);
        searchText = (EditText) view.findViewById(R.id.searchText);

        checkFromFragment();

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
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                searchView.setVisibility(View.VISIBLE);
                btnSearchPressed();


            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchView.setVisibility(View.GONE);
                checkFromFragment();
                // Hides keyboard
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
                searchText.setText("");
                if(fromFragment.equals("FragmentHome")) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction().replace(R.id.container, new FragmentListArticle()).commit();
                }
                else if(fromFragment.equals("FragmentSaved")){
                    getActivity().getSupportFragmentManager()
                            .beginTransaction().replace(R.id.container, new FragmentListSavedArticle()).commit();
                }
            }
        });
        searchText.setOnKeyListener(this);

        return view;
    }

    private void btnHomePressed() {

        btnHome.setSelected(true);
        btnHome.setEnabled(false);
        btnHome.setClickable(false);
        btnSaved.setSelected(false);
        btnSaved.setEnabled(true);
        btnSaved.setClickable(true);
        btnSearch.setSelected(false);
        btnSearch.setEnabled(true);
        btnSearch.setClickable(true);
        btnHomePressed.setVisibility(View.VISIBLE);
        btnSavedPressed.setVisibility(View.INVISIBLE);
        btnSearchPressed.setVisibility(View.INVISIBLE);
        PageManager.getInstance().fromFragment = "FragmentHome";
    }

    private void btnSavedPressed() {
        btnHome.setSelected(false);
        btnHome.setEnabled(true);
        btnHome.setClickable(true);
        btnSaved.setSelected(true);
        btnSaved.setEnabled(false);
        btnSaved.setClickable(false);
        btnSearch.setSelected(false);
        btnSearch.setEnabled(true);
        btnSearch.setClickable(true);
        btnHomePressed.setVisibility(View.INVISIBLE);
        btnSavedPressed.setVisibility(View.VISIBLE);
        btnSearchPressed.setVisibility(View.INVISIBLE);
        PageManager.getInstance().fromFragment = "FragmentSaved";
    }

    private void btnSearchPressed() {
        btnHome.setSelected(false);
        btnHome.setEnabled(true);
        btnHome.setClickable(true);
        btnSaved.setSelected(false);
        btnSaved.setEnabled(true);
        btnSaved.setClickable(true);
        btnSearch.setSelected(true);
        btnSearch.setEnabled(false);
        btnSearch.setClickable(false);
        btnHomePressed.setVisibility(View.INVISIBLE);
        btnSavedPressed.setVisibility(View.INVISIBLE);
        btnSearchPressed.setVisibility(View.VISIBLE);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mVw = getView();
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
                keyCode == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            String strSearch = searchText.getText().toString();
            Bundle args = new Bundle();
            args.putString("keyword", strSearch);
            FragmentListArticle fragmentListArticle = new FragmentListArticle();
            fragmentListArticle.setArguments(args);
            getActivity().getSupportFragmentManager()
                    .beginTransaction().replace(R.id.container, fragmentListArticle, "search").commit();
        }
        return false;
    }


    private void checkFromFragment(){
        fromFragment= PageManager.getInstance().fromFragment;
        if(fromFragment.equals("FragmentMain") || fromFragment.equals("FragmentHome")){
            btnHomePressed();
        }
        else if(fromFragment.equals("FragmentSaved")){
            btnSavedPressed();

        }
        else if(fromFragment.equals("FragmentSearch")){
            btnSearchPressed();
        }
    }
}
