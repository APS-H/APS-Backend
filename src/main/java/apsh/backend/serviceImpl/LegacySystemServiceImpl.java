package apsh.backend.serviceImpl;

import apsh.backend.po.Equipment;
import apsh.backend.po.Human;
import apsh.backend.po.Order;
import apsh.backend.service.LegacySystemService;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
@Service
public class LegacySystemServiceImpl implements LegacySystemService {
    String orderServiceUrl = "http://123.57.73.97:30109/order?wsdl";
    String ERPServiceUrl = "http://123.57.73.97:30309/erp?wsdl";

    @Override
    public List<Order> getAllOrders() {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient(orderServiceUrl);
        Object[] objects = new Object[0];
        try {
            // invoke("方法名",参数1,参数2,参数3....);
            objects = client.invoke("getOrderAll");
            List<Object> orders = (List)(objects[0]);
            List<Order> allOrders = orders.stream().map(o -> {
                Order order = null;
                try {
                    order = new Order(o);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
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
            List<Object> resources = (List)(objects[0]);

            List<Human> allHumans = new ArrayList<Human>();
            Field f = resources.get(0).getClass().getDeclaredField("name");
            f.setAccessible(true);
            for (Object o : resources) {
                if (f.get(o).equals("班组")) {
                    Human human = new Human(o);
                    allHumans.add(human);
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
            List<Object> resources = (List) (objects[0]);

            List<Equipment> allEquipments = new ArrayList<Equipment>();
            Field f = resources.get(0).getClass().getDeclaredField("name");
            f.setAccessible(true);
            Field ff = resources.get(0).getClass().getDeclaredField("count");
            ff.setAccessible(true);
            for (Object o : resources) {
                if (f.get(o).equals("线体") || f.get(o).equals("设备")) {
                    int count = (int) ff.get(o);
                    for (int i = 0; i < count; i++) {
                        Equipment equipment = new Equipment(o);
                        allEquipments.add(equipment);
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
