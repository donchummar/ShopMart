package com.nod.shopmart.adaptor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nod.shopmart.EventListener;
import com.nod.shopmart.ProductListActivity;
import com.nod.shopmart.R;
import com.nod.shopmart.model.Facets;
import com.nod.shopmart.utils.UrlBuilder;
import com.nod.shopmart.widget.CheckableRelativeLayout;
import com.nod.shopmart.widget.InertCheckBox;

import java.util.ArrayList;

/**
 * Created by Don Chummar on 9/25/2015.
 */
public class FilterlistAdaptor extends RecyclerView.Adapter<FilterlistAdaptor.FViewHolder> {

    private final ArrayList<Facets> mProductList;
    private EventListener mCheckListener;

    public FilterlistAdaptor(ProductListActivity mContext, ArrayList<Facets> list) {
        mProductList = list;
        mCheckListener =  mContext;
    }


    @Override
    public FViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.filter_list_item, viewGroup, false);
        return new FViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FViewHolder pViewHolder, final int i) {
        final Facets facetdata = mProductList.get(i);
        pViewHolder.mProductName.setText(facetdata.getLabel() + " ("+facetdata.getCount() +")");
        pViewHolder.mCheckLyt.setChecked(facetdata.is_selected());
        pViewHolder.mCheckLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CheckableRelativeLayout) view).toggle();
                boolean isChecked = ((CheckableRelativeLayout) view).isChecked();
                mCheckListener.onCheckedListener(isChecked, (facetdata.getTag()));
                mProductList.get(i).setSelected(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }


    public static class FViewHolder extends RecyclerView.ViewHolder {
        private final TextView mProductName;
        private final CheckableRelativeLayout mCheckLyt;

        public FViewHolder(View itemView) {
            super(itemView);
            mCheckLyt = (CheckableRelativeLayout) itemView.findViewById(R.id.checkable_lyt);
            mProductName = (TextView) itemView.findViewById(R.id.product_name);
        }
    }
}
