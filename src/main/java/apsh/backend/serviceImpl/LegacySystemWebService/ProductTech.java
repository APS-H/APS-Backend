
package apsh.backend.serviceImpl.LegacySystemWebService;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>productTech complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="productTech">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="employerCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="materialList" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="ProductItem" type="{http://service.legacy.apsh.com/}productItem" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="resourceList" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="ProductResource" type="{http://service.legacy.apsh.com/}productResource" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "productTech", propOrder = {
    "name",
    "employerCount",
    "materialList",
    "resourceList"
})
public class ProductTech {

    protected String name;
    protected Integer employerCount;
    protected ProductTech.MaterialList materialList;
    protected ProductTech.ResourceList resourceList;

    /**
     * 获取name属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * 设置name属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * 获取employerCount属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getEmployerCount() {
        return employerCount;
    }

    /**
     * 设置employerCount属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setEmployerCount(Integer value) {
        this.employerCount = value;
    }

    /**
     * 获取materialList属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ProductTech.MaterialList }
     *     
     */
    public ProductTech.MaterialList getMaterialList() {
        return materialList;
    }

    /**
     * 设置materialList属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ProductTech.MaterialList }
     *     
     */
    public void setMaterialList(ProductTech.MaterialList value) {
        this.materialList = value;
    }

    /**
     * 获取resourceList属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ProductTech.ResourceList }
     *     
     */
    public ProductTech.ResourceList getResourceList() {
        return resourceList;
    }

    /**
     * 设置resourceList属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ProductTech.ResourceList }
     *     
     */
    public void setResourceList(ProductTech.ResourceList value) {
        this.resourceList = value;
    }


    /**
     * <p>anonymous complex type的 Java 类。
     * 
     * <p>以下模式片段指定包含在此类中的预期内容。
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="ProductItem" type="{http://service.legacy.apsh.com/}productItem" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "productItem"
    })
    public static class MaterialList {

        @XmlElement(name = "ProductItem")
        protected List<ProductItem> productItem;

        /**
         * Gets the value of the productItem property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the productItem property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getProductItem().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ProductItem }
         * 
         * 
         */
        public List<ProductItem> getProductItem() {
            if (productItem == null) {
                productItem = new ArrayList<ProductItem>();
            }
            return this.productItem;
        }

    }


    /**
     * <p>anonymous complex type的 Java 类。
     * 
     * <p>以下模式片段指定包含在此类中的预期内容。
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="ProductResource" type="{http://service.legacy.apsh.com/}productResource" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "productResource"
    })
    public static class ResourceList {

        @XmlElement(name = "ProductResource")
        protected List<ProductResource> productResource;

        /**
         * Gets the value of the productResource property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the productResource property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getProductResource().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ProductResource }
         * 
         * 
         */
        public List<ProductResource> getProductResource() {
            if (productResource == null) {
                productResource = new ArrayList<ProductResource>();
            }
            return this.productResource;
        }

    }

}
