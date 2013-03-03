package com.haloword.startup;

import com.haloword.Configuration;
import com.haloword.core.SimpleContext;
import com.haloword.core.SimpleContextLifecycleListener;
import com.haloword.core.SimpleContextMapper;
import com.haloword.core.SimpleLoader;
import com.haloword.core.SimpleWrapper;
import org.apache.catalina.Connector;
import org.apache.catalina.Context;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.Loader;
import org.apache.catalina.Mapper;
import org.apache.catalina.Valve;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.http.HttpConnector;
import org.apache.catalina.Pipeline;
import org.apache.catalina.logger.FileLogger;
import org.w3c.dom.DOMException;
import org.w3c.dom.NodeList;

public final class Bootstrap {
  public static void main(String[] args) throws DOMException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    Connector connector = new HttpConnector();
    NodeList wrappers = Configuration.doc.getElementsByTagName("Wrapper");
    Context context = new SimpleContext();
    for(int i=0; i<wrappers.getLength(); i++) {
    	Wrapper wrapper = new SimpleWrapper();
    	wrapper.setName(wrappers.item(i).getAttributes().getNamedItem("name").getNodeValue());
    	wrapper.setServletClass(wrappers.item(i).getAttributes().getNamedItem("servletClass").getNodeValue());
    	context.addChild(wrapper);
    }
//    Wrapper wrapper1 = new SimpleWrapper();
//    wrapper1.setName("Primitive");
//    wrapper1.setServletClass("PrimitiveServlet");
//    Wrapper wrapper2 = new SimpleWrapper();
//    wrapper2.setName("Modern");
//    wrapper2.setServletClass("ModernServlet");

//    context.addChild(wrapper1);
//    context.addChild(wrapper2);

    NodeList valves = Configuration.doc.getElementsByTagName("Valve");
    for(int i=0; i<valves.getLength(); i++) {
    	Valve valve = (Valve)Class.forName(valves.item(i).getAttributes().getNamedItem("type").getNodeValue()).newInstance();
    	((Pipeline) context).addValve(valve);
    }
    Mapper mapper = new SimpleContextMapper();
    mapper.setProtocol("http");
    LifecycleListener listener = new SimpleContextLifecycleListener();
    ((Lifecycle) context).addLifecycleListener(listener);
    context.addMapper(mapper);
    Loader loader = new SimpleLoader();
    context.setLoader(loader);
    // context.addServletMapping(pattern, name);
    context.addServletMapping("/Primitive", "Primitive");
    context.addServletMapping("/Modern", "Modern");
    context.addWelcomeFile("index.html");
    
    // ------ add logger --------
    System.setProperty("catalina.base", System.getProperty("user.dir"));
    FileLogger logger = new FileLogger();
    logger.setPrefix("FileLog_");
    logger.setSuffix(".txt");
    logger.setTimestamp(true);
    logger.setDirectory("webroot");
    context.setLogger(logger);
 
    connector.setContainer(context);
    try {
      connector.initialize();
      ((Lifecycle) connector).start();
      ((Lifecycle) context).start();

      // make the application wait until we press a key.
      System.in.read();
      ((Lifecycle) context).stop();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}