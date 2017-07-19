package introsde.document.people;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class JaxWsLoggingHandler implements SOAPHandler<SOAPMessageContext> {

	//private String filename;
	private int requestNum = 0;
	private FileOutputStream fos;
	
	public JaxWsLoggingHandler(FileOutputStream fos) {
		this.fos = fos;
	}
	
	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		
		SOAPMessage message = context.getMessage();
		//message.
        boolean isOutboundMessage = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (isOutboundMessage) {
            System.out.println("OUTBOUND MESSAGE\n");
            try {
				fos.write("\nXML\n".getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 
        } else {
            System.out.println("INBOUND MESSAGE\n");
            try {
				fos.write("\nXML\n".getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        try {
            message.writeTo(fos);
            String result = prettySOAPMessageAsString(message);
            System.out.println(result);
        } catch (SOAPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		SOAPMessage message = context.getMessage();
		try {
            message.writeTo(System.out);
            //String result = prettySOAPMessageAsString(message);
            //System.out.println(result);
        } catch (SOAPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
	}

	@Override
	public void close(MessageContext context) {
	}

	@Override
	public Set<QName> getHeaders() {
		return null;
	}
	
	public void setRequestNum(int requestNum) {
		this.requestNum = requestNum;
	}

	public String prettySOAPMessageAsString(SOAPMessage soapMessage) {
		try {

			TransformerFactory tff = TransformerFactory.newInstance();
			Transformer tf = tff.newTransformer();

			// Set formatting
			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","2");

			Source sc = soapMessage.getSOAPPart().getContent();

			ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(streamOut);
			tf.transform(sc, result);

			String strMessage = streamOut.toString();
			return strMessage;
			
		} catch (Exception e) {
			System.out.println("Exception in getSOAPMessageAsString " + e.getMessage());
			return null;
		}

	}
}