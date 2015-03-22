package com.jds.webapp.Fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.view.View.OnKeyListener;
import android.widget.HorizontalScrollView;

import com.jds.webapp.PageManager;
import com.jds.webapp.R;


public class FragmentHeaderMain extends Fragment implements OnKeyListener {
    View btnHome, btnSaved, btnSearch, btnNav,btnClose;
    View btnHomePressed, btnSavedPressed, btnSearchPressed, btnNavPressed;
    View mVw;
    View topView, searchView, categoryView;
    View btnFashion, btnCosmetics, btnTravel, btnBeauty, btnGourmet, btnGoods, btnLife, btnApps;
    View btnFashionPressed, btnCosmeticsPressed, btnTravelPressed, btnBeautyPressed, btnGourmetPressed, btnGoodsPressed, btnLifePressed, btnAppsPressed;
    EditText searchText;
    String fromFragment, fromCategory;

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

        btnFashion = view.findViewById(R.id.btnFashion);
        btnCosmetics = view.findViewById(R.id.btnCosmetics);
        btnTravel = view.findViewById(R.id.btnTravel);
        btnBeauty = view.findViewById(R.id.btnBeauty);
        btnGourmet = view.findViewById(R.id.btnGourmet);
        btnGoods = view.findViewById(R.id.btnGoods);
        btnLife = view.findViewById(R.id.btnLife);
        btnApps = view.findViewById(R.id.btnApps);

        btnFashionPressed = view.findViewById(R.id.btnFashionPressed);
        btnCosmeticsPressed = view.findViewById(R.id.btnCosmeticsPressed);
        btnTravelPressed = view.findViewById(R.id.btnTravelPressed);
        btnBeautyPressed = view.findViewById(R.id.btnBeautyPressed);
        btnGourmetPressed = view.findViewById(R.id.btnGourmetPressed);
        btnGoodsPressed = view.findViewById(R.id.btnGoodsPressed);
        btnLifePressed = view.findViewById(R.id.btnLifePressed);
        btnAppsPressed = view.findViewById(R.id.btnAppsPressed);

        btnHome = view.findViewById(R.id.btnHome);
        btnSaved = view.findViewById(R.id.btnSaved);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnNav = view.findViewById(R.id.btnNav);
        btnClose = view.findViewById(R.id.closeBtn);

        btnHomePressed = view.findViewById(R.id.btnHomePressed);
        btnSavedPressed = view.findViewById(R.id.btnSavedPressed);
        btnSearchPressed = view.findViewById(R.id.btnSearchPressed);
        btnNavPressed = view.findViewById(R.id.btnNavPressed);

