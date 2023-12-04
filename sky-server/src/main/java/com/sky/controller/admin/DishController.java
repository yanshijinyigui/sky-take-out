package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;

import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品")
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     *
     * 菜品添加
     *
     * */
    @PostMapping
    @ApiOperation("菜品添加")
    public Result save(@RequestBody DishDTO dishDTO) {
        dishService.saveDishWithFlavor(dishDTO);
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
     *
     * 菜品删除
     *
     * */
    @DeleteMapping
    @ApiOperation("菜品删除")
    public  Result delete( @RequestParam List<Long> ids){

        dishService.deleteBatch(ids);

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
        return Result.success();
    }

}
