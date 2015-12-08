package zerodegrees.prj_04ver20;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AuthActivity extends Activity {
    public final static int ACCESS_TOKEN_TAKEN = 1;
    WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth);

        webView = (WebView) findViewById(R.id.webView);

        webView.loadUrl(VK.getAuthUrl("5141811", "2097150"));

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String token = VK.findAccessTokenInURL(url);
                if (token != null) {
                    Intent intent = new Intent();
                    intent.putExtra("access_token", token);
                    AuthActivity.this.setResult(ACCESS_TOKEN_TAKEN, intent);
                    AuthActivity.this.finish();
                }
            }
        });

    }
}