package ui.mainAppWindow;

import javafx.scene.control.Tab;
import javafx.scene.web.WebView;

public class ViewTab<T> extends Tab {

    private WebView webView;

    public ViewTab(String tabName, T content){
        this.setText(tabName);
        webView = new WebView();
        this.setContent(webView);
        webView.getEngine().loadContent(String.valueOf(content));
    }

    public void reload(){
        webView.getEngine().reload();
    }
}
