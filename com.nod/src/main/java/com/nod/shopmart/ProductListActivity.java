package com.nod.shopmart;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nod.shopmart.adaptor.ProductListAdaptor;
import com.nod.shopmart.adaptor.ViewpagerAdaptor;
import com.nod.shopmart.app.MyVolley;
import com.nod.shopmart.model.Facets;
import com.nod.shopmart.model.Folders;
import com.nod.shopmart.model.IDataModel;
import com.nod.shopmart.model.Product;
import com.nod.shopmart.model.ShopData;
import com.nod.shopmart.net.GsonRequest;
import com.nod.shopmart.utils.AppUtils;
import com.nod.shopmart.utils.Constant;
import com.nod.shopmart.utils.UrlBuilder;
import com.nod.shopmart.widget.SlidingTabLayout;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;

public class ProductListActivity extends AppCompatActivity implements Response.Listener<IDataModel>, Response.ErrorListener, EventListener, View.OnClickListener, SearchView.OnQueryTextListener {


    private int mPastVisiblesItems, mVisibleItemCount, mTotalItemCount;
    private boolean isLoading, isNextPageLoad, mDoubleBackToExitPressedOnce;
    private String mSearchQuery = "mobiles";
    private ArrayList<Product> mMainProductList;

    private View mprogressBar;
    private TextView mHeaderText;
    private ViewPager mViewPager;
    private RecyclerView mProductListView;
    private SlidingTabLayout mTabLayout;
    private MenuItem mSearchItem;

