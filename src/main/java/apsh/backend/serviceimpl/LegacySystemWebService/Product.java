
package apsh.backend.serviceimpl.LegacySystemWebService;

import apsh.backend.po.Craft;
import apsh.backend.po.Equipment;
import apsh.backend.po.Human;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>product complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="product">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="itemCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ProductTechList" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="ProductTech" type="{http://service.legacy.apsh.com/}productTech" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "product", propOrder = {
    "itemCode",
    "productTechList"
})
public class Product {

    protected String itemCode;
    @XmlElement(name = "ProductTechList")
    protected Product.ProductTechList productTechList;

    /**
     * ��ȡitemCode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemCode() {
        return itemCode;
    }

    /**
     * ����itemCode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemCode(String value) {
        this.itemCode = value;
    }

    /**
     * ��ȡproductTechList���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Product.ProductTechList }
     *     
     */
    public Product.ProductTechList getProductTechList() {
        return productTechList;
    }

    /**
     * ����productTechList���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Product.ProductTechList }
     *     
     */
    public void setProductTechList(Product.ProductTechList value) {
        this.productTechList = value;
    }


    /**
     * <p>anonymous complex type�� Java �ࡣ
     * 
     * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="ProductTech" type="{http://service.legacy.apsh.com/}productTech" maxOccurs="unbounded" minOccurs="0"/>
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
        "productTech"
    })
    public static class ProductTechList {

        @XmlElement(name = "ProductTech")
        protected List<ProductTech> productTech;

        /**
         * Gets the value of the productTech property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the productTech property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getProductTech().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ProductTech }
         * 
         * 
         */
        public List<ProductTech> getProductTech() {
            if (productTech == null) {
                productTech = new ArrayList<ProductTech>();
            }
            return this.productTech;
        }

    }


    public Craft getCraft() {
        int size = productTechList.getProductTech().size();
        int needPeopleCount=0;
        int standardCapability=0;
        List<Human> hlist=new ArrayList<Human>();
        List<Equipment> elist=new ArrayList<Equipment>();
        for (int i = size - 1; i >= 0; i--) {
            if(productTechList.getProductTech().get(i).getName().equals("装配")){
                needPeopleCount=productTechList.getProductTech().get(i).getEmployerCount();
                for (ProductResource PR :productTechList.getProductTech().get(i).resourceList.getProductResource()){
                    standardCapability=PR.capacity;
                    if(PR.name.equals("班组")){
                        Human newGroup=new Human();
                        newGroup.setGroupName(PR.code);
                        hlist.add(newGroup);
                    }
                    else if(PR.getName().equals("线体")){
                        Equipment newequipment=new Equipment();
                        newequipment.setName(PR.code);
                        elist.add(newequipment);
                    }
                }
                break;
            }

        }
        Craft craft = new Craft(itemCode,needPeopleCount,hlist,elist,standardCapability );
        return craft;
    }

}
