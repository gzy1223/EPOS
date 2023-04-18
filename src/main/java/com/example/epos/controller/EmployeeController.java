package com.example.epos.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.epos.common.R;
import com.example.epos.entity.Employee;
import com.example.epos.service.EmployeeService;
import com.example.epos.service.SendMailService;
import com.example.epos.service.impl.FireBaseConnect;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private SendMailService sendMailService;
    @Autowired
    private FireBaseConnect fireBaseConnect;
    /**
      * @Author: GZY
      * @Description:
      * @Date: 22/01/2023
      * @Param request:
     * @Param employee:
      * @return: com.example.epos.common.R<com.example.epos.entity.Employee>
      **/
    @PostMapping("/login")  //request to store the session
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) throws IOException, ExecutionException, InterruptedException, MessagingException {
        //get the password and encrypted with MD5
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //search the database based on the username
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper); // get the unique value
        //check if the username is got
        if(emp== null)
        {
            return R.error("login fail");
        }
        // check the password
        if (!emp.getPassword().equals(password))
        {
            return R.error("login fail");
        }
        // check the staff status
        if (emp.getStatus()==0)
        {
            return R.error("banned");
        }
        sendMailService.loginHint(emp);
        //login sucessfully, put the staff id into Session and return the successful result
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }
    /**
      * @Author: GZY
      * @Description:
      * @Date: 23/01/2023
      * @Param request: 
      * @return: com.example.epos.common.R<java.lang.String> 
      **/
    
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        // clean the staff id information stored in the Session
        request.getSession().removeAttribute("employee");
        return R.success("successfully logout");
    }
    /**
      * @Author: GZY
      * @Description:
      * @Date: 25/01/2023
      * @Param request: 
     * @Param employee: 
      * @return: com.example.epos.common.R<java.lang.String> 
      **/
    
    @PostMapping("")
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee)
    {
        //set the primitive password, but use md5 to encrypt
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
    //    employee.setCreateTime(LocalDateTime.now());
    //    employee.setUpdateTime(LocalDateTime.now());

        //get the current login id
        Long empId = (long )request.getSession().getAttribute("employee");
    //    employee.setCreateUser(empId);
    //    employee.setUpdateUser(empId);

        employeeService.save(employee);

        return R.success("successfully add the staff");
    }
    // staff message search by pages
    /**
      * @Author: GZY
      * @Description: search employee information on page
      * @Date: 25/01/2023
      * @Param page:
     * @Param pageSize:
     * @Param name:
      * @return: com.example.epos.common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page>
      **/

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name)
    {
        log.info("page = {},pageSize={},name={}",page,pageSize,name);
        //configure the page
        Page pageinfo = new Page(page,pageSize);

        //configure the condition
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //add a filter condition
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //search sort condition
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //enquiry
        employeeService.page(pageinfo,queryWrapper);
        return R.success(pageinfo);
    }
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee)
    {
        log.info(employee.toString());
       // Long empId = (Long) request.getSession().getAttribute("employee");
       // employee.setUpdateTime(LocalDateTime.now());
       // employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return R.success("Success");
    }
    /**
      * @Author: GZY
      * @Description: search information based on the Id
      * @Date: 25/01/2023
      * @Param id:
      * @return: com.example.epos.common.R<com.example.epos.entity.Employee>
      **/

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id)
    {
        log.info("searching staff information");
        Employee employee = employeeService.getById(id);
        if (employee!= null)
        {
            return R.success(employee);
        }
        return R.error("not find");
    }

}
