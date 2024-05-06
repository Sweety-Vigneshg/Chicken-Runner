package vigneshgbe.chickenrun;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import javax.security.auth.callback.Callback;


public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private WebSettings webSettings;
    private ProgressBar progressBar;
    private String baseurl;
    private ImageView imageView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView=findViewById(R.id.webView);
        progressBar=findViewById(R.id.progressBarH);
        imageView=findViewById(R.id.imageView);
        webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("utf-8");

        //baseurl=getResources().getString(R.string.main_url);
        baseurl="file:///android_asset/index.html";
        //baseurl="https://qu1zkplmi8ann493domkog.on.drv.tw/Chicken-Runner/";

        webView.loadUrl(baseurl);
        //webView.setVerticalScrollBarEnabled(true);

        //changing size to fit
        //webSettings.setLoadWithOverviewMode(true);
        //webSettings.setUseWideViewPort(true);

        //webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);


        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url1)
            {
                if(url1.startsWith(baseurl))
                {
                    progressBar.setVisibility(View.GONE);
                    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(url1));
                    startActivity(intent);
                    return true;
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(url1));
                    startActivity(intent);
                    return false;
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);

            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                progressBar.setVisibility(View.GONE);
                if(webView.getVisibility()==View.GONE)
                {
                    webView.setVisibility(View.VISIBLE);
                }
                if(imageView.getVisibility()==View.VISIBLE)
                {
                    imageView.setVisibility(View.GONE);
                }
            }



            @SuppressLint("NewApi")
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Toast.makeText(MainActivity.this,error.getDescription(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Toast.makeText(MainActivity.this,errorResponse.getReasonPhrase(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        });

        // Initialize the Mobile Ads SDK
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Toast.makeText(MainActivity.this, " initializing.. ", Toast.LENGTH_SHORT).show();
            }
        });

        AdView mAdView;
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        webView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int newProgress)
            {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
            }
        });

    }
    @Override
    protected void onRestart() {
        this.recreate();  // refresh activity
        super.onRestart();
    }

    @Override
    public void onBackPressed()
    {
        if(webView!=null && webView.canGoBack())
        {
            webView.goBack();
        }
        else
        {
            super.onBackPressed();
        }

    }
}