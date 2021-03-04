package com.retailo2o.smc.export;

import com.alibaba.fastjson.JSONObject;
import com.retailo2o.smc.util.HttpClientUtils;
import net.jplugin.common.kits.MD5Kit;
import org.apache.commons.collections.MapUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述
 *
 * @param
 * @author cxx
 * @date
 * @return
 */
public class Demo {

    void test(){
        Map<String,Object> param=new HashMap<> ();
        Long currentTimeMillis=System.currentTimeMillis ();
        String platform_num="101397";
        String token="DA0FEF30CF4BB5FA8224AC343D618103";
        String url ="http://testpicksys.retailo2o.com/picksysdi/api/orderOperate/getPageRetailProInfo.do";
        param.put ("timeStamp", currentTimeMillis);
        JSONObject paramJson = new JSONObject ();

        param.put ("_platform_num", platform_num);
        param.put ("token", token);
        paramJson.put ("date",currentTimeMillis);
        param.put ("param",paramJson);
        String mySign = MD5Kit.MD5(("key=" + MapUtils.getString (param,"token") + "&_platform_num=" + MapUtils.getString (param,"_platform_num")
                + "&timeStamp=" +currentTimeMillis).toUpperCase());
        param.put ("sign", mySign);

        JSONObject result = HttpClientUtils.getInstance ().post (url,param);
		
        System.out.println(result);//test print
        /**
         * result  格式
         * {
         *   "success": true,
         *   "msg": "",
         *   "code": "0",
         *   "content": {
         *     "result": ""
         *   }
         * }
         */

    }
}
