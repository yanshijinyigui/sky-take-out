package com.sky.controller.admin;

import com.sky.entity.Orders;
import com.sky.result.Result;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/admin/workspace")
public class WorkspaceController {
    @Autowired
    WorkspaceService workspaceService;


    @GetMapping("/businessData")
    public Result<BusinessDataVO> Business(){
        //获得当天的开始时间
        LocalDateTime begin = LocalDateTime.now().with(LocalTime.MIN);
        //获得当天的结束时间
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);


        return Result.success(workspaceService.businessData(begin, end));
    }


    /**
     * 订单
     */
    @GetMapping("/overviewOrders")
    public Result<OrderOverViewVO> orderOverView(){

        //获得一天  加入map/list集合   使用日期+状态查询

        Map map = new HashMap();
        map.put("begin", LocalDateTime.now().with(LocalTime.MIN));
        map.put("status", 2);

        return Result.success(workspaceService.getOrderOverView(map));
    }

    /**
     * 菜品
     */
    @GetMapping("/overviewDishes")
    public Result<DishOverViewVO> dishOverView(){
        //根据状态查询  不能传参  参数会改变
        return Result.success(workspaceService.getDishOverView());
    }

    /**
     * 套餐总览
     */
    @GetMapping("/overviewSetmeals")
    public Result<SetmealOverViewVO> setmealOverView(){
        //根据状态查询  不能传参  参数会改变
        return Result.success(workspaceService.getSetmealOverView());
    }
}
