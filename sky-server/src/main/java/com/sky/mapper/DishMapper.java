package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);



    @AutoFill(value = OperationType.INSERT)
    void insertDish(Dish dish);



    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);



    @Select("select * from dish where id=#{id}")
    Dish SelectDishById(long id);



    /**
     * 根据分类查询菜品
     * @param categoryId
     * @return
     */
    @Select("select * from dish where category_id=#{categoryId}")
    List<Dish> SelectDishByCategoryId(String categoryId);


    /**
     * 根据id动态修改菜品数据
     *
     * @param dish
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 批量删除
     * @param ids
     */

    void deleteByIds(List<Long> ids);

    /**
     * 根据条件统计菜品数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);

}
