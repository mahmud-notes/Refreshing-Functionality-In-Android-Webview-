package com.dogspull.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import java.util.Stack;

public class NewWebViewActivity extends AppCompatActivity {
    private WebView webView ;
    private SwipeRefreshLayout swipe ;
    private String mainUrl  = "https://dogspull.com/";
    private Stack<String> urlStack ;
    private Boolean loadStatus = true;
    private RelativeLayout loadingLayout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hiding the action bar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_webview);

        //Stacking Url for reloading functionality
        urlStack = new Stack<>();
        urlStack.push(mainUrl);

        //finding elements
        loadingLayout = findViewById(R.id.loading_layout_id);
        swipe = findViewById(R.id.web_swipe_layout_id);
        webView=findViewById(R.id.main_webview_id);

        //on swipe activity
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    LoadWeb();
                }catch (Exception e){
                    Toast.makeText(NewWebViewActivity.this, "Something went wrong! Restart App", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //initial webview load with top url from the stack
        LoadWeb();
    }

    public void LoadWeb(){
        loadStatus = true ;
        webView.clearCache(true);
        webSettings();

        //load url to webviwe with top url from the urlStack
        try{
            webView.loadUrl(urlStack.peek());
        }catch (Exception e){}

        //setting swipe re as progress
        swipe.setRefreshing(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                //update the url stack with the latest visited url
                urlStack.push(url);
                super.doUpdateVisitedHistory(view, url, isReload);
            }
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //set visibility of webview when page started
                webView.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                loadStatus = false ;
                new AlertDialog.Builder(NewWebViewActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("No Internet Connection!")
                        .setMessage("Please check your connection and try again. ")
                        .setPositiveButton("RETRY", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try{
                                    LoadWeb();
                                }catch (Exception e){

                                }
                            }

                        })
                        .setNegativeButton("CANCEL", null)
                        .show();
                        webView.setVisibility(View.GONE);
            }
            public  void  onPageFinished(WebView view, String url){
                if (loadStatus){
                    try{
                        swipe.setRefreshing(false);
                        webView.setVisibility(View.VISIBLE);
                        loadingLayout.setVisibility(View.GONE);
                    }catch (Exception e){

                    }
                }
                swipe.setRefreshing(false);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        try{
                            urlStack.pop();
                        }catch(Exception e) {

                        }
                        webView.goBack();
                    } else {
                        new AlertDialog.Builder(this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Closing app")
                                .setMessage("Are you sure you want to close this app?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }

                                })
                                .setNegativeButton("No", null)
                                .show();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
    private void webSettings(){
        //setting sevelral webview settings
        webView.clearHistory();
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        WebSettings webSettings = webView.getSettings();
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setJavaScriptEnabled(WebView_Config.JavaScriptEnable());
        webSettings.setDomStorageEnabled(WebView_Config.DomStorageEnabled());
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webSettings.setSavePassword(WebView_Config.SavePassword());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(webView,true);
        }
        webView.getSettings().setSupportMultipleWindows(true);
        webSettings.setSaveFormData(WebView_Config.SaveFormData());
        webView.getSettings().setJavaScriptEnabled(WebView_Config.JavaScriptEnable());
        webView.getSettings().setAppCacheEnabled(WebView_Config.AppCache());
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setSupportZoom(WebView_Config.SupportZoom());
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
    }

}