package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {

        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());

        Page<SetmealVO> page=setmealMapper.pageQuery(setmealPageQueryDTO);
        
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 增加套餐
     *
     * */
    @Override
    public void saveSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);

        
        setmealMapper.insert(setmeal);

        System.out.println(setmeal.getId());

        //套餐-菜品表增添
        List<SetmealDish> s=setmealDTO.getSetmealDishes();

        s.forEach(setmealDish -> setmealDish.setSetmealId(setmeal.getId()));

        setmealDishMapper.insert(s);



    }

    @Override
    public SetmealVO selectById(long id) {
        SetmealVO setmealVO=new SetmealVO();
        //查询套餐表
        Setmeal s=setmealMapper.selectById(id);

        BeanUtils.copyProperties(s,setmealVO);
        //查询套餐-菜品表
        List<SetmealDish> list=setmealDishMapper.countBySetmealId(s.getId());
        setmealVO.setSetmealDishes(list);

        return setmealVO;

    }

    @Transactional
    @Override
    public void updateSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        //修改套餐表
        setmealMapper.updateSetmeal(setmeal);
        //删除套餐-菜品
        setmealDishMapper.deleteDishBySetMealId(setmeal.getId());

        // 增加套餐-菜品
        List<SetmealDish> s=setmealDTO.getSetmealDishes();

        s.forEach(setmealDish -> setmealDish.setSetmealId(setmeal.getId()));

        setmealDishMapper.insert(s);

    }

    @Transactional
    @Override
    public void deleteSetmeal(List<Long> ids) {
        // TODO: 2023/12/6 判断能否删除
        //删除套餐
        setmealMapper.deleteSetmeal(ids);

        //删除菜品
        for (Long id : ids) {
            setmealDishMapper.deleteDishBySetMealId(id);

        }

    }

    @Override
    public void starOrStop(Integer status, long id) {
        Setmeal s = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.updateSetmeal(s);
    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }





}
