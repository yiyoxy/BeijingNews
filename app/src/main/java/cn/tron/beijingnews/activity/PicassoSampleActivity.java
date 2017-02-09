package cn.tron.beijingnews.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.tron.beijingnews.R;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PicassoSampleActivity extends AppCompatActivity {

    @BindView(R.id.iv_photo)
    PhotoView ivPhoto;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picasso_sample);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        url = getIntent().getStringExtra("url");

        final PhotoViewAttacher attacher = new PhotoViewAttacher(ivPhoto);

        // picasso:Implementation of ImageView for Android that supports zooming, by various touch gestures.
        Picasso.with(this)
                .load(url)
                .into(ivPhoto, new Callback() {
                    @Override
                    public void onSuccess() {
                        attacher.update();
                    }

                    @Override
                    public void onError() {

                    }
                });
    }
}
