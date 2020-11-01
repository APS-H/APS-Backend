package apsh.backend.serviceimpl;

import apsh.backend.po.Equipment;
import apsh.backend.po.Human;
import apsh.backend.po.Order;
import apsh.backend.service.LegacySystemService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
@Service
public class LegacySystemServiceImpl implements LegacySystemService {
    String orderServiceUrl="http://localhost:8083/Order?wsdl";
    String ERPServiceUrl="http://localhost:8086/ERP?wsdl";
    @Override
    public List<Order> getAllOrders() {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient(orderServiceUrl);
        Object[] objects = new Object[0];
        try {
            // invoke("方法名",参数1,参数2,参数3....);
            objects = client.invoke("getOrderAll");
            List<Object> orders=new ArrayList(Arrays.asList(objects));
            List<Order> allOrders=orders.stream().map(o->{
                Order order=new Order(o);
                return order;
            }).collect(Collectors.toList());
            return allOrders;
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Human> getAllHumans() {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient(ERPServiceUrl);
        Object[] objects = new Object[0];
        try {
            // invoke("方法名",参数1,参数2,参数3....);
            objects = client.invoke("getResourceAll");
            List<Object> orders=new ArrayList(Arrays.asList(objects));

            List<Human> allHumans=new ArrayList<Human>();
            for(Object o:objects){
                if(o.name.equals("班组")){
                    Human human=new Human(o);
                    allHumans.add(o);
                }
            }
            return allHumans;
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public List<Equipment> getAllEquipments() {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient(ERPServiceUrl);
        Object[] objects = new Object[0];
        try {
            // invoke("方法名",参数1,参数2,参数3....);
            objects = client.invoke("getResourceAll");
            List<Object> orders=new ArrayList(Arrays.asList(objects));

            List<Equipment> allEquipments=new ArrayList<Equipment>();
            for(Object o:objects){
                if(o.name.equals("线体")){
                    for(int i=0;i<o.count;i++){
                    Equipment equipment=new Equipment(o);
                    allEquipments.add(o);
                    }
                }
            }
            return allEquipments;
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }


        return null;

    }

}
