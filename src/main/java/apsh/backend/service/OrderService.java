package apsh.backend.service;

import apsh.backend.vo.OrderProgressVo;

import java.util.Date;

public interface OrderService {

    OrderProgressVo getOrderProgress(Date date);
}
