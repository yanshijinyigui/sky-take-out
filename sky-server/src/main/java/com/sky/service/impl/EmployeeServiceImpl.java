package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }


    //修改密码
    @Override
    public void updatePassword(PasswordEditDTO O) {
        employeeMapper.updatePassword(O);
    }

    //启用、禁用员工账号
    @Override
    public void startOrStop(Integer status, Long id) {
        employeeMapper.startOrStop(status, id);

    }

    /**
     * 员工分页查询
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO u) {

        int size = employeeMapper.pageQuery(new EmployeePageQueryDTO(u.getName(), u.getPage(), u.getPageSize())).size();
        List<Employee> employees = employeeMapper.pageQuery(new EmployeePageQueryDTO(u.getName(), u.getPage(), u.getPageSize()));

        PageResult pageResult = new PageResult(size,employees);
        return pageResult;
    }


    //新增员工
    @Override
    public void newEmp(Employee o) {
        o.setCreateTime(LocalDateTime.now());
        o.setUpdateTime(LocalDateTime.now());
        o.setCreateUser(o.getId());
        o.setUpdateUser(o.getId());
        employeeMapper.newEmp(o);
    }


    //根据id查询员工
    @Override
    public Employee SearchEmp(String o) {
        return employeeMapper.SearchEmp(o);
    }

    //编辑员工信息
    @Override
    public void update(Employee o) {
        employeeMapper.update(o);

    }
}
