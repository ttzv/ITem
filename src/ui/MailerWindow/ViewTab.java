package ui.MailerWindow;

import file.MailMsgParser;
import javafx.scene.control.Tab;
import javafx.scene.web.WebView;

import java.util.Objects;

public class ViewTab extends Tab {

    private MailMsgParser parser;
    private WebView webView;
    private String name;

    public ViewTab(String tabName, MailMsgParser parser){

        webView = new WebView();
        this.name = tabName;
        this.parser = parser;

        this.setText(name);
        this.setContent(webView);
        webView.getEngine().loadContent(String.valueOf(parser.getOutputString()));
    }

    public void reload(){
        webView.getEngine().reload();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewTab viewTab = (ViewTab) o;
        return Objects.equals(name, viewTab.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(webView, name, parser);
    }

    public MailMsgParser getParser() {
        return parser;
    }

    public String getName() {
        return name;
    }
}
