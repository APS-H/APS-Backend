package apsh.backend.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ResourceServiceTest {
    @Autowired
    ResourceService service;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String beginTime = "2018-07-28 14:42:32";
    @Test
    void getResourceLoad() throws ParseException {
        Date date1 = format.parse(beginTime);
        service.getResourceLoad(date1,1,7);

    }

    @Test
    void getResourceUse() throws ParseException {
        Date date1 = format.parse(beginTime);
        service.getResourceUse(date1,1,7);

    }
}