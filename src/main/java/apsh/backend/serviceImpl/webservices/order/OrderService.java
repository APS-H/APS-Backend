
package apsh.backend.serviceimpl.webservices.order;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "OrderService", targetNamespace = "http://service.legacy.apsh.com/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface OrderService {


    /**
     * 
     * @param arg0
     * @return
     *     returns apsh.backend.serviceimpl.webservices.order.Order
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getOrderById", targetNamespace = "http://service.legacy.apsh.com/", className = "apsh.backend.serviceimpl.webservices.order.GetOrderById")
    @ResponseWrapper(localName = "getOrderByIdResponse", targetNamespace = "http://service.legacy.apsh.com/", className = "apsh.backend.serviceimpl.webservices.order.GetOrderByIdResponse")
    public Order getOrderById(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

    /**
     * 
     * @return
     *     returns java.util.List<apsh.backend.serviceimpl.webservices.order.Order>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getOrderAll", targetNamespace = "http://service.legacy.apsh.com/", className = "apsh.backend.serviceimpl.webservices.order.GetOrderAll")
    @ResponseWrapper(localName = "getOrderAllResponse", targetNamespace = "http://service.legacy.apsh.com/", className = "apsh.backend.serviceimpl.webservices.order.GetOrderAllResponse")
    public List<Order> getOrderAll();

}
