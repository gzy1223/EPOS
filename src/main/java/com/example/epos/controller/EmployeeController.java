package com.example.epos.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.epos.common.R;
import com.example.epos.common.Snowflake;
import com.example.epos.dto.Staff;
import com.example.epos.entity.Employee;
import com.example.epos.firemapper.RestaurantMapper;
import com.example.epos.firemapper.RestaurantSercurityMapper;
import com.example.epos.firemapper.staffMapper;
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
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.example.epos.common.PasswordHandler.generateStaffPassword;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private SendMailService sendMailService;
    /**
      * @Author: GZY
      * @Description:
      * @Date: 22/01/2023
      * @Param request:
     * @Param employee:
      * @return: com.example.epos.common.R<com.example.epos.entity.Employee>
      **/
    @PostMapping("/login")  //request to store the session
    public R<Staff> login(HttpServletRequest request, @RequestBody Employee employee) throws IOException, ExecutionException, InterruptedException, MessagingException {
        //get the password and encrypted with MD5
        Staff staff = new Staff();
        staff.setUsername(employee.getUsername());
        staff.setPassword(employee.getPassword());

        staffMapper staffMapper = new staffMapper();
        // check the staff status
        RestaurantSercurityMapper restaurantSercurityMapper = new RestaurantSercurityMapper();


        //login sucessfully, put the staff id into Session and return the successful result

        if (staffMapper.staffLogin(staff)==1)
        {
            return R.success(staff);
        }else if (staffMapper.staffLogin(staff)==0){
            return R.error("No such user");
        }else if (staffMapper.staffLogin(staff)==2){
            return R.error("Wrong password");
        }else{
            return R.error("unknown error");
        }

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
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee) throws MessagingException, IOException, ExecutionException, InterruptedException {
        Staff staff = new Staff();
        staff.setUsername(employee.getUsername());
        String password = generateStaffPassword();

        staff.setPassword(password);
        staff.setUid((int) Snowflake.nextId());
        staff.setEmail(employee.getIdNumber());
        sendMailService.passwordEmail(staff,password);


        staffMapper staffMapper = new staffMapper();
        staffMapper.addStaff(staff);

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
    public R<Page> page(int page, int pageSize, String name) throws ExecutionException, InterruptedException {
        log.info("page = {},pageSize={},name={}",page,pageSize,name);
        //configure the page
        Page pageinfo = new Page(page,pageSize);
        ArrayList<Staff> staffArrayList = new ArrayList<>();
        staffArrayList = staffMapper.findStaffByName(name);
        //configure the condition
        pageinfo.setRecords(staffArrayList);
        //enquiry
        return R.success(pageinfo);
    }

    /**
      * @Author: GZY
      * @Description: search information based on the Id
      * @Date: 25/01/2023
      * @Param id:
      * @return: com.example.epos.common.R<com.example.epos.entity.Employee>
      **/

    @DeleteMapping("delete/{id}")
    public R<Staff> getById(@PathVariable Long id) throws ExecutionException, InterruptedException {
        staffMapper staffMapper = new staffMapper();
        Staff staff = staffMapper.findStaffById(id);
        int status = staffMapper.deleteStaff(id);
        if (status==1)
        {
            return R.success(staff);
        }else{
            return R.error("delete failed");
        }
    }

}
