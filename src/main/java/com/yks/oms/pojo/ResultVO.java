/*  
 * 文件名：ResultVO.java  
 * 版权：Copyright by www.youkeshu.com  
 * 描述：代码注释以及格式化示例
 * 创建人：孙伸  
 * 创建时间：2018/2/2    
 * 修改理由：  
 * 修改内容：  
 */
package com.yks.oms.pojo;

import java.io.Serializable;

/**
 * @author 孙伸
 * @version 1.0
 * @date 2018/2/2
 * @see
 * @since JDK1.8
 */
public class ResultVO implements Serializable{
    /**
     * 响应业务状态
     * */
    private Integer status;

    /**
     * 响应消息
     */
    private String msg;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
