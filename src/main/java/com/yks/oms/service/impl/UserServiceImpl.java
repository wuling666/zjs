package com.yks.oms.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yks.oms.constant.OMSConstant;
import com.yks.oms.entity.User;
import com.yks.oms.pojo.BaseDataVO;
import com.yks.oms.pojo.LogVO;
import com.yks.oms.service.UserService;
import com.yks.oms.utils.HttpClientUtil;
import com.yks.oms.utils.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * TODO
 *
 * @author sunshen
 * @version 1.0
 * @date 2018-01-30
 * @see
 * @since JDK1.8
 */
@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    /**
     * 对接SSO系统参数
     */
    @Value("${dingdan.sso.ticket_url}")
    private String ssoTicketUrl;
    @Value("${dingdan.sso.info_url}")
    private String ssoInfoUrl;
    @Value("${dingdan.sso.md5_salt}")
    private String md5Salt;
    @Value("${dingdan.sso.name}")
    private String sysName;

    /**
     * @param user 用户账号和密码封装在此对象中
     * @description TODO 用户登录
     */
    @Override
    public BaseDataVO login(User user) {
        // 去SSO获取ticket
        String ticket = getTicketFromSSO();

        // 去SSO获取用户权限信息
        String result = getInfoFromSSO(user.getUsername(), user.getPassword(), ticket);
        BaseDataVO baseDataVO = parseSSOResult(result);
        return baseDataVO;
    }

    private String getTicketFromSSO() {
        String result = HttpClientUtil.doGet(ssoTicketUrl);
        String jsonStr = result.substring(result.indexOf("(") + 1, result.lastIndexOf(")"));
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        String ticket = jsonObject.getString("ticket");
        return ticket;
    }

    /**
     * @param username 登录用户账号
     * @param password 密码
     * @param ticket   票据
     * @return String 请求SSO返回的json字符串
     * @description 去SSO获取用户权限信息
     * @author sunshen
     * @date 2018-01-30
     */
    private String getInfoFromSSO(String username, String password, String ticket) {
        StringBuffer url = new StringBuffer(this.ssoInfoUrl);
        url.append("&user=" + username);
        url.append("&pwd=" + password);
        url.append("&ticket=" + ticket);

        // 需要加密的MD5字段，由请求的参数的值拼接而成
        String md5Str = "of_base_sso_apicheckdefault" + this.sysName + "1"
                + username + password + ticket + this.md5Salt;
        url.append("&md5=" + MD5Util.encode2hex(md5Str));

        String result = HttpClientUtil.doGet(url.toString());
        // 打印日志
        LogVO logVO = new LogVO();
        logVO.setParam("{\"username\":" + username + ",password:\"****\",\"ticket\":" + ticket + "}");
        logVO.setMessage("SSO 返回的数据:" + result);
        logger.info(logVO.toString());
        return result;
    }

    /**
     * @param ssoResult SSO返回的信息
     * @description  解析从SSO返回的数据
     * @author sunshen
     * @date 2018-01-30
     */
   private BaseDataVO parseSSOResult(String ssoResult) {
        BaseDataVO baseDataVO = new BaseDataVO();
        JSONObject resultJson = JSON.parseObject(ssoResult);
        Integer state = resultJson.getInteger("state");
       // 如果返回码不是200或者402，则直接返回
       if (state.equals(OMSConstant.HTTP_200) && state.equals(OMSConstant.HTTP_402)) {
           baseDataVO.setStatus(state);
           baseDataVO.setMsg("系统故障");
           return baseDataVO;
       }

       // 返回码402说明有错误，例如密码错误
       if (state.equals(OMSConstant.HTTP_402)) {
           String msg;
           msg = resultJson.getString("msg");
           baseDataVO.setMsg(msg);
           baseDataVO.setStatus(state);
           return baseDataVO;
       }

       // 返回码200说明有权限
       if (state.equals(OMSConstant.HTTP_200)) {
           // 获取用户的权限信息
           List<String> userPrivilege = this.getUserPrivilege(resultJson);
           if (userPrivilege.size() > 0) {
               // 有权限信息，则说明有权限
               baseDataVO.setFunc(userPrivilege);
               baseDataVO.setStatus(state);
               baseDataVO.setMsg("成功");
               baseDataVO.setName(resultJson.getString("name"));
               baseDataVO.setNike(resultJson.getString("nike"));
               baseDataVO.setTicket(resultJson.getString("ticket"));
               baseDataVO.setUser(resultJson.getString("user"));
                if (resultJson.getJSONObject("role").getJSONObject("allow").getJSONObject("pack").containsKey("admin")){
                    baseDataVO.setAdmin(true);
                }else {
                    baseDataVO.setAdmin(false);
                }
           } else {
               // 无权限信息说明无权限
               baseDataVO.setStatus(state);
               baseDataVO.setMsg("没有权限");
           }
       }
        return baseDataVO;
    }

    /**
     * @param ssoResultJson SSO返回的信息
     * @return List<String> 权限信息列表
     * @description TODO 获取用户的权限
     * @author chenjiangxin
     * @date 2018-01-08 17:38:00
     */
    private List<String> getUserPrivilege(JSONObject ssoResultJson) {
        // 权限信息封装在 allow 层中
        JSONObject allowRole = ssoResultJson.getJSONObject("role").getJSONObject("allow");
        System.out.println(allowRole);
        List<String> allPrivilege = new ArrayList<>();

        // 有权限，则解析
        if (allowRole != null) {
            // 有数据的情况： pack里面是这样的  {里面有数据}
            // 无数据的情况： pack里面是这样的  [里面没数据]
            // 两种不同的数据类型，所以，只能这样判断到底有没数据
            String packString = allowRole.getString("pack");
            if (!(packString.indexOf("[]") == 0)) {
                JSONObject packJson = allowRole.getJSONObject("pack");
                // 有权限数据，则解析权限
                allPrivilege = this.getAllPrivilege(packJson);
            }
        }
        return allPrivilege;
    }

    /**
     * @param packJson 权限所在的Json层
     * @return List<String> 权限信息列表
     * @description TODO 获得该用户的所有权限
     * @author chenjiangxin
     * @date 2018-01-08 16:11:31
     */
    private List<String> getAllPrivilege(JSONObject packJson) {
        List<String> allPrivilege = new ArrayList<>();
        System.out.println(packJson);
        System.out.println(packJson.entrySet());
        Iterator<Map.Entry<String, Object>> packIterator = packJson.entrySet().iterator();
        System.out.println(packIterator);
        // 遍历用户所拥有的角色
        while (packIterator.hasNext()) {
            Map.Entry<String, Object> entry = packIterator.next();
            // 获取每个角色下面的权限
            JSONObject privilegeJson = packJson.getJSONObject(entry.getKey()).getJSONObject("func");
            Set<String> privilegeSet = privilegeJson.keySet();
            Iterator<String> privilegeIterator = privilegeSet.iterator();
            // 遍历该角色下面的所有权限
            while (privilegeIterator.hasNext()) {
                String privilege = privilegeIterator.next();
                if (!(allPrivilege.contains(privilege))) {
                    allPrivilege.add(privilege);
                }
            }
        }
        return allPrivilege;
    }
}