        searchView = view.findViewById(R.id.searchLayout);
        searchText = (EditText) view.findViewById(R.id.searchText);
        topView = view.findViewById(R.id.topLayout);
        categoryView = (HorizontalScrollView) view.findViewById(R.id.categoryLayout);

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
                topView.setVisibility(View.GONE);
            }
        });

        btnNav.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //checkFromCategory();
                btnNavPressed();
                loadFragmentCategory("1");
                btnFashion.setEnabled(false);btnCosmetics.setEnabled(true);
                btnTravel.setEnabled(true);btnBeauty.setEnabled(true);
                btnGourmet.setEnabled(true);btnGoods.setEnabled(true);
                btnLife.setEnabled(true);btnApps.setEnabled(true);

                btnFashionPressed.setVisibility(View.VISIBLE);
                btnCosmeticsPressed.setVisibility(View.INVISIBLE);
                btnTravelPressed.setVisibility(View.INVISIBLE);
                btnBeautyPressed.setVisibility(View.INVISIBLE);
                btnGourmetPressed.setVisibility(View.INVISIBLE);
                btnGoodsPressed.setVisibility(View.INVISIBLE);
                btnLifePressed.setVisibility(View.INVISIBLE);
                btnAppsPressed.setVisibility(View.INVISIBLE);

                btnHomePressed.setVisibility(View.INVISIBLE);
                btnSavedPressed.setVisibility(View.INVISIBLE);
                btnSearchPressed.setVisibility(View.INVISIBLE);
                btnNavPressed.setVisibility(View.VISIBLE);
                topView.setVisibility(View.VISIBLE);

                fromFragment = PageManager.getInstance().fromFragment = "FragmentNav";
                fromCategory = PageManager.getInstance().fromCategory = "Fashion";
            }
        });


        btnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchView.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
                searchText.setText("");

                if(fromFragment.equals("FragmentHome") ||fromFragment.equals("FragmentSearch") ) {
                    btnHomePressed();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction().replace(R.id.container, new FragmentListArticle()).commit();
                }
                else if(fromFragment.equals("FragmentSaved")){
                    btnSavedPressed();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction().replace(R.id.container, new FragmentListSavedArticle()).commit();
                }
                else if(fromFragment.equals("FragmentNav")){
                    btnNavPressed();
                    checkFromCategory();
                }
            }
        });
        searchText.setOnKeyListener(this);



        btnFashion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnFashionPressed();
            }
        });
        btnCosmetics.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnCosmeticsPressed();
            }
        });
        btnTravel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnTravelPressed();
            }
        });
        btnBeauty.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnBeautyPressed();
            }
        });
        btnGourmet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnGourmetPressed();
            }
        });
        btnGoods.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnGoodsPressed();
            }
        });
        btnLife.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnLifePressed();
            }
        });
        btnApps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnAppsPressed();
            }
        });
        return view;
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
            FragmentSearchArticle fragmentSearchArticle = new FragmentSearchArticle();
            fragmentSearchArticle.setArguments(args);
            getActivity().getSupportFragmentManager()
                    .beginTransaction().replace(R.id.container, fragmentSearchArticle).commit();
        }
        return false;
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

        btnNav.setSelected(false);
        btnNav.setEnabled(true);
        btnNav.setClickable(true);

        btnSearch.setVisibility(View.VISIBLE);
        btnHomePressed.setVisibility(View.VISIBLE);
        btnSavedPressed.setVisibility(View.INVISIBLE);
        btnSearchPressed.setVisibility(View.INVISIBLE);
        btnNavPressed.setVisibility(View.INVISIBLE);

        topView.setVisibility(View.GONE);
        fromFragment = PageManager.getInstance().fromFragment = "FragmentHome";
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
        btnNav.setSelected(false);
        btnNav.setEnabled(true);
        btnNav.setClickable(true);
        btnHomePressed.setVisibility(View.INVISIBLE);
        btnSavedPressed.setVisibility(View.VISIBLE);
        btnSearchPressed.setVisibility(View.INVISIBLE);
        btnNavPressed.setVisibility(View.INVISIBLE);
        topView.setVisibility(View.GONE);
        fromFragment = PageManager.getInstance().fromFragment = "FragmentSaved";
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
        btnNav.setSelected(false);
        btnNav.setEnabled(true);
        btnNav.setClickable(true);
        btnHomePressed.setVisibility(View.INVISIBLE);
        btnSavedPressed.setVisibility(View.INVISIBLE);
        btnSearchPressed.setVisibility(View.VISIBLE);
        btnNavPressed.setVisibility(View.INVISIBLE);
        searchView.setVisibility(View.VISIBLE);
        topView.setVisibility(View.GONE);
        fromFragment = PageManager.getInstance().fromFragment = "FragmentSearch";
    }
    private void btnNavPressed() {
        btnHome.setSelected(false);
        btnHome.setEnabled(true);
        btnHome.setClickable(true);
        btnSaved.setSelected(false);
        btnSaved.setEnabled(true);
        btnSaved.setClickable(true);
        btnSearch.setSelected(false);
        btnSearch.setEnabled(true);
        btnSearch.setClickable(true);
        btnNav.setSelected(true);
        btnNav.setEnabled(false);
        btnNav.setClickable(false);
        btnHomePressed.setVisibility(View.INVISIBLE);
        btnSavedPressed.setVisibility(View.INVISIBLE);
        btnSearchPressed.setVisibility(View.INVISIBLE);
        btnNavPressed.setVisibility(View.VISIBLE);
        topView.setVisibility(View.VISIBLE);
        fromFragment = PageManager.getInstance().fromFragment = "FragmentNav";
    }

    private void btnFashionPressed(){
        loadFragmentCategory("1");
        btnFashion.setEnabled(false);btnCosmetics.setEnabled(true);
        btnTravel.setEnabled(true);btnBeauty.setEnabled(true);
        btnGourmet.setEnabled(true);btnGoods.setEnabled(true);
        btnLife.setEnabled(true);btnApps.setEnabled(true);

        btnFashionPressed.setVisibility(View.VISIBLE);
        btnCosmeticsPressed.setVisibility(View.INVISIBLE);
        btnTravelPressed.setVisibility(View.INVISIBLE);
        btnBeautyPressed.setVisibility(View.INVISIBLE);
        btnGourmetPressed.setVisibility(View.INVISIBLE);
        btnGoodsPressed.setVisibility(View.INVISIBLE);
        btnLifePressed.setVisibility(View.INVISIBLE);
        btnAppsPressed.setVisibility(View.INVISIBLE);
        fromCategory = PageManager.getInstance().fromCategory = "Fashion";
    }
    private void btnCosmeticsPressed(){
        loadFragmentCategory("2");
        btnFashion.setEnabled(true);btnCosmetics.setEnabled(false);
        btnTravel.setEnabled(true);btnBeauty.setEnabled(true);
        btnGourmet.setEnabled(true);btnGoods.setEnabled(true);
        btnLife.setEnabled(true);btnApps.setEnabled(true);

        btnFashionPressed.setVisibility(View.INVISIBLE);
        btnCosmeticsPressed.setVisibility(View.VISIBLE);
        btnTravelPressed.setVisibility(View.INVISIBLE);
        btnBeautyPressed.setVisibility(View.INVISIBLE);
        btnGourmetPressed.setVisibility(View.INVISIBLE);
        btnGoodsPressed.setVisibility(View.INVISIBLE);
        btnLifePressed.setVisibility(View.INVISIBLE);
        btnAppsPressed.setVisibility(View.INVISIBLE);
        fromCategory = PageManager.getInstance().fromCategory = "Cosmetics";
    }
    private void btnTravelPressed(){
        loadFragmentCategory("3");
        btnFashion.setEnabled(true);btnCosmetics.setEnabled(true);
        btnTravel.setEnabled(false);btnBeauty.setEnabled(true);
        btnGourmet.setEnabled(true);btnGoods.setEnabled(true);
        btnLife.setEnabled(true);btnApps.setEnabled(true);

        btnFashionPressed.setVisibility(View.INVISIBLE);
        btnCosmeticsPressed.setVisibility(View.INVISIBLE);
        btnTravelPressed.setVisibility(View.VISIBLE);
        btnBeautyPressed.setVisibility(View.INVISIBLE);
        btnGourmetPressed.setVisibility(View.INVISIBLE);
        btnGoodsPressed.setVisibility(View.INVISIBLE);
        btnLifePressed.setVisibility(View.INVISIBLE);
        btnAppsPressed.setVisibility(View.INVISIBLE);
        fromCategory = PageManager.getInstance().fromCategory = "Travel";
    }
    private void btnBeautyPressed(){
        loadFragmentCategory("4");
        btnFashion.setEnabled(true);btnCosmetics.setEnabled(true);
        btnTravel.setEnabled(true);btnBeauty.setEnabled(false);
        btnGourmet.setEnabled(true);btnGoods.setEnabled(true);
        btnLife.setEnabled(true);btnApps.setEnabled(true);

        btnFashionPressed.setVisibility(View.INVISIBLE);
        btnCosmeticsPressed.setVisibility(View.INVISIBLE);
        btnTravelPressed.setVisibility(View.INVISIBLE);
        btnBeautyPressed.setVisibility(View.VISIBLE);
        btnGourmetPressed.setVisibility(View.INVISIBLE);
        btnGoodsPressed.setVisibility(View.INVISIBLE);
        btnLifePressed.setVisibility(View.INVISIBLE);
        btnAppsPressed.setVisibility(View.INVISIBLE);
        fromCategory = PageManager.getInstance().fromCategory = "Beauty";
    }
    private void btnGourmetPressed(){
        loadFragmentCategory("5");
        btnFashion.setEnabled(true);btnCosmetics.setEnabled(true);
        btnTravel.setEnabled(true);btnBeauty.setEnabled(true);
        btnGourmet.setEnabled(false);btnGoods.setEnabled(true);
        btnLife.setEnabled(true);btnApps.setEnabled(true);

        btnFashionPressed.setVisibility(View.INVISIBLE);
        btnCosmeticsPressed.setVisibility(View.INVISIBLE);
        btnTravelPressed.setVisibility(View.INVISIBLE);
        btnBeautyPressed.setVisibility(View.INVISIBLE);
        btnGourmetPressed.setVisibility(View.VISIBLE);
        btnGoodsPressed.setVisibility(View.INVISIBLE);
        btnLifePressed.setVisibility(View.INVISIBLE);
        btnAppsPressed.setVisibility(View.INVISIBLE);
        fromCategory = PageManager.getInstance().fromCategory = "Gourmet";
    }
    private void btnGoodsPressed(){
        loadFragmentCategory("6");
        btnFashion.setEnabled(true);btnCosmetics.setEnabled(true);
        btnTravel.setEnabled(true);btnBeauty.setEnabled(true);
        btnGourmet.setEnabled(true);btnGoods.setEnabled(false);
        btnLife.setEnabled(true);btnApps.setEnabled(true);

        btnFashionPressed.setVisibility(View.INVISIBLE);
        btnCosmeticsPressed.setVisibility(View.INVISIBLE);
        btnTravelPressed.setVisibility(View.INVISIBLE);
        btnBeautyPressed.setVisibility(View.INVISIBLE);
        btnGourmetPressed.setVisibility(View.INVISIBLE);
        btnGoodsPressed.setVisibility(View.VISIBLE);
        btnLifePressed.setVisibility(View.INVISIBLE);
        btnAppsPressed.setVisibility(View.INVISIBLE);
        fromCategory = PageManager.getInstance().fromCategory = "Goods";
    }
    private void btnLifePressed() {
        loadFragmentCategory("7");
        btnFashion.setEnabled(true);btnCosmetics.setEnabled(true);
        btnTravel.setEnabled(true);btnBeauty.setEnabled(true);
        btnGourmet.setEnabled(true);btnGoods.setEnabled(true);
        btnLife.setEnabled(false);btnApps.setEnabled(true);

        btnFashionPressed.setVisibility(View.INVISIBLE);
        btnCosmeticsPressed.setVisibility(View.INVISIBLE);
        btnTravelPressed.setVisibility(View.INVISIBLE);
        btnBeautyPressed.setVisibility(View.INVISIBLE);
        btnGourmetPressed.setVisibility(View.INVISIBLE);
        btnGoodsPressed.setVisibility(View.INVISIBLE);
        btnLifePressed.setVisibility(View.VISIBLE);
        btnAppsPressed.setVisibility(View.INVISIBLE);
        fromCategory = PageManager.getInstance().fromCategory = "Life";
    }
    private void btnAppsPressed() {
        loadFragmentCategory("8");
        btnFashion.setEnabled(true);btnCosmetics.setEnabled(true);
        btnTravel.setEnabled(true);btnBeauty.setEnabled(true);
        btnGourmet.setEnabled(true);btnGoods.setEnabled(true);
        btnLife.setEnabled(true);btnApps.setEnabled(false);

        btnFashionPressed.setVisibility(View.INVISIBLE);
        btnCosmeticsPressed.setVisibility(View.INVISIBLE);
        btnTravelPressed.setVisibility(View.INVISIBLE);
        btnBeautyPressed.setVisibility(View.INVISIBLE);
        btnGourmetPressed.setVisibility(View.INVISIBLE);
        btnGoodsPressed.setVisibility(View.INVISIBLE);
        btnLifePressed.setVisibility(View.INVISIBLE);
        btnAppsPressed.setVisibility(View.VISIBLE);
        fromCategory = PageManager.getInstance().fromCategory = "Apps";
    }


    private void checkFromFragment() {
        fromFragment = PageManager.getInstance().fromFragment;
        if (fromFragment.equals("FragmentMain") || fromFragment.equals("FragmentHome")) {
            btnHomePressed();
        } else if (fromFragment.equals("FragmentSaved")) {
            btnSavedPressed();
        } else if(fromFragment.equals("FragmentSearch")) {
            btnSearchPressed();
        } else if(fromFragment.equals("FragmentNav")) {
            btnNavPressed();
            checkFromCategory();
        }
    }


    private void checkFromCategory() {
        fromCategory = PageManager.getInstance().fromCategory;
        if (fromCategory.equals("Fashion")) {
            btnFashionPressed();
        } else if (fromCategory.equals("Cosmetics")) {
            btnCosmeticsPressed();
        } else if(fromCategory.equals("Travel")) {
            btnTravelPressed();
        } else if(fromCategory.equals("Beauty")) {
            btnBeautyPressed();
        } else if(fromCategory.equals("Gourmet")) {
            btnGourmetPressed();
        } else if(fromCategory.equals("Goods")) {
            btnGoodsPressed();
        } else if(fromCategory.equals("Life")) {
            btnLifePressed();
        } else if(fromCategory.equals("Apps")) {
            btnAppsPressed();
        }
    }


    private void loadFragmentCategory(String category) {
        FragmentCategoryArticle fragmentCategoryArticle = new FragmentCategoryArticle();
        Bundle args = new Bundle();
        args.putString("category", category);
        fragmentCategoryArticle.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,fragmentCategoryArticle )
                .commit();
    }
}
