package com.sky.mapper;

import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface SetmealDishMapper {





    List<Long> countByDishId(List<Long> ids);

    void insert(List<SetmealDish> ids);

    //在套餐菜品表中套餐ID查询
    List<SetmealDish> countBySetmealId(long id);


    @Delete("delete from setmeal_dish where setmeal_id = #{id}")
    void deleteDishBySetMealId(Long id);
}
