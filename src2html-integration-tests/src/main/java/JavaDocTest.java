import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class JavaDocTest {

    private static final Map<String, String> classList = new Hashtable<String, String>();
    private static final Map<String, String> packageList = new HashMap<String, String>(); //use JavaSource

    public static void main(String[] args) throws IOException {
        URL urlClasses = new URL("xxx");
        Connection con = Jsoup.connect("THIS IS NOT REAL CODE");
        Document doc = con.get();

         Elements links = doc.select("a[href]");
         for (Element link : links) {
             String text = link.text();
              if (text.equals("All Classes")) continue;
             packageList.put(link.text(), new URL(urlClasses, link.attr("href")).toString());
         }
    }

}
