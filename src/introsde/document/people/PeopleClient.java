package introsde.document.people;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.Service;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import introsde.document.ws.People;
import introsde.document.ws.Person;
@Stateless
@LocalBean
@Path("/peopleStorage")
public class PeopleClient {
	

	private URL url;
	private QName qname;
	private People people;
	String testo="";
	private int idCreated;
	private DocumentBuilderFactory domFactory;
	private DocumentBuilder builder;
	private Document doc;
	private SOAPConnectionFactory soapConnectionFactory;
	private SOAPConnection soapConnection;

	SOAPMessage soapMessage = null;
	SOAPBody soapBody = null;

	final String ENVELOPE_NAMESPACE = "http://schemas.xmlsoap.org/soap/envelope/";
	final String ENVELOPE_NAMESPACE_TAG = "ws";
	final String ENCODING_NAMESPACE = "http://www.w3.org/2001/12/soap-encoding";

	final String BODY_NAMESPACE = "http://ws.document.introsde/";
	final String BODY_NAMESPACE_TAG = "m";

	String mediaType = "text/xml";
	int id;
	Long first_personId;
	int last_personId;
	String measure_type;
	Long measureId;
	
	public PeopleClient(){}

	public PeopleClient(String endpointUrl) throws Exception {

		System.out.println("Starting People Service...");
		System.out.println("**STEP 1**");
		System.out.println("WSDL url " + endpointUrl + "\n[kill the process to exit]");

		// 1st argument service URI, refer to wsdl document above
		url = new URL(endpointUrl);

		// 2nd argument is service name, refer to wsdl document above
		qname = new QName("http://ws.document.introsde/", "PeopleService");

		Service service = Service.create(url, qname);

		

		// Create SOAP Connection
		soapConnectionFactory = SOAPConnectionFactory.newInstance();
		soapConnection = soapConnectionFactory.createConnection();
	}



	// ricevo dati da sopra per salvare utente se non esiste o per aggiornare se
	// esiste
	@GET

	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String receiveData(@DefaultValue("") @QueryParam("username") String username,
			@DefaultValue("") @QueryParam("password") String password, 
			@DefaultValue("") @QueryParam("saveperson") String saveperson) throws Exception {
		System.out.println("GET");
		String urlserver = "http://localhost:6903/ws/people";

		PeopleClient c = new PeopleClient(urlserver);
		
		if (saveperson.equals("yes")){
		
		if (username.equals("") || password.equals("")) {
			return "non ho trovato username e password";
		} else {
			
			for (int i = 0; i < c.request_1().size() && !(c.request_1().get(i).getUsername().equals(username)); i++) {
				
					id=c.request_1().get(i).getPersonId();
					testo= "aggiornato";
					//break;
					
					//return "aggiornato";
				
				System.out.println(testo+"e questo");
			} if (!testo.equals("aggiornato")){
				SOAPMessage soapResponse4 = c.soapConnection.call(c.request_4("sofia", "chimirri"), c.url);
				System.out.println("INBOUND MESSAGE\n");
				System.out.println(getSOAPMessageAsString(soapResponse4));
				/**/
				domFactory = DocumentBuilderFactory.newInstance();
				domFactory.setNamespaceAware(true);
				builder = domFactory.newDocumentBuilder();
				doc = builder.parse(new InputSource(new StringReader(getSOAPMessageAsString(soapResponse4))));
				Element rootElement = doc.getDocumentElement();

				String found = "";
					for (int i1 = 0; i1 < rootElement.getChildNodes().getLength(); i1++) {
					System.out.println("found: " + rootElement.getTextContent());
//					if (rootElement.getChildNodes().item(i1).getNodeName().equals("idPerson")) {
//						found = rootElement.getTextContent();
//					
//						
//					}
					return rootElement.getTextContent();
				}
					
			}else{
				SOAPMessage soapResponse3 = c.soapConnection
						.call(c.request_3(id, username, password), c.url);
				System.out.println("INBOUND MESSAGE\n");
				System.out.println(getSOAPMessageAsString(soapResponse3));
				System.out.println("id: "+id);
				return Integer.toString(id);
			}
		}}else{
			
			System.out.println("id della persona1: "+username);
			int i = 0;
			while( i < c.request_1().size() && !(c.request_1().get(i).getUsername().equals(username))) {
				
				id=c.request_1().get(i).getPersonId();
					//int id2= c.getPersonByUsername(username);
					
					i++;
				}
			System.out.println("id della persona: "+id+" e "+c.request_1().get(i).getUsername());
			return Integer.toString(id);
		}
		return "-1";
	}

	public List<Person> request_1() {
		System.out.println("REQUEST 1");
		templateRequest(1, "POST", mediaType);
		return people.readPersonList();
	}

