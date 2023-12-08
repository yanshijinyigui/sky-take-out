package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;


    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result<PageResult> pageResultResult(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("分页查询：{}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);

        return Result.success(pageResult);
    }


    /**
     * 新增套餐
     */

    @PostMapping
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result saveSetmeal(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐", setmealDTO);
        setmealService.saveSetmeal(setmealDTO);
        return Result.success();
    }

    /**
     * id查询套餐
     */
    @GetMapping("/{id}")
    public Result selectById(@PathVariable long id){
        return Result.success(setmealService.selectById(id));
    }


    /**
     * 修改套餐
     */
    @PutMapping
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result updateSetmeal(@RequestBody SetmealDTO setmealDTO){
        log.info("修改套餐", setmealDTO);
        setmealService.updateSetmeal(setmealDTO);


        return Result.success();
    }

    /**
     * 删除套餐
     */
    @DeleteMapping
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result deleteSetmeal(@RequestParam  List<Long> ids){
        log.info("修改套餐", ids);
        setmealService.deleteSetmeal(ids);



        return Result.success();
    }

    /**
     * 修改套餐状态
     */
    @PostMapping("/status/{status}")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result starOrStop(@PathVariable Integer status,long id){
        log.info("修改套餐状态", status);
        setmealService.starOrStop(status,id);

        return Result.success();
    }






}
