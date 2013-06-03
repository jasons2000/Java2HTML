package com.java2html;


import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;


public class WebServer {

    private Server server;

    public static void main(String[] args) throws Exception {

        // simple test
        new WebServer("./java2html-core/src/test/resources/javadoc-sample/jsoup-1.7.2-javadoc", "index.html");
    }

    public WebServer(String base, String... welcome) throws Exception {

        server = new Server(0);

        ResourceHandler handler = new ResourceHandler();
        handler.setDirectoriesListed(true);
        handler.setWelcomeFiles(welcome);

        handler.setResourceBase(base);
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{handler, new DefaultHandler()});
        server.setHandler(handlers);

        server.start();
    }

    public int getPort() {
        return server.getConnectors()[0].getLocalPort();
    }

    public void close() {
        try {
            server.stop();
        }
        catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


}
