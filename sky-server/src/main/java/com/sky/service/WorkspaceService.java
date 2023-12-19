package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

import java.time.LocalDateTime;
import java.util.Map;

public interface WorkspaceService {
    /**
     * 营业额总览
     */
    BusinessDataVO businessData(LocalDateTime begin, LocalDateTime end);

    /**
     * 订单总览
     */
    OrderOverViewVO getOrderOverView(Map map);

    /**
     * 菜品总览
     */
    DishOverViewVO getDishOverView();

    /**
     * 套餐总览
     */
    SetmealOverViewVO getSetmealOverView();
}
