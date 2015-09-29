package com.nod.shopmart.net;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.nod.shopmart.model.IDataModel;
import com.nod.shopmart.model.ShopData;

import java.io.UnsupportedEncodingException;
import java.util.Map;


/**
 * Created by Don Chummar on 8/13/2015.
 */
public class GsonRequest<T> extends Request<IDataModel> {
    private final Gson gson = new Gson();
    private IDataModel dataModel;
    private Map<String, String> headers;
    private final Response.Listener<IDataModel> listener;
    public static final int MY_SOCKET_TIMEOUT_MS = 30000;

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url       URL of the request to make
     * @param dataModel Relevant class object, for Gson's reflection
     * @param headers   Map of request headers
     */
    public GsonRequest(String url, IDataModel dataModel, Map<String, String> headers,
                       Response.Listener<IDataModel> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.dataModel = dataModel;
        this.headers = headers;
        this.listener = listener;
    }

    public GsonRequest(String url, IDataModel dataModel, Response.Listener<IDataModel> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = listener;
        this.dataModel = dataModel;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected Response<IDataModel> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            IDataModel productData =  gson.fromJson(jsonString, dataModel.getClass());
            if (productData instanceof ShopData){
                ShopData shopData = (ShopData) productData;
                shopData.createImagemap();
            }
            return Response.success(productData, getCacheEntry());
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }


    @Override
    public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
        retryPolicy = new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        return super.setRetryPolicy(retryPolicy);
    }

    @Override
    protected void deliverResponse(IDataModel response) {
        listener.onResponse(response);
    }

}
