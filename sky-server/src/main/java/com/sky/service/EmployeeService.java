package com.sky.service;

import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

import java.util.List;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);



    //修改密码
    void updatePassword(PasswordEditDTO O);

    //启用、禁用员工账号
    void startOrStop( Integer status, Long id);

    /**员工分页查询*/
    PageResult pageQuery(EmployeePageQueryDTO empPage);


    //新增员工
    void newEmp(Employee o);

    //根据id查询员工
    Employee SearchEmp(String o);

    //编辑员工信息
    void update(Employee o);



}
