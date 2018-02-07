package com.yks.oms.controller;

import com.yks.oms.constant.OMSConstant;
import com.yks.oms.entity.User;
import com.yks.oms.pojo.BaseDataVO;
import com.yks.oms.pojo.ResultVO;
import com.yks.oms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author sunshen
 * */
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    /**
     * @param user 用户账号和密码封装在此对象中
     * @return BaseDataVO 请求SSO返回的数据封装的对象
     * @author sunshen
     * @date 2018-01-31
     */
    @RequestMapping(value = "api/oms/user/login",method = RequestMethod.POST)
    @ResponseBody
    public BaseDataVO login(@RequestBody User user, HttpSession session, HttpServletResponse response){
        BaseDataVO baseDataVO = userService.login(user);
        //用于ajax post跨域（*，最好指定确定的http等协议+ip+端口号）
        response.addHeader("Access-Control-Allow-Origin", "*");
        //登陆成功
        if (OMSConstant.HTTP_200.equals(baseDataVO.getStatus())){
            user.setTicket(baseDataVO.getTicket());
            session.setAttribute("user",user);
        }
        return baseDataVO;
    }

    /**
     * 登出
     */
    @RequestMapping(value = "api/oms/user/logout",method = RequestMethod.POST)
    @ResponseBody
    public ResultVO logout(HttpSession session){
        ResultVO resultVO = new ResultVO();
        try {
            session.invalidate();
            resultVO.setStatus(201);
            resultVO.setMsg("登出成功");
            return resultVO;
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setStatus(OMSConstant.HTTP_500);
            resultVO.setMsg("登出失败");
            return resultVO;
        }
    }

    @RequestMapping(value = "api/oms/user/test",method = RequestMethod.POST)
    @ResponseBody
    public String test(){
        System.out.println("测试");
        return "测试";
    }
}
