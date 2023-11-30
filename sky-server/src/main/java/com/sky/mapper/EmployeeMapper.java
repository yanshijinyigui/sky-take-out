package com.sky.mapper;

import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    //修改密码
    @Update("update employee set password=#{newPassword} where id=#{empId} and password=#{oldPassword}")
    void updatePassword(PasswordEditDTO O);

    //启用、禁用员工账号
    @Update("update employee set status=#{status}  where id=#{id}")
    void startOrStop( Integer status, Long id);

    /**员工分页查询
     */
    List<Employee> pageQuery(EmployeePageQueryDTO O);




    //新增员工

    void newEmp(Employee o);

    //根据id查询员工
    Employee SearchEmp(String o);

    //编辑员工信息
    void update(Object o);


}
