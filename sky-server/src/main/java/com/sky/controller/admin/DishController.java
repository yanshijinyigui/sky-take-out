package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;

import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品")
public class DishController {

    @Autowired
     DishService dishService;
    @Autowired
     RedisTemplate redisTemplate;

    /**
     * 清理缓存数据
     * @param pattern
     */
    private void cleanCache(String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
    /**
     *
     * 菜品添加
     *
     * */
    @PostMapping
    @ApiOperation("菜品添加")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品：{}", dishDTO);
        dishService.saveDishWithFlavor(dishDTO);

        String key = "dish_"+dishDTO.getCategoryId();
        cleanCache(key);
        return Result.success();
    }

    /**
     *
     * 菜品分页查询
     *
     * */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("分页查询：{}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }
    /**
     *
     * 菜品ID查询
     *
     * */
    @GetMapping("/{id}")
    @ApiOperation("菜品ID查询")
    public Result SelectDishById(@PathVariable long id) {
        log.info("ID回显", id);
        DishVO D = dishService.getByIdWithFlavor(id);
        return Result.success(D);
    }

    /**
     * 根据分类id查询菜品
     *
     * */
    @GetMapping("/list")
    public Result SelectDishByCategoryId(String categoryId){
        List<Dish> ds=dishService.SelectDishByCategoryId(categoryId);

        return Result.success(ds);
    }
    /**
     *
     * 菜品删除
     *
     * */
    @DeleteMapping
    @ApiOperation("菜品删除")
    public  Result delete( @RequestParam List<Long> ids){
        log.info("菜品批量删除：{}", ids);
        dishService.deleteBatch(ids);

        //将所有的菜品缓存数据清理掉，所有以dish_开头的key
        cleanCache("dish_*");

        return Result.success();
    }

    /**
     * 修改菜品
     *
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品：{}", dishDTO);
        dishService.updateWithFlavor(dishDTO);

        //将所有的菜品缓存数据清理掉，所有以dish_开头的key
        cleanCache("dish_*");
        return Result.success();
    }


    /**
     * 修改菜品状态
     * @return
     */
    @PostMapping("status/{status}")
    @ApiOperation("修改菜品")
    public Result startOrStop(@PathVariable("status") Integer status,long id) {

        dishService.startOrStop( status, id);
        //将所有的菜品缓存数据清理掉，所有以dish_开头的key
        cleanCache("dish_*");
        return Result.success();
    }



}
