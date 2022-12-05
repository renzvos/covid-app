package com.instantapp.corona;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

public class source extends AppCompatActivity {

    TextView title;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source);
        title = findViewById(R.id.stype);

        Bundle extras = getIntent().getExtras();
        String info  = extras.getString("info");

        if (info.contains("route"))
        {
            title.setText("RouteMap");

            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
            mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
                private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
                public void putBitmap(String url, Bitmap bitmap) {
                    mCache.put(url, bitmap);
                }
                public Bitmap getBitmap(String url) {
                    return mCache.get(url);
                }

            });

            NetworkImageView avatar = (NetworkImageView)findViewById(R.id.source);
            avatar.setImageUrl(info,mImageLoader);





        }


    }
}
