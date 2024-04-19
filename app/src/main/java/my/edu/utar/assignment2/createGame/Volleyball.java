package my.edu.utar.sportsbuddy;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class Volleyball extends AppCompatActivity {

    private WebView webView1;
    private WebView webView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volleyball);

        webView1 = findViewById(R.id.webView1);
        webView2 = findViewById(R.id.webView2);
//


        // Embed YouTube video URL
        String videoUrl1 = "https://www.youtube.com/embed/OWCkPbzq81g?si=llBsN9ABtFJpEMln";
        String videoUrl2 = "https://www.youtube.com/embed/h6d1vPmvI2o?si=tFuyjZHlsWRAO1gp";

        // HTML content to embed the video
        String htmlContent1 = "<html><body><iframe width=\"100%\" height=\"100%\" src=\"" + videoUrl1 + "\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe></body></html>";
        String htmlContent2 = "<html><body><iframe width=\"100%\" height=\"100%\" src=\"" + videoUrl2 + "\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe></body></html>";

        // Load the HTML content with the base URL
        webView1.loadDataWithBaseURL(null, htmlContent1, "text/html", "utf-8", null);
        webView2.loadDataWithBaseURL(null, htmlContent2, "text/html", "utf-8", null);

        // Enable JavaScript
        webView1.getSettings().setJavaScriptEnabled(true);
        webView2.getSettings().setJavaScriptEnabled(true);

        // Set WebChromeClient
        webView1.setWebChromeClient(new WebChromeClient());
        webView2.setWebChromeClient(new WebChromeClient());
    }
}
