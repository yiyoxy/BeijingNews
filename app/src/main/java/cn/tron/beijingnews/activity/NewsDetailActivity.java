package cn.tron.beijingnews.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.tron.beijingnews.R;

public class NewsDetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ib_menu)
    ImageButton ibMenu;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.ib_textsize)
    ImageButton ibTextsize;
    @BindView(R.id.ib_share)
    ImageButton ibShare;
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;

    private String url;
    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);

        url = getIntent().getStringExtra("url");

        tvTitle.setVisibility(View.GONE);
        ibBack.setVisibility(View.VISIBLE);
        ibTextsize.setVisibility(View.VISIBLE);
        ibShare.setVisibility(View.VISIBLE);

        // webview的使用
        webview.loadUrl(url);
        // webview.loadUrl("http://android.atguigu.com");

        webSettings = webview.getSettings();

        // 支持javascript脚本语言
        webSettings.setJavaScriptEnabled(true);

        // 添加缩放按钮: 需要页面支持
        webSettings.setBuiltInZoomControls(true);

        // 支持双击页面变大变小: 需要页面支持
        webSettings.setUseWideViewPort(true);

        // 默认设置正常字体
        // webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        webSettings.setTextZoom(100);

        // 设置监听
        webview.setWebViewClient(new WebViewClient() {

            // 当网页加载完成的时候回调
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                progressbar.setVisibility(View.GONE);
            }
        });
    }

    @OnClick({R.id.ib_back, R.id.ib_textsize, R.id.ib_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_textsize:
                //Toast.makeText(NewsDetailActivity.this, "设置文字大小", Toast.LENGTH_SHORT).show();
                // 弹出设置对话框
                showChangeTextSizeDialog();
                break;
            case R.id.ib_share:
                Toast.makeText(NewsDetailActivity.this, "分享", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private int tempSize = 2; // 默认的字体为正常字体
    private int realSize = tempSize;

    // 改变文字大小
    private void showChangeTextSizeDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String[] items = {"超大号字体", "大号字体", "正常字体", "小号字体", "超小号字体"};

        builder.setTitle("设置字体大小");
        builder.setSingleChoiceItems(items, realSize, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 选中的字体大小(0~4)
                tempSize = which;
            }
        });
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 点击确定执行这里
                realSize = tempSize;
                changeText(realSize);
            }
        });

        // false: 按对话框以外的地方不起作用,按返回键也不起作用; true: 默认, 都起作用
        // builder.setCancelable(false);

        // builder.show() 返回一个dialog
        AlertDialog dialog = builder.show();

        // false: 按对话框以外的地方不起作用,按返回键起作用
        dialog.setCanceledOnTouchOutside(false);

        // false: 按对话框以外的地方不起作用,按返回键也不起作用
        // dialog.setCancelable(false);
    }

    private void changeText(int realSize) {
        switch (realSize) {
            case 0:
                // webSettings.setTextSize(WebSettings.TextSize.LARGEST); // 200
                webSettings.setTextZoom(200);
                break;
            case 1:
                // webSettings.setTextSize(WebSettings.TextSize.LARGER); // 150
                webSettings.setTextZoom(150);
                break;
            case 2:
                // webSettings.setTextSize(WebSettings.TextSize.NORMAL); // 100
                webSettings.setTextZoom(100);
                break;
            case 3:
                // webSettings.setTextSize(WebSettings.TextSize.SMALLER); // 75
                webSettings.setTextZoom(75);
                break;
            case 4:
                // webSettings.setTextSize(WebSettings.TextSize.SMALLEST); // 50
                webSettings.setTextZoom(50);
                break;
        }
    }
}
