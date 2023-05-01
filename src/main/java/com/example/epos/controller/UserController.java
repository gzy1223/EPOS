// test code for learning

//package com.example.epos.controller;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.example.epos.common.R;
//import com.example.epos.entity.User;
//import com.example.epos.service.UserService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpSession;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/user")
//@Slf4j
//public class UserController {
//
//    /**
//      * @Author: GZY
//      * @Description: user login, draft
//      * @Date: 30/01/2023
//      * @Param user:
//      * @return: com.example.epos.common.R<java.lang.String>
//      **/
//    @PostMapping("/sendMsg")
//    public R<String> sendMsg(@RequestBody User user, HttpSession session)
//    {
//        String phone = user.getPhone();
//        //get the phone number
//        String code = "1111";
//        //get the random code
//        session.setAttribute(phone,code);
//
//        return R.success("Success");
//    }
//    @PostMapping("/login")
//    public R<User> login(@RequestBody Map map, HttpSession session)
//    {
//        String phone = map.get("phone").toString();
//
//        String code = map.get("code").toString();
//        //get the random code
//        Object codeInsession = session.getAttribute(phone);
//        if (codeInsession!= null && codeInsession.equals(code))
//        {
//            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(User::getPhone, phone);
//            User user = userService.getOne(queryWrapper);
//            if (user == null)
//            {
//                user = new User();
//                user.setPhone(phone);
//                user.setStatus(1);
//                userService.save(user);
//            }
//            session.setAttribute("user",user.getId());
//            return R.success(user);
//        }
//
//        return R.error("error");
//    }
//}
