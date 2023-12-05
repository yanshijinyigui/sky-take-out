package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    void saveDishWithFlavor(DishDTO dishDTO);

    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);



    /**
     *
     * @param id
     * @return
     */
    DishVO getByIdWithFlavor(long id);

    /**
     * 根据id修改菜品基本信息和对应的口味信息
     *
     * @param dishDTO
     */
    void updateWithFlavor(DishDTO dishDTO);

    /**
     * 批量删除
     * @param ids
     */
    void deleteBatch(List<Long> ids);



    /**
     *
     * @param status
     * @param id
     */
    void startOrStop(Integer status, long id);



    /**
     *
     * @param categoryId
     * @return
     */
    List<Dish> SelectDishByCategoryId(String categoryId);
}
