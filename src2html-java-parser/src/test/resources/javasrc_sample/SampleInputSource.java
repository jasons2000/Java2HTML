package javasrc_sample;


import org.jsoup.Connection;
import org.jsoup.Jsoup;

/**
 * This is never actually ran - just used for testing
 */
public class SampleInputSource {

    // singe line comment test

    private String aString = "Random String";

    private String umlats = "áéú";
    private String escapes = "\u1000\u2000";


    SampleInputSource(){

    }

    public static void main(String[] args) {

        Connection connection = Jsoup.connect("http://a_url_blah_blah"); //this should be java doc linked

        /*
           comment test
         */
    }
}