	public void getGoalByPersonId(int idPerson) {
		System.out.println("getGoalByPersonId");
		templateRequest(2, "POST", mediaType);

		people.readPerson(idPerson);
	}

	public int getPersonByUsername(String username) {
		System.out.println("getGoalByPersonId");
		templateRequest(7, "POST", mediaType);
		//people.login(username);
		List<Person>p =people.readPersonList();
		System.out.println("dim: "+ p.size());
			return -1;
	}

	public SOAPMessage request_3(int id, String user, String pass) {
		String method_3 = "updatePerson";
		String arg0 = "person";
		String arg1 = "personId";
		String arg2 = "username";
		String arg3 = "password";
		// String arg4 = "birthdate";
		try {
			createSOAPRequest();
			SOAPElement updatePerson = soapBody.addChildElement(method_3, BODY_NAMESPACE_TAG);

			SOAPElement person = updatePerson.addChildElement(arg0);

			SOAPElement personId = person.addChildElement(arg1);
			personId.addTextNode(String.valueOf(id));

			SOAPElement username = person.addChildElement(arg2);
			username.addTextNode(user);

			SOAPElement password = person.addChildElement(arg3);
			password.addTextNode(pass);
			// SOAPElement birthdate = person.addChildElement(arg4);
			// birthdate.addTextNode("1985/03/05");

			person.addChildElement(personId);
			person.addChildElement(username);
			person.addChildElement(password);
			// person.addChildElement(birthdate);

			soapMessage.saveChanges();

			/* Print the request message */
			templateRequest(3, "PUT", mediaType);
			System.out.println("OUTBOUND MESSAGE\n");

			System.out.println(getSOAPMessageAsString(soapMessage));
			System.out.println();

			return soapMessage;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public SOAPMessage request_4(String user, String pass) {

		SOAPElement createPerson = null;
		String method_4 = "createPerson";
		String arg0 = "person";
		String arg1 = "username";
		String arg2 = "password";
		String arg3 = "birthdate";
		//
		// Person personToCreate = new Person();
		// personToCreate.setBirthdate("1978-04-23");
		// personToCreate.setPassword(pass);
		// personToCreate.setUsername(user);
		// int personToCreateId = people.createPerson(personToCreate);
		// System.out.println("Person id: " + personToCreateId);

		try {
			createSOAPRequest();
			createPerson = soapBody.addChildElement(method_4, BODY_NAMESPACE_TAG);

			SOAPElement person = createPerson.addChildElement(arg0);

			SOAPElement username = person.addChildElement(arg1);
			username.addTextNode(user);

			SOAPElement password = person.addChildElement(arg2);
			password.addTextNode(pass);

			SOAPElement birthdate = person.addChildElement(arg3);
			birthdate.addTextNode("1999-02-03");

			person.addChildElement(username);
			person.addChildElement(password);
			person.addChildElement(birthdate);

			soapMessage.saveChanges();

			/* Print the request message */
			templateRequest(4, "POST", mediaType);
			System.out.println("OUTBOUND MESSAGE\n");

			System.out.println(getSOAPMessageAsString(soapMessage));
			System.out.println();
			return soapMessage;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	public void request_5() {
		templateRequest(5, "DELETE", mediaType);
		int i = 0;
		while (people.readPerson(i) == null) {
			i = (int) (Math.random() * 100);
			// System.out.println(i);
		}

		people.deletePerson(i);
	}

	public void createSOAPRequest() throws Exception {
		// Create message
		MessageFactory messageFactory = MessageFactory.newInstance();
		soapMessage = messageFactory.createMessage();
		SOAPPart soapPart = soapMessage.getSOAPPart();

		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		// envelope.addNamespaceDeclaration(ENVELOPE_NAMESPACE_TAG,ENVELOPE_NAMESPACE);
		envelope.setEncodingStyle(ENCODING_NAMESPACE);

		// SOAP Body
		soapBody = envelope.getBody();
		soapBody.addNamespaceDeclaration(BODY_NAMESPACE_TAG, BODY_NAMESPACE);
	}

	public static String getSOAPMessageAsString(SOAPMessage soapMessage) {
		try {

			TransformerFactory tff = TransformerFactory.newInstance();
			Transformer tf = tff.newTransformer();

			// Set formatting

			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

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

	/**
	 * 
	 * @param numberRequest
	 * @param method
	 * @param mediaType
	 */
	public static void templateRequest(int numberRequest, String method, String mediaType) {
		mediaType = mediaType.toUpperCase();
		System.out.println(
				"======================================================================================================");
		System.out.println("Method #" + numberRequest + ": " + method + " " + "Accept: " + mediaType + " "
				+ "Content-Type: " + mediaType);
		System.out.println();
	}

}
