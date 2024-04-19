package my.edu.utar.sportsbuddy;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class Badminton extends AppCompatActivity {

    private WebView webView1;
    private WebView webView2;
    private WebView webView3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badminton);

        webView1 = findViewById(R.id.webView1);
        webView2 = findViewById(R.id.webView2);
        webView3 = findViewById(R.id.webView3);


        // Embed YouTube video URL
        String videoUrl1 = "https://www.youtube.com/embed/tAS7rOKtpgQ?si=z8_WrhBPi5p7ebow";
        String videoUrl2 = "https://www.youtube.com/embed/DnUwBz2BVYM?si=Dm27KFriRomKZlGI";
        String videoUrl3 = "https://www.youtube.com/embed/WGChK364ybM?si=bR9B28IPZ8f0xevR";

        // HTML content to embed the video
        String htmlContent1 = "<html><body><iframe width=\"100%\" height=\"100%\" src=\"" + videoUrl1 + "\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe></body></html>";
        String htmlContent2 = "<html><body><iframe width=\"100%\" height=\"100%\" src=\"" + videoUrl2 + "\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe></body></html>";
        String htmlContent3 = "<html><body><iframe width=\"100%\" height=\"100%\" src=\"" + videoUrl3 + "\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe></body></html>";

        // Load the HTML content with the base URL
        webView1.loadDataWithBaseURL(null, htmlContent1, "text/html", "utf-8", null);
        webView2.loadDataWithBaseURL(null, htmlContent2, "text/html", "utf-8", null);
        webView3.loadDataWithBaseURL(null, htmlContent3, "text/html", "utf-8", null);

        // Enable JavaScript
        webView1.getSettings().setJavaScriptEnabled(true);
        webView2.getSettings().setJavaScriptEnabled(true);
        webView3.getSettings().setJavaScriptEnabled(true);

        // Set WebChromeClient
        webView1.setWebChromeClient(new WebChromeClient());
        webView2.setWebChromeClient(new WebChromeClient());
        webView3.getSettings().setJavaScriptEnabled(true);
    }
}
