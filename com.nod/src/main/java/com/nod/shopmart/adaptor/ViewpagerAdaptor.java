package com.nod.shopmart.adaptor;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nod.shopmart.ProductListActivity;
import com.nod.shopmart.R;
import com.nod.shopmart.model.Facets;
import com.nod.shopmart.model.Folders;
import com.nod.shopmart.utils.AppUtils;

import java.util.ArrayList;

/**
 * Created by Don Chummar on 9/25/2015.
 */
public class ViewpagerAdaptor extends PagerAdapter {

    private final ProductListActivity mContext;
    private  ArrayList<String> mFolderTitle;
    private  ArrayList<Folders> mFolderList;

    public ViewpagerAdaptor(ProductListActivity productListActivity, ArrayList<Folders> folderData, ArrayList<String> filterTitle) {
        mContext = productListActivity;
        mFolderList = folderData;
        mFolderTitle = filterTitle;
        removeCatgory();
    }

    private void removeCatgory() {
        if (mFolderList != null && mFolderList.size() > 0 && mFolderList.get(0).getName().equalsIgnoreCase("Categories")){
            mFolderList.remove(0);
        }
        if (mFolderTitle != null && mFolderTitle.size() > 0 && mFolderTitle.get(0).equalsIgnoreCase("Categories")){
            mFolderTitle.remove(0);
        }
    }

    @Override
    public int getCount() {
        return mFolderList ==null || mFolderList.size() > 0 ? mFolderList.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFolderTitle.size() > 0 ? mFolderTitle.get(position) : super.getPageTitle(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.view_pager_item,container,false);
        view.setTag(position);
        RecyclerView filterList = (RecyclerView) view.findViewById(R.id.filer_list);
        int height = AppUtils.getScreenHeight((Activity) container.getContext());
        filterList.getLayoutParams().height = (int) (height * 0.5);
        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(container.getContext(), LinearLayoutManager.VERTICAL, false);
        filterList.setLayoutManager(linearlayoutManager);
        filterList.setAdapter(new FilterlistAdaptor(mContext, (ArrayList<Facets>) mFolderList.get(position).getFacets()));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public int getItemPosition(Object object){
        return POSITION_NONE;
    }


    public void updateFilterStatus(ArrayList<Folders> folderData){
        mFolderList = folderData;
        notifyDataSetChanged();
   }
}
