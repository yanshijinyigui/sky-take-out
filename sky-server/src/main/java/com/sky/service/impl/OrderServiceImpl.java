package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;


    /**
     * 下单
     *
     * @param ordersSubmitDTO
     * @return
     */
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {

        //1.查询当前用户收货地址
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        //异常情况的处理（收货地址为空、超出配送范围、购物车为空）
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
//*
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);

        //2.查询当前用户的购物车数据
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.selectByCard(shoppingCart);
        if (shoppingCartList == null || shoppingCartList.size() == 0) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        //3.构造订单数据
        Orders order = new Orders();
        order.setPhone(addressBook.getPhone());//手机号
        order.setAddress(addressBook.getDetail());//地址
        order.setConsignee(addressBook.getConsignee());//收货人
        order.setNumber(String.valueOf(System.currentTimeMillis()));//订单号
        order.setUserId(userId);
        order.setStatus(Orders.PENDING_PAYMENT);//订单状态
        order.setPayStatus(Orders.UN_PAID);//付款状态
        order.setOrderTime(LocalDateTime.now());

        BeanUtils.copyProperties(ordersSubmitDTO,order);

        //向订单表插入1条数据
        orderMapper.insert(order);

        //4.订单明细数据   （获取2中购物车信息）
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart cart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(order.getId());
            orderDetailList.add(orderDetail);
        }

        //向明细表插入订单
        orderDetailMapper.insertBatch(orderDetailList);

        //清空购物车
        shoppingCartMapper.deleteByUserId(userId);

        //封装返回结果
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(order.getId())
                .orderNumber(order.getNumber())
                .orderAmount(order.getAmount())
                .orderTime(order.getOrderTime())
                .build();

        return orderSubmitVO;
    }

    /**
     * 用户端订单分页查询
     * @return
     */
    public PageResult pageQuery4User(int pageNum, int pageSize, Integer status) {
        // 设置分页
        PageHelper.startPage(pageNum, pageSize);

        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        ordersPageQueryDTO.setStatus(status);

        // 分页条件查询
        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);

        List<OrderVO> list = new ArrayList();

        // 查询出订单明细并封装
        if (page != null && page.getTotal() > 0) {
            for (Orders orders : page) {
                Long orderId = orders.getId();// 订单id

                // 查询订单明细
                List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orderId);

                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                orderVO.setOrderDetailList(orderDetails);

                list.add(orderVO);
            }
        }
        return new PageResult(page.getTotal(), list);
    }

    /**
     * 查询订单详情
     */
    public OrderVO details(Long id) {
        // 根据id查询订单
        Orders orders = orderMapper.getById(id);

        // 查询
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());

        // 封装到OrderVO
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetailList);

        return orderVO;
    }

}