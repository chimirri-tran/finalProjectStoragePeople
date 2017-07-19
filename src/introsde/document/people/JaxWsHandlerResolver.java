package introsde.document.people;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

public class JaxWsHandlerResolver implements HandlerResolver{

	private FileOutputStream fos;
	
	public JaxWsHandlerResolver(FileOutputStream fos) {
		// TODO Auto-generated constructor stub
		this.fos = fos;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<Handler> getHandlerChain(PortInfo portInfo) {
		List<Handler> hchain = new ArrayList<Handler>();
        hchain.add(new JaxWsLoggingHandler(fos));
        return hchain;
	}

}