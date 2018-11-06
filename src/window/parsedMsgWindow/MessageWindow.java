package window.parsedMsgWindow;


import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

import java.io.IOException;

public class MessageWindow extends VBox {

    private WebView webView;
    private String content;


    public MessageWindow(String content) throws IOException {
        this.content = content;
        this.setPadding(new Insets(15, 15, 15, 15));
        setManaged(true);

        webView = new WebView();
        webView.setManaged(true);
        System.out.println(webView.isResizable());
        this.getChildren().add(webView);
        webView.getEngine().loadContent(content);
    }

    public void updateWindowContent(String content){
        webView.getEngine().loadContent(content);
    }

}
