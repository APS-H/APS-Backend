
package apsh.backend.serviceimpl.webservices;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the apsh.backend.serviceimpl.LegacySystemWebService package.
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetItemByCode_QNAME = new QName("http://service.legacy.apsh.com/", "getItemByCode");
    private final static QName _GetProductAllResponse_QNAME = new QName("http://service.legacy.apsh.com/", "getProductAllResponse");
    private final static QName _GetProductAll_QNAME = new QName("http://service.legacy.apsh.com/", "getProductAll");
    private final static QName _GetItemByCodeResponse_QNAME = new QName("http://service.legacy.apsh.com/", "getItemByCodeResponse");
    private final static QName _GetResourceAll_QNAME = new QName("http://service.legacy.apsh.com/", "getResourceAll");
    private final static QName _GetItemAll_QNAME = new QName("http://service.legacy.apsh.com/", "getItemAll");
    private final static QName _GetResourceByCodeResponse_QNAME = new QName("http://service.legacy.apsh.com/", "getResourceByCodeResponse");
    private final static QName _GetProductByCode_QNAME = new QName("http://service.legacy.apsh.com/", "getProductByCode");
    private final static QName _GetProductByCodeResponse_QNAME = new QName("http://service.legacy.apsh.com/", "getProductByCodeResponse");
    private final static QName _GetResourceAllResponse_QNAME = new QName("http://service.legacy.apsh.com/", "getResourceAllResponse");
    private final static QName _GetResourceByCode_QNAME = new QName("http://service.legacy.apsh.com/", "getResourceByCode");
    private final static QName _GetItemAllResponse_QNAME = new QName("http://service.legacy.apsh.com/", "getItemAllResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: apsh.backend.serviceimpl.LegacySystemWebService
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ProductTech }
     * 
     */
    public ProductTech createProductTech() {
        return new ProductTech();
    }

    /**
     * Create an instance of {@link Product }
     * 
     */
    public Product createProduct() {
        return new Product();
    }

    /**
     * Create an instance of {@link GetResourceByCodeResponse }
     * 
     */
    public GetResourceByCodeResponse createGetResourceByCodeResponse() {
        return new GetResourceByCodeResponse();
    }

    /**
     * Create an instance of {@link GetProductByCode }
     * 
     */
    public GetProductByCode createGetProductByCode() {
        return new GetProductByCode();
    }

    /**
     * Create an instance of {@link GetItemByCode }
     * 
     */
    public GetItemByCode createGetItemByCode() {
        return new GetItemByCode();
    }

    /**
     * Create an instance of {@link GetProductAllResponse }
     * 
     */
    public GetProductAllResponse createGetProductAllResponse() {
        return new GetProductAllResponse();
    }

    /**
     * Create an instance of {@link GetProductAll }
     * 
     */
    public GetProductAll createGetProductAll() {
        return new GetProductAll();
    }

    /**
     * Create an instance of {@link GetItemByCodeResponse }
     * 
     */
    public GetItemByCodeResponse createGetItemByCodeResponse() {
        return new GetItemByCodeResponse();
    }

    /**
     * Create an instance of {@link GetResourceAll }
     * 
     */
    public GetResourceAll createGetResourceAll() {
        return new GetResourceAll();
    }

    /**
     * Create an instance of {@link GetItemAll }
     * 
     */
    public GetItemAll createGetItemAll() {
        return new GetItemAll();
    }

    /**
     * Create an instance of {@link GetResourceAllResponse }
     * 
     */
    public GetResourceAllResponse createGetResourceAllResponse() {
        return new GetResourceAllResponse();
    }

    /**
     * Create an instance of {@link GetResourceByCode }
     * 
     */
    public GetResourceByCode createGetResourceByCode() {
        return new GetResourceByCode();
    }

    /**
     * Create an instance of {@link GetItemAllResponse }
     * 
     */
    public GetItemAllResponse createGetItemAllResponse() {
        return new GetItemAllResponse();
    }

    /**
     * Create an instance of {@link GetProductByCodeResponse }
     * 
     */
    public GetProductByCodeResponse createGetProductByCodeResponse() {
        return new GetProductByCodeResponse();
    }

    /**
     * Create an instance of {@link Item }
     * 
     */
    public Item createItem() {
        return new Item();
    }

    /**
     * Create an instance of {@link Resource }
     * 
     */
    public Resource createResource() {
        return new Resource();
    }

    /**
     * Create an instance of {@link ProductItem }
     * 
     */
    public ProductItem createProductItem() {
        return new ProductItem();
    }

    /**
     * Create an instance of {@link ProductResource }
     * 
     */
    public ProductResource createProductResource() {
        return new ProductResource();
    }

    /**
     * Create an instance of {@link ProductTech.MaterialList }
     * 
     */
    public ProductTech.MaterialList createProductTechMaterialList() {
        return new ProductTech.MaterialList();
    }

    /**
     * Create an instance of {@link ProductTech.ResourceList }
     * 
     */
    public ProductTech.ResourceList createProductTechResourceList() {
        return new ProductTech.ResourceList();
    }

    /**
     * Create an instance of {@link Product.ProductTechList }
     * 
     */
    public Product.ProductTechList createProductProductTechList() {
        return new Product.ProductTechList();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetItemByCode }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.legacy.apsh.com/", name = "getItemByCode")
    public JAXBElement<GetItemByCode> createGetItemByCode(GetItemByCode value) {
        return new JAXBElement<GetItemByCode>(_GetItemByCode_QNAME, GetItemByCode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetProductAllResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.legacy.apsh.com/", name = "getProductAllResponse")
    public JAXBElement<GetProductAllResponse> createGetProductAllResponse(GetProductAllResponse value) {
        return new JAXBElement<GetProductAllResponse>(_GetProductAllResponse_QNAME, GetProductAllResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetProductAll }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.legacy.apsh.com/", name = "getProductAll")
    public JAXBElement<GetProductAll> createGetProductAll(GetProductAll value) {
        return new JAXBElement<GetProductAll>(_GetProductAll_QNAME, GetProductAll.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetItemByCodeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.legacy.apsh.com/", name = "getItemByCodeResponse")
    public JAXBElement<GetItemByCodeResponse> createGetItemByCodeResponse(GetItemByCodeResponse value) {
        return new JAXBElement<GetItemByCodeResponse>(_GetItemByCodeResponse_QNAME, GetItemByCodeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetResourceAll }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.legacy.apsh.com/", name = "getResourceAll")
    public JAXBElement<GetResourceAll> createGetResourceAll(GetResourceAll value) {
        return new JAXBElement<GetResourceAll>(_GetResourceAll_QNAME, GetResourceAll.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetItemAll }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.legacy.apsh.com/", name = "getItemAll")
    public JAXBElement<GetItemAll> createGetItemAll(GetItemAll value) {
        return new JAXBElement<GetItemAll>(_GetItemAll_QNAME, GetItemAll.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetResourceByCodeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.legacy.apsh.com/", name = "getResourceByCodeResponse")
    public JAXBElement<GetResourceByCodeResponse> createGetResourceByCodeResponse(GetResourceByCodeResponse value) {
        return new JAXBElement<GetResourceByCodeResponse>(_GetResourceByCodeResponse_QNAME, GetResourceByCodeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetProductByCode }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.legacy.apsh.com/", name = "getProductByCode")
    public JAXBElement<GetProductByCode> createGetProductByCode(GetProductByCode value) {
        return new JAXBElement<GetProductByCode>(_GetProductByCode_QNAME, GetProductByCode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetProductByCodeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.legacy.apsh.com/", name = "getProductByCodeResponse")
    public JAXBElement<GetProductByCodeResponse> createGetProductByCodeResponse(GetProductByCodeResponse value) {
        return new JAXBElement<GetProductByCodeResponse>(_GetProductByCodeResponse_QNAME, GetProductByCodeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetResourceAllResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.legacy.apsh.com/", name = "getResourceAllResponse")
    public JAXBElement<GetResourceAllResponse> createGetResourceAllResponse(GetResourceAllResponse value) {
        return new JAXBElement<GetResourceAllResponse>(_GetResourceAllResponse_QNAME, GetResourceAllResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetResourceByCode }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.legacy.apsh.com/", name = "getResourceByCode")
    public JAXBElement<GetResourceByCode> createGetResourceByCode(GetResourceByCode value) {
        return new JAXBElement<GetResourceByCode>(_GetResourceByCode_QNAME, GetResourceByCode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetItemAllResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.legacy.apsh.com/", name = "getItemAllResponse")
    public JAXBElement<GetItemAllResponse> createGetItemAllResponse(GetItemAllResponse value) {
        return new JAXBElement<GetItemAllResponse>(_GetItemAllResponse_QNAME, GetItemAllResponse.class, null, value);
    }

}
