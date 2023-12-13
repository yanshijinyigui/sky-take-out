package com.sky.Task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {


    @Autowired
    OrderMapper orderMapper;
    //每分钟执行一次
    //查询超过15分钟的订单
    @Scheduled(cron = "0 * * * * ?")
    public void OutTimeOrder(){
        LocalDateTime time=LocalDateTime.now().minusMinutes(15);
       List<Orders> list= orderMapper.getByStatusAndTime(Orders.PENDING_PAYMENT,time);
        // 修改订单状态
       if(list!=null&&list.size()>0){
           System.out.println("开始清理,本次将清理"+list.size() +"条");
           list.forEach(o -> {
               orderMapper.update(Orders.builder()
                       .id(o.getId())
                       .status(Orders.CANCELLED)
                       .cancelReason("超时未支付")
                       .cancelTime(LocalDateTime.now())
                       .build()
               );
           });
           System.out.println("清理成功");
       }
       else{
           System.out.println("当前没有订单超时");
       }
    }




    /**
     * 处理“派送中”状态的订单
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder(){
        LocalDateTime time = LocalDateTime.now().minusHours(3);
        List<Orders> ordersList = orderMapper.getByStatusAndTime(Orders.DELIVERY_IN_PROGRESS, time);
        if(ordersList != null && ordersList.size() > 0){
            System.out.println("开搞"+ordersList.size() +"条");
            ordersList.forEach(order -> {
                order.setStatus(Orders.COMPLETED);
                orderMapper.update(order);
            });
            System.out.println("搞完");
        }else{
            System.out.println("别搞");
        }
    }

}
