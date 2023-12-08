package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.MD5SaltedHash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
     EmployeeMapper employeeMapper;

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
        if(!MD5SaltedHash.verifyPassword(password,employee.getPassword())){
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
        String newPassword = O.getNewPassword();
        String s = MD5SaltedHash.hashAndSaltPassword(newPassword);
        O.setNewPassword(s);
        System.out.println(O);
        employeeMapper.updatePassword(O);
    }

    //启用、禁用员工账号
    @Override
    public void startOrStop(Integer status, Long id) {

        Employee employee = new Employee();
        employee.setStatus(status);
        employee.setId(id);

        employeeMapper.update(employee);

    }

    /**
     * 员工分页查询
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO u) {
        PageHelper.startPage(u.getPage(),u.getPageSize());

        int size = employeeMapper.pageQuery(u).size();
        List<Employee> list = employeeMapper.pageQuery(u);


        System.out.println(list);

        PageResult pageResult = new PageResult(size,list);
        return pageResult;
    }


    //新增员工
    @Override
    public void newEmp(EmployeeDTO EMP) {
        Employee emp = new Employee();
        BeanUtils.copyProperties(EMP, emp);

        //添加默认密码并加密
        emp.setStatus(StatusConstant.ENABLE);
        emp.setPassword(MD5SaltedHash.hashAndSaltPassword("123456"));

   /*     emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        emp.setCreateUser(BaseContext.getCurrentId());
        emp.setUpdateUser(BaseContext.getCurrentId());*/

        employeeMapper.newEmp(emp);
    }


    //根据id查询员工
    @Override
    public Employee SearchEmp(String o) {
        return employeeMapper.SearchEmp(o);
    }

    /**
     * 编辑员工信息
     *
     * @param employeeDTO
     */
    public void update(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        /*employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(BaseContext.getCurrentId());*/

        employeeMapper.update(employee);
    }
}
