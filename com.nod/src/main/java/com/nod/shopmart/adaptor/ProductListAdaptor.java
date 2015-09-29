package com.nod.shopmart.adaptor;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.nod.shopmart.R;
import com.nod.shopmart.model.Product;
import com.nod.shopmart.utils.AppUtils;
import com.nod.shopmart.utils.Constant;
import com.nod.shopmart.utils.ImageCacheManager;

import java.util.ArrayList;

/**
 * Created by Don Chummar on 9/25/2015.
 */
public class ProductListAdaptor extends RecyclerView.Adapter<ProductListAdaptor.PViewHolder> {

    private final Activity mContext;
    private final ImageLoader mImageLoader;
    private ArrayList<Product> mProductList = new ArrayList<>();

    public ProductListAdaptor(Activity context) {
        mContext = context;
        mImageLoader = ImageCacheManager.INSTANCE.getImageLoader();
    }


    @Override
    public PViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list_item, viewGroup, false);
        return new PViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PViewHolder pViewHolder, int i) {
        Product productData = mProductList.get(i);
        if (productData != null) {
            if (productData.getMin_price_str() != null)
                pViewHolder.productPrice.setText("Rs. " + productData.getMin_price_str());
            if (productData.getName() != null)
                pViewHolder.productName.setText(productData.getName());
            Double ratio = Double.valueOf(AppUtils.getDeviceDensityRatio(mContext));
            Integer result = Constant.imageResoList.get(ratio);
            int imageSize = (int) (AppUtils.getScreenWidth(mContext) * 0.3);
            pViewHolder.productImage.getLayoutParams().height = imageSize;
            pViewHolder.productImage.getLayoutParams().width = imageSize;
            String url = productData.gethighestResolution();
            pViewHolder.storeCount.setText(productData.getStore_count() + (Integer.parseInt(productData.getStore_count()) > 1 ? " Stores" : " Store"));
            //pViewHolder.productImage.setImageUrl(url, ImageCacheManager.INSTANCE.getImageLoader());
            pViewHolder.ratingValue.setText(productData.getAvg_rating());
            Double colorValue = Double.valueOf(productData.getAvg_rating());
            pViewHolder.ratingValue.setBackgroundColor(Color.rgb(0, (int) (255 - (colorValue * 20)), 0));
            if (productData.getRating_count() != 0)
                pViewHolder.ratingCount.setText(productData.getRating_count() + (productData.getRating_count() > 1 ? " Votes" : " Vote"));
            pViewHolder.productImage.setVisibility(View.INVISIBLE);
            mImageLoader.get(url, new ImageLoader.ImageListener() {

                @Override
                public void onErrorResponse(VolleyError arg0) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                    if (response != null) {
                        Bitmap bitmap = response.getBitmap();
                        if (bitmap != null) {
                            pViewHolder.productImage.setVisibility(View.VISIBLE);
                            pViewHolder.productImage.setImageBitmap(bitmap);
                            AppUtils.zoomInImage(pViewHolder.productImage);
                        }
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mProductList.size() > 0 ? mProductList.size() : 0;
    }

    public void updateList(ArrayList<Product> newList) {
        mProductList = newList;
        notifyDataSetChanged();
    }

    public static class PViewHolder extends RecyclerView.ViewHolder {
        private final TextView productName;
        private final ImageView productImage;
        private final TextView productPrice;
        private final TextView storeCount;
        private final TextView ratingValue;
        private final TextView ratingCount;

        public PViewHolder(View itemView) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.product_name);
            ratingValue = (TextView) itemView.findViewById(R.id.rating_bar);
            ratingCount = (TextView) itemView.findViewById(R.id.rating_count);
            storeCount = (TextView) itemView.findViewById(R.id.store_count);
            productPrice = (TextView) itemView.findViewById(R.id.product_price);
            productImage = (ImageView) itemView.findViewById(R.id.productImage);
        }
    }
}
