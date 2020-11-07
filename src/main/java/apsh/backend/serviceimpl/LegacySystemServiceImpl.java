package apsh.backend.serviceimpl;

import apsh.backend.po.Craft;
import apsh.backend.po.Equipment;
import apsh.backend.po.Human;
import apsh.backend.po.Order;
import apsh.backend.service.LegacySystemService;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class LegacySystemServiceImpl implements LegacySystemService {


    private final static String orderServiceUrl = "http://81.69.252.233:9001/order?wsdl";
    private final static String erpServiceUrl = "http://81.69.252.233:9003/erp?wsdl";


    @Override
    public List<Order> getAllOrders() {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient(orderServiceUrl);
        try {
            // invoke("方法名",参数1,参数2,参数3....);
            Object[] objects = client.invoke("getOrderAll");
            List<Object> orders = (List) (objects[0]);
            return orders.stream().map(o -> {
                Order order = null;
                try {
                    order = new Order(o);
                } catch (NoSuchFieldException | IllegalAccessException | ParseException e) {
                    e.printStackTrace();
                }
                return order;
            }).collect(Collectors.toList());
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Human> getAllHumans() {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient(erpServiceUrl);
        try {
            // invoke("方法名",参数1,参数2,参数3....);
            Object[] objects = client.invoke("getResourceAll");
            List<Object> resources = (List) (objects[0]);
            Field f = resources.get(0).getClass().getDeclaredField("name");
            f.setAccessible(true);

            return resources.parallelStream()
                    .filter(r -> {
                        try {
                            return "班组".equals(f.get(r));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                            return false;
                        }
                    })
                    .map(r -> {
                        try {
                            return new Human(r);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Equipment> getAllEquipments() {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient(erpServiceUrl);
        Object[] objects = new Object[0];
        try {
            // invoke("方法名",参数1,参数2,参数3....);
            objects = client.invoke("getResourceAll");
            List<Object> resources = (List) (objects[0]);

            List<Equipment> allEquipments = new ArrayList<Equipment>();
            Field fieldName = resources.get(0).getClass().getDeclaredField("name");
            fieldName.setAccessible(true);
            Field fieldCount = resources.get(0).getClass().getDeclaredField("count");
            fieldCount.setAccessible(true);

            for (Object o : resources) {
                if (fieldName.get(o).equals("线体") || fieldName.get(o).equals("设备")) {
                    int count = (int) fieldCount.get(o);
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

//<<<<<<< HEAD:src/main/java/apsh/backend/serviceImpl/LegacySystemServiceImpl.java
//
//    public static void main(String args []){
//        LegacySystemServiceImpl s=new LegacySystemServiceImpl();
//        List<Order> m=s.getAllOrders();
//        int a=0;
//=======
    @Override
    public List<Craft> getAllCrafts() {
        // TODO : 获取所有的工艺路线
        return null;

    }
}