    private LinearLayoutManager mLinearlayoutManager;
    private ProductListAdaptor mListAdaptor;
    private ViewpagerAdaptor mViewPagerAdaptor;
    private ShopData mResponseData;
    private SearchView mSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        initViews();
        loadData();
    }

    private void initViews() {
        mProductListView = (RecyclerView) findViewById(R.id.product_list);
        mHeaderText = (TextView) findViewById(R.id.header_text);
        mprogressBar = findViewById(R.id.progress_bar);
        mLinearlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mProductListView.setLayoutManager(mLinearlayoutManager);
        mProductListView.setItemAnimator(new DefaultItemAnimator());
        setSupportActionBar((Toolbar) findViewById(R.id.tabanim_toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setCustomView(R.layout.search_view);
        }
        mViewPager = (ViewPager) findViewById(R.id.tabanim_viewpager);
        mTabLayout = (SlidingTabLayout) findViewById(R.id.main_tabs);
        mTabLayout.setCustomTabView(R.layout.custom_tab_lyt, R.id.tab_text_custom);

        mListAdaptor = new ProductListAdaptor(this);
        SlideInBottomAnimationAdapter slideInBottomAnimationAdapter = new SlideInBottomAnimationAdapter(mListAdaptor);
        mProductListView.setAdapter(new ScaleInAnimationAdapter(slideInBottomAnimationAdapter));
        registerPaginationListener();
        registerPageChangeListener();
    }

    private void registerPageChangeListener() {
        mTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // mViewPager.setVisibility(View.VISIBLE);
                updateFilterCount();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void registerPaginationListener() {
        mProductListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mVisibleItemCount = mLinearlayoutManager.getChildCount();
                mTotalItemCount = mLinearlayoutManager.getItemCount();
                mPastVisiblesItems = mLinearlayoutManager.findFirstVisibleItemPosition();
                if (!isLoading) {
                    if ((mVisibleItemCount + mPastVisiblesItems) >= mTotalItemCount) {
                        isLoading = true;
                        if (mResponseData.getNext() != null && !mResponseData.getNext().trim().equalsIgnoreCase("")) {
                            loadMore(UrlBuilder.createUrl(true, mResponseData.getNext()), false);
                        }
                        AppUtils.log("LOG", "loadmorepage");
                    }
                }
            }
        });
    }

    private void loadData() {
        if (AppUtils.isNetworkAvailable(getApplicationContext())) {
            showPrgressBar();
            AppUtils.log("LOG", "product url " + Constant.LAUNCH_URL);
            MyVolley.getRequestQueue(this).add(new GsonRequest(Constant.LAUNCH_URL, new ShopData(), this, this));
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.no_network_dialog), Toast.LENGTH_LONG).show();
        }
    }

    private void showPrgressBar() {
        mprogressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressbar() {
        mprogressBar.setVisibility(View.GONE);
    }

    private void loadMore(String url, boolean isFilterLoad) {
        if (AppUtils.isNetworkAvailable(getApplicationContext())) {
            showPrgressBar();
            AppUtils.log("LOG", "product url " + url.toString());
            isNextPageLoad = !isFilterLoad;
            MyVolley.getRequestQueue(this).add(new GsonRequest(url.toString(), new ShopData(), this, this));
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.no_network_dialog), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResponse(IDataModel response) {

        isLoading = false;
        if (response != null && response instanceof ShopData) {
            ShopData productData = (ShopData) response;
            if (isNextPageLoad) {
                isNextPageLoad = false;
                addNextPageproduct(productData);
            } else {
                mResponseData = productData;
                mMainProductList = mResponseData.getProducts();
            }
            updateUI();
        }
    }

    private void addNextPageproduct(ShopData productData) {
        ArrayList<Product> newProductList = productData.getProducts();
        mMainProductList.addAll(newProductList);
        mResponseData.setNext(productData.getNext());
        mResponseData.setTotal(productData.getTotal());
    }

    private void updateUI() {
        hideProgressbar();
        ArrayList<String> filterTitle = new ArrayList<>();
        for (Folders folder : mResponseData.getFolders()) {
            filterTitle.add(folder.getName());
        }
        mViewPagerAdaptor = new ViewpagerAdaptor(this, mResponseData.getFolders(), filterTitle);
        mViewPager.setAdapter(mViewPagerAdaptor);
        mTabLayout.setViewPager(mViewPager);
        mViewPager.setCurrentItem(-1);
        mListAdaptor.updateList(mMainProductList);
        if (mSearchQuery != null) {
            mHeaderText.setText(mSearchQuery + " (" + mResponseData.getTotal() + ")");
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_product_list, menu);
        mSearchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (mSearchItem != null) {
            mSearchView = (SearchView) mSearchItem.getActionView();
        }
        if (mSearchView != null) {
            mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            mSearchView.isIconified();
            mSearchView.setOnQueryTextListener(this);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (mViewPager != null && mViewPager.getVisibility() == View.VISIBLE) {
            mViewPager.setVisibility(View.GONE);
        } else {
            if (mDoubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.mDoubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    mDoubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public void onCheckedListener(boolean ischecked, String tag) {
        if (ischecked) {
            UrlBuilder.addTag(tag);
        } else {
            UrlBuilder.removeTag(tag);
        }
        updateFilterCount();
    }

    private void updateFilterCount() {
        if (mViewPager != null) {
            View view = mViewPager.findViewWithTag(mViewPager.getCurrentItem());
            if (view != null) {
                if (UrlBuilder.getSelectedFilterCount() == 0) {
                    ((TextView) view.findViewById(R.id.clear_filter)).setText("Clear All");
                } else {
                    ((TextView) view.findViewById(R.id.clear_filter)).setText("Clear All(" + UrlBuilder.getSelectedFilterCount() + ")");
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.apply_filter:
                loadFilteredData();
                break;
            case R.id.clear_filter:
                clearFilterSelection();
                break;


        }
    }

    private void clearFilterSelection() {
        UrlBuilder.clear();
        ArrayList<Folders> folderList = mResponseData.getFolders();
        for (Folders data : folderList) {
            for (Facets facet : data.getFacets()) {
                facet.setSelected(false);
            }
        }
        mViewPagerAdaptor.updateFilterStatus(folderList);
        updateFilterCount();
    }

    private void loadFilteredData() {
        if (mViewPager != null && mViewPager.getVisibility() == View.VISIBLE) {
            mViewPager.setVisibility(View.GONE);
        }
        loadMore(UrlBuilder.createUrl(false, null), true);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (mSearchItem != null) {
            mSearchView.onActionViewCollapsed();
        }
        if (!TextUtils.isEmpty(query)) {
            mSearchQuery = query;
            loadSerach(query);
        }
        return false;
    }

    private void loadSerach(String query) {
        if (mViewPager != null && mViewPager.getVisibility() == View.VISIBLE) {
            mViewPager.setVisibility(View.GONE);
        }
        loadMore(UrlBuilder.createSearchUrl(query), true);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
