
package gov.va.os.reference.framework.ws.client.remote.test.mocks;

import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter1
    extends XmlAdapter<String, Date>
{


    public Date unmarshal(String value) {
        return (gov.va.os.reference.framework.transfer.jaxb.adapters.DateAdapter.parseDateTime(value));
    }

    public String marshal(Date value) {
        return (gov.va.os.reference.framework.transfer.jaxb.adapters.DateAdapter.printDateTime(value));
    }

}