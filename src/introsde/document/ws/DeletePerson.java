
package introsde.document.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per deletePerson complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="deletePerson">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="personId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deletePerson", propOrder = {
    "personId"
})
public class DeletePerson {

    protected int personId;

    /**
     * Recupera il valore della proprietà personId.
     * 
     */
    public int getPersonId() {
        return personId;
    }

    /**
     * Imposta il valore della proprietà personId.
     * 
     */
    public void setPersonId(int value) {
        this.personId = value;
    }

}
