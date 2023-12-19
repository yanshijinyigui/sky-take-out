package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {
    @Autowired
     OrderMapper orderMapper;
    @Autowired
     UserMapper userMapper;
    @Autowired
     DishMapper dishMapper;
    @Autowired
     SetmealMapper setmealMapper;

    /**
     * 营业额总览
     */
    @Override
    public BusinessDataVO businessData(LocalDateTime begin, LocalDateTime end) {

        //获得一天  加入map/list集合   使用日期查询
        Map map = new HashMap();
        map.put("begin",begin);
        map.put("end",end);
        //查询总订单数(只需要日期)
        Integer totalOrderCount = orderMapper.countByMap(map);
        //新增用户数(只需要日期)
        Integer newUsers = userMapper.countByMap(map);


        //营业额（需要状态）
        map.put("status", Orders.COMPLETED);
        Double turnover = orderMapper.sumByMap(map);
        if(turnover==null){
            turnover=0.0;
        }

        //有效订单数（需要状态）
        Integer validOrderCount = orderMapper.countByMap(map);


        Double unitPrice = 0.0;
        Double orderCompletionRate = 0.0;
        if(totalOrderCount != 0 && validOrderCount != 0){
            //订单完成率
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
            //平均客单价
            unitPrice = turnover / validOrderCount;
        }


        return BusinessDataVO.builder()
                .turnover(turnover)//营业额
                .validOrderCount(validOrderCount)//有效订单数
                .orderCompletionRate(orderCompletionRate)//订单完成率
                .unitPrice(unitPrice)//平均客单价
                .newUsers(newUsers)//新增用户数
                .build();
    }



    /**
     * 订单总览
     */
    public OrderOverViewVO getOrderOverView(Map map) {

        //待接单
        Integer waitingOrders = orderMapper.countByMap(map);

        //待派送
        map.put("status", Orders.TO_BE_CONFIRMED);
        Integer deliveredOrders = orderMapper.countByMap(map);

        //已完成
        map.put("status", Orders.COMPLETED);
        Integer completedOrders = orderMapper.countByMap(map);

        //已取消
        map.put("status", Orders.CANCELLED);
        Integer cancelledOrders = orderMapper.countByMap(map);

        //全部订单 状态为空
        map.put("status", null);
        Integer allOrders = orderMapper.countByMap(map);

        return OrderOverViewVO.builder()
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders)
                .build();
    }

    /**
     * 菜品总览
     */
    public DishOverViewVO getDishOverView() {
        Map map = new HashMap();
        //成功
        map.put("status", StatusConstant.ENABLE);
        Integer sold = dishMapper.countByMap(map);

        //全部
        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = dishMapper.countByMap(map);

        return DishOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    /**
     * 套餐总览
     */
    public SetmealOverViewVO getSetmealOverView() {
        Map map = new HashMap();
        map.put("status", StatusConstant.ENABLE);
        Integer sold = setmealMapper.countByMap(map);

        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = setmealMapper.countByMap(map);

        return SetmealOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }
}
