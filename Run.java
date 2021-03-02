package com.sale;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jxl.write.DateTime;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.junit.Test;

import ceshi.WriteExcel;

import com.entity.Postesalescreaterequest1;
import com.entity.orgSalesMemo;
import com.entity.salesItem;
import com.entity.salesTender;
import com.entity.salesTotal;
import com.entity.transHeader;
import com.sale.impl.CocoImpl;
import com.sale.impl.Createxml;

public class Run {
	CocoImpl cocoImpl = new CocoImpl();
	static List<String> asList = new ArrayList<String>();
	
	// startTime = Calendar.getInstance();// 锟斤拷锟斤拷
	//static Calendar endTime = Calendar.getInstance();//锟斤拷锟斤拷
  
	
	static SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
	

	
	
	/*
	 * 锟斤拷取sql锟斤拷锟�
	 */
	
	/*
	 * 锟斤拷取xml锟斤拷录锟侥硷拷
	 */
	public static void main(String[] args) {
		String []cmd =new String []{"tasklist"};
		Process proc;
		try {
			proc = Runtime.getRuntime().exec(cmd);
			BufferedReader in=new BufferedReader(new InputStreamReader(proc.getInputStream()));
			int count=0;
            for(String string_temp=in.readLine();string_temp!=null;string_temp=in.readLine()){
              System.out.println(string_temp);
            if(string_temp.indexOf("销售单导入.exe")>=0){
            	System.out.println("线程存在请稍后");
            	count++;
            }
            }
            if(count>1){
            	System.exit(0);
            }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String,String> map=new HashMap<String,String>();  
		Run co = new Run();
		CocoImpl im = new CocoImpl();
		co.reDelploy();
		Dao dao=new Dao();
		List<String> scode=new ArrayList<String>();
		List<String> scode1=new ArrayList<String>();
		List<String> scode11=new ArrayList<String>();
		Map<String,String> log=new HashMap<String,String>();  
		
		try {

			
			
			Connection conn = DbHelp.lianjie();
		//	System.out.println(postList.get("member_id"));
			//传递的JSON格式字符
	//		list=co.str(postList);
			
		//	int code=0;
			
			
		//	引入jar包  json-lib-2.4

				
				Interface it=new Interface();
			//	String a="method="+method+"&nowtime="+nowtime+"&page="+page+"&page_size="+page_size+"&system="+system;
		    //此部分前缀已经修改
			String a="{\"method\":\"oauth.trade.list\",\"nowtime\":\"2017-06-29 10:30:00\",\"page\":\"1\",\"page_size\":\"10\",\"system\":\"pos\"}";
			String ur=url+"&method=oauth.trade.list&nowtime=2017-06-29 10:30:00&page=1&page_size=200&system=pos";
				//System.out.println("a="+a);
			System.out.println(ur);
				 scode=it.tune(a, ur);
				 Map<String,Map<String,String>> map1=new HashMap<String,Map<String,String>>();
				 Map<String,Map<String,Map<String,String>>> map2=new HashMap<String,Map<String,Map<String,String>>>();
				 Map<String,Map<String,Map<String,String>>> map3=new HashMap<String,Map<String,Map<String,String>>>();
				 try {
					 map1= co.json(scode);
					 map2= co.json1(scode);
					map3= co.json2(scode);
				} catch (Exception e) {
					// TODO: handle exception
				}
				 
				
//				 System.out.println("map1.get(0).size()="+map1.get("0").size());
			//	 System.out.println(map2.get("0").size());
//				 for (int i = 0; i < map2.get("0").size()-1; i++) {
//					 String tax_amt=String.valueOf(i);
//					 for (int j = 0; j < map2.get(tax_amt).size(); j++) {
//						 String tax_amt1=String.valueOf(j);
//					}
//				}
				 List<String> aalist=new ArrayList<String>();
				 
				 
				 SimpleDateFormat df11 = new SimpleDateFormat("yyyyMMddHHmmssSSS");//设置日期格式
	     		//	System.out.println(df11.format(new Date()));// new Date()为获取当前系统时间
	     		//	 SimpleDateFormat df12 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
	     			 
				 String df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				 FileOutputStream fos = new FileOutputStream("logsale/"+df11.format(new Date())+"log.txt", true);
	    			PrintStream p = new PrintStream(fos);
	    			
	    			 SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
	    			 FileOutputStream fos22 = new FileOutputStream("txtsale/"+df2.format(new Date())+".txt", true);
		    			PrintStream p22 = new PrintStream(fos22);
	    			
		    			//读txt文本判断（用作判断是否有微商城的单子已经写入xml）
		   			File file33 = new File("txtsale/"+df2.format(new Date())+".txt");//Text文件
		   			BufferedReader br0 = new BufferedReader(new FileReader(file33));
		   			String ss0=null;
		   			List<String> listss=new ArrayList<String>();
		   			while((ss0 = br0.readLine())!=null){//使用readLine方法，一次读一行
		   				listss.add(ss0);
		   				
		   				}
		   			br0.close();
		    			
					for (int i = 0; i <map1.size() ; i++) {
						 String tax_amt=String.valueOf(i);
						 aalist.add("map1.get(tax_amt).get(trade_code)="+map1.get(tax_amt).get("trade_code"));
					}
//					System.out.println("map3.get(0).size()="+map3.get("0").size());
//					for (int i = 0; i < map3.get("0").size(); i++) {
//						 String tax_amt=String.valueOf(i);
//						System.out.println(map3.get("0").get(tax_amt).get("upc"));
//						System.out.println(map3.get("0").get(tax_amt).get("qty"));
//					}
					
// 				List<Postesalescreaterequest1> postList2 = co.getSql4(conn,res1.getItem_sid());
// 				List<Postesalescreaterequest1> postList3 = co.getSql5(conn,res1.getItem_sid());
 			//	System.out.println(res2.getUdf3()+"     ----"+res2.getUdf4());
 				//List<Postesalescreaterequest1> postList2 = co.getSql3(conn,"104795901901054");
// 				List<Postesalescreaterequest1> postList3 = co.getSql3(conn,res1.getItem_sid());
// 				List<Postesalescreaterequest1> postList4 = co.getSql4(conn,res1.getItem_sid());
// 				List<Postesalescreaterequest1> postList5 = co.getSql5(conn,res1.getItem_sid());
				// System.out.println(map1.get("0").get("trade_code")+"            "+map1.get("9").get("trade_code"));
			//生成invc_sid 随机数
			
			invc_no
			//invc_sid（19） cust_sid(19)要不重复随机 生成
			//invc_no从1开始   流水号生成
					File file3 = new File("invc_no.txt");//Text文件
					BufferedReader br = new BufferedReader(new FileReader(file3));
					String ss="";
					String ss1="";
					while((ss = br.readLine())!=null){//使用readLine方法，一次读一行
						System.out.println(ss);
						ss1=ss;
						}
					if("".equals(ss1.trim())){
						ss1="18";	
					}
					int invc_no=Integer.parseInt(ss1);
					br.close();
				 //写入
					
				
				 Document doc=DocumentHelper.createDocument();
				 Element root=doc.addElement("DOCUMENT");	
			try {
				
	            Element str3=root.addElement("INVOICES");
	            int stk=1;
	            //	String[] split = map.get(j).split(" ");
	           System.out.println("map1.size()="+map1.size());
	           
	          
	           
	            String XX="";
	            for (int i = 0; i < map1.size(); i++) {
	            	
	            	try {
	            		
	            		
		           	     p.println(stk);
		           	     stk++;
		            	 String tax_amt=String.valueOf(i);
		            	 //门店需要添加限制
		            	 if(!store.equals(map1.get(tax_amt).get("trade_store_code"))){//配置文件里的门店号
		            		//System.out.println(map1.get(tax_amt).get("trade_store_code"));
		            		 continue;//大致这么写   
		            	 }
		            	
		            	 String invc_no1=String.valueOf(invc_no);
		            	 System.out.println("---------------------跳出=================");
		            	 
		            		
		            	 
		            	 
		            	 
		            	// System.out.println("map1.get(tax_amt).get(ku_code)="+map1.get(tax_amt).get("sku_code"));
		       //     	 System.out.println("-");
		            	 
		            	 List<Postesalescreaterequest1> postList = dao.getSql(conn,map1.get(tax_amt).get("sku_code"));//map1.get(tax_amt).get("sku_code")
		            	 if(postList.size()==0){
		            			String sb= df1 +"    "+"trade_code="+map1.get(tax_amt).get("trade_code")+"     导入失败！";
		            			String sb1=df1 +"    "+"失败原因：该upc不存在或者错误！";
				    			p.println(sb);
				    			p.println(sb1);
				    			//doc.remove(root);
		            	 }else{
		            		 Postesalescreaterequest1 res1=postList.get(0);
			         		//	System.out.println("res1.getItem_sid="+res1.getItem_sid());
			 				
			 					List<Postesalescreaterequest1> postList1 = dao.getSql3(conn,res1.getItem_sid());
				 				Postesalescreaterequest1 res2=postList1.get(0);
					
			 				
			 			//	System.out.println("map1.get(tax_amt).get(fk_member_id)="+map1.get(tax_amt).get("fk_member_id"));
			 				List<Postesalescreaterequest1> postList2 = dao.getSql4(conn,map1.get(tax_amt).get("fk_member_id"));
			 			//	System.out.println("postList2.size()="+postList2.size());
			 				Postesalescreaterequest1 res3=postList2.get(0);
			 				//System.out.println("res3.getCust_sid()="+res3.getCust_sid());
			 				
			 				
			 	//			System.out.println("res3.getInfo1()="+res3.getInfo1());
			 				String tyty=res3.getInfo1();
			 				if(tyty==null){
			 					tyty="";
			 				}
			 				
			 				if(!tyty.equals("")){
			 					List<Postesalescreaterequest1> postList3 = dao.getSql5(conn,res3.getCust_sid());
			 					Postesalescreaterequest1 res4=null;
			 					String ttyy="";
			 					try {
			 						res4=postList3.get(0);
			 						ttyy=res4.getPhone1();
								} catch (Exception e) {
									
								}
			 					
			 				//	 SimpleDateFormat df = new SimpleDateFormat("1yyyyMMddHHmmssSSS2");//设置日期格式
			 	     		//	System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
			 	     			String x=map1.get(tax_amt).get("trade_code");
			 	     			System.out.println(x+"：单子");
			 	     			String check=String.valueOf(x.charAt(0));
			 	     			if("P".equals(check)||"p".equals(check)){
			 	     				x="2"+x.substring(1);
			 	     			}
			 	     			
			 	     			List<Postesalescreaterequest1> postList6 = dao.getSql6(conn,x);
			 	            	if(postList6.size()==0){
			 	            		String Y;
				 	            	if(map1.get(tax_amt).get("trade_store_code")==null){
				 	            		Y=map1.get(tax_amt).get("trade_store_code");
				 	            		Y="";
				 	            	}else{
				 	            		Y=map1.get(tax_amt).get("trade_store_code");
				 	            	}
				 	 //           	System.out.println("Y="+Y);
				 	            	List<Postesalescreaterequest1> postList8 = dao.getSql8(conn,Y);
				 	            	
				 	            //	System.out.println("Y="+Y);
				 	            	String store_no1="";
				 	            	if(postList8.size()==0){
				 	            		String sb= df1 +"    "+"trade_code="+map1.get(tax_amt).get("trade_code")+"     导入失败！";
				            			String sb1=df1 +"    "+"失败原因：该门店号错误！";
						    			p.println(sb);
						    			p.println(sb1);
						    		//	doc.remove(root);
				 	            	//	Postesalescreaterequest1 res8=postList8.get(0);
						    			continue;
				 	            	}else{
				 	            		Postesalescreaterequest1 res8=postList8.get(0);
				 	            		store_no1=res8.getStore_no();
				 	            	}
			 	            		Element str=str3.addElement("INVOICE");
				 	            	Element str1=str.addElement("CUSTOMER");
				 	            	Element str6=str.addElement("SHIPTO_CUSTOMER");
				 	            	Element str4=str.addElement("INVC_SUPPLS");
				 	            	Element str5=str.addElement("INVC_COMMENTS");
				 	            	Element str7=str.addElement("INVC_EXTRAS");
				 	            	Element str8=str.addElement("INVC_FEES");
				 	            	Element str9=str.addElement("INVC_TENDERS");
				 	            	Element str10=str.addElement("INVC_COUPONS");
				 	            	Element str11=str.addElement("INVC_ITEMS");
				 	          	
				 	            	Element str99=str9.addElement("INVC_TENDER");
				 	            	
				 	            	Element str55=str5.addElement("INVC_COMMENT");
				 	            	Element str88=str8.addElement("INVC_FEE");
				 	            	
				 	            	List<Postesalescreaterequest1> postList7 = dao.getSql7(conn,map1.get(tax_amt).get("trade_code"),"0");
				 	            	if(postList7.size()!=0){
				 	            		Postesalescreaterequest1 res7=postList7.get(0);
				 	            		invc_no1="";
				 	            		invc_no1=res7.getInvc_no();
				 	            		invc_no--;
				 	            		x=res7.getInvc_sid();
				 	            	}
				 	            	
				 	            	
				 	            	
				 	            	str.addAttribute("invc_sid", x);
				 	            	str.addAttribute("sbs_no", "1");//默认值1
				 	            	
				 	            	str.addAttribute("store_no", store_no1);
				 	            	
				 	            	
				 	          
				 	            	
				 	            	
				 	            	str.addAttribute("invc_no", invc_no1);
				 	            	
				 	            	str.addAttribute("invc_type", "0");//销售单是0，退货单是2
				 	            	str.addAttribute("hisec_type", "0");//默认是0
				 	            	str.addAttribute("status", "2");//默认是2
				 	            	str.addAttribute("proc_status", "0");//默认是0
				 	            	str.addAttribute("cust_sid", res2.getCust_sid());//会员信息
				 	            	str.addAttribute("addr_no", "1");//默认是1
				 	            	str.addAttribute("shipto_cust_sid", res2.getCust_sid());//同会员信息
				 	            	str.addAttribute("shipto_addr_no", "1");//默认是1
				 	            	str.addAttribute("station", "");
				 	            	str.addAttribute("workstation", "1");//默认是1
				 	            	str.addAttribute("orig_store_no",store_no1);//默认是取store_no
				 	            	str.addAttribute("orig_station", "");
				 	            	str.addAttribute("use_vat", "1");//默认是1
				 	            	str.addAttribute("vat_options", "0");//默认是0
				 	            	str.addAttribute("so_no", "");
				 	            	str.addAttribute("so_sid", "");
				 	            	str.addAttribute("cust_po_no", "");
				 	            	str.addAttribute("note", "");
				 	            	str.addAttribute("disc_perc", "");
				 	            	str.addAttribute("disc_amt", map1.get(tax_amt).get("trade_amount_dis"));
				 	            	str.addAttribute("disc_perc_spread", "");
				 	            	str.addAttribute("over_tax_perc", "");
				 	            	str.addAttribute("over_tax_perc2", "");
				 	            	str.addAttribute("tax_reb_perc", "");
				 	            	str.addAttribute("tax_reb_amt", "");
				 	            	str.addAttribute("rounding_offset", "");
				 	            	
				 	            //	System.out.println("map1.get(tax_amt).get(trade_time_created)="+map1.get(tax_amt).get("trade_time_created"));
				 		            	String[] trade_time_created1=map1.get(tax_amt).get("trade_time_created").split(" ");
				 		            	trade_time_created1[1]=trade_time_created1[1].substring(0, 8);
				 		            	String trade_time_created11=trade_time_created1[0]+"T"+trade_time_created1[1];
				 		      //      	System.out.println(trade_time_created11);
				 		            	
				 		            	String[] trade_time_shipping2=map1.get(tax_amt).get("trade_time_shipping").split(" ");
				 		            	trade_time_shipping2[1]=trade_time_shipping2[1].substring(0, 8);
				 		            	String trade_time_shipping11=trade_time_shipping2[0]+"T"+trade_time_shipping2[1];
				 		            	
				 	            	str.addAttribute("created_date", trade_time_shipping11);
				 	            //	System.out.println("map1.get(tax_amt).get(trade_time_pay)="+map1.get(tax_amt).get("trade_time_pay"));
			 		            	String[] trade_time_pay1=map1.get(tax_amt).get("trade_time_pay").split(" ");
			 		            	trade_time_pay1[1]=trade_time_pay1[1].substring(0, 8);
			 		            	String trade_time_pay11=trade_time_pay1[0]+"T"+trade_time_pay1[1]+"+08:00";
			 		            	//System.out.println(trade_time_pay11);
			 		            	String[] trade_time_shipping=map1.get(tax_amt).get("trade_time_created").split(" ");
			 		            	trade_time_shipping[1]=trade_time_shipping[1].substring(0, 8);
			 		            	String trade_time_shipping1=trade_time_shipping[0]+"T"+trade_time_shipping[1]+"+08:00";
			 		            	
			 		            	String[] post_date=df1.split(" ");
			 		            	post_date[1]=post_date[1].substring(0, 8);
			 		            	String post_date1=post_date[0]+"T"+post_date[1]+"+08:00";
			 		            	
				 	            	str.addAttribute("modified_date", trade_time_shipping1);
				 	            	str.addAttribute("post_date", post_date1);//写入当前时间
				 	            	
				 	            	str.addAttribute("tracking_no", map1.get(tax_amt).get("trade_code"));
				 	            	str.addAttribute("ref_invc_sid", "");
				 	            	str.addAttribute("audited", "1");//默认是1
				 	            	str.addAttribute("cms_post_date", trade_time_pay11);
				 	            	str.addAttribute("ws_seq_no", "");
				 	            	str.addAttribute("cust_fld", "");
				 	            	str.addAttribute("held", "0");//默认是0
				 	            	str.addAttribute("drawer_no", "1");//默认是1
				 	            	str.addAttribute("controller", "8888");//默认是8888
				 	            	str.addAttribute("orig_controller", "8888");//默认是8888
				 	            	str.addAttribute("elapsed_time", "");
				 	            	str.addAttribute("till_name", "");
				 	            	str.addAttribute("activity_perc", "");
				 	            	str.addAttribute("activity_perc2", "");
				 	            	str.addAttribute("activity_perc3", "");
				 	            	str.addAttribute("activity_perc4", "");
				 	            	str.addAttribute("activity_perc5", "");
				 	            	str.addAttribute("eft_invc_no", "");
				 	            	str.addAttribute("detax", "0");//默认是0
				 	            	str.addAttribute("doc_ref_no", "");
				 	            	str.addAttribute("fiscal_doc_id", "");
				 	            	str.addAttribute("subloc_code", "");
				 	            	str.addAttribute("subloc_id", "");
				 	            	str.addAttribute("ship_perc", "");
				 	            	str.addAttribute("trans_disc_amt", "");
				 	            	str.addAttribute("empl_sbs_no", "1");//默认是1

				 	            	
				 	            	String trade_emp_code=map1.get(tax_amt).get("trade_emp_code");
				 	            	
				 	            	if(trade_emp_code==null||trade_emp_code.trim()==""){
				 	            		trade_emp_code="10002570";
				 	            	}
				 	            	str.addAttribute("empl_name", trade_emp_code);//默认值10000001
				 	            	str.addAttribute("tax_area_name", "CH_VAT");
				 	            	str.addAttribute("tax_area2_name", "");
				 	            	str.addAttribute("ref_invc_no", "");
				 	            	str.addAttribute("ref_invc_created_date", "");
				 	            	str.addAttribute("createdby_sbs_no", "1");//默认值1
				 	            	str.addAttribute("createdby_empl_name", trade_emp_code);//默认值10000001
				 	            	str.addAttribute("modifiedby_sbs_no", "1");//默认值1
				 	            	str.addAttribute("modifiedby_empl_name", trade_emp_code);//默认值10000001
				 	            	str.addAttribute("clerk_sbs_no", "1");//默认值1
				 	            	str.addAttribute("clerk_name", trade_emp_code);//默认值10000001
				 	            	str.addAttribute("clerk_sbs_no2", "");
				 	            	str.addAttribute("clerk_name2", "");
				 	            	str.addAttribute("clerk_sbs_no3", "");
				 	            	str.addAttribute("clerk_name3", "");
				 	            	str.addAttribute("clerk_sbs_no4", "");
				 	            	str.addAttribute("clerk_name4", "");
				 	            	str.addAttribute("clerk_sbs_no5", "");
				 	            	str.addAttribute("clerk_name5", "");
				 	            	str.addAttribute("disbur_reason_type", "");
				 	            	str.addAttribute("disbur_reason_name", "");
				 	            	str.addAttribute("doc_reason_code", "");
				 	            
				 	            	str1.addAttribute("cust_sid", res3.getCust_sid());
				 	            	str1.addAttribute("cust_id", res3.getCust_id());
				 	            	str1.addAttribute("store_no", "1");
				 	            	str1.addAttribute("station", "");
				 	            	str1.addAttribute("first_name", res3.getFirst_name());
				 	            	str1.addAttribute("last_name", res3.getLast_name());
				 	            	str1.addAttribute("price_lvl", "");
				 	            	str1.addAttribute("detax", res3.getDetax());
				 	            	str1.addAttribute("info1", tyty);//res3.getInfo1()
				 	            	str1.addAttribute("info2", map1.get(tax_amt).get("fk_member_id"));
				 	            	
				 	            	str1.addAttribute("modified_date", trade_time_shipping1);
				 	            	str1.addAttribute("sbs_no", "1");
				 	            	str1.addAttribute("cms", "1");
				 	            	str1.addAttribute("company_name", "");
				 	            	str1.addAttribute("title", "");
				 	            	str1.addAttribute("tax_area_name", "");
				 	            	str1.addAttribute("shipping", "0");
				 	            	str1.addAttribute("address1", "");
				 	            	str1.addAttribute("address2", "");
				 	            	str1.addAttribute("address3", "");
				 	            	str1.addAttribute("address4", "");
				 	            	str1.addAttribute("address5", "");
				 	            	str1.addAttribute("address6", "43");
				 	            	str1.addAttribute("zip", "");
				 	            	str1.addAttribute("phone1", ttyy);
				 	            	str1.addAttribute("phone2", "");
				 	            	str1.addAttribute("country_name", "");
				 	            	System.out.println("res3.getAlternate_id1()="+res3.getAlternate_id1());
				 	            	str1.addAttribute("alternate_id1", res3.getAlternate_id1());
				 	            	str1.addAttribute("alternate_id2", res3.getAlternate_id2());
				 	            	
				 	            	
				 	            	
				 	            	
				 	            	str55.addAttribute("comment_no", "1");// map1.get(tax_amt).get("trade_note_buyer")
				 	            	str55.addAttribute("comments", map1.get(tax_amt).get("trade_distrtype_name"));
				 	            	
				 	            	
				 	            	
				 	            	
				 	            	str6.addAttribute("cust_sid", res3.getCust_sid());
				 	            	str6.addAttribute("cust_id", res3.getCust_id());
				 	            	str6.addAttribute("store_no", "1");
				 	            	str6.addAttribute("station", "");
				 	            	str6.addAttribute("first_name", res3.getFirst_name());
				 	            	str6.addAttribute("last_name", res3.getLast_name());
				 	            	str6.addAttribute("price_lvl", "");
				 	            	str6.addAttribute("detax", res3.getDetax());
				 	            	str6.addAttribute("info1", tyty);
				 	            	str6.addAttribute("info2", map1.get(tax_amt).get("fk_member_id"));
				 	            	
				 	            	str6.addAttribute("modified_date", trade_time_pay11);
				 	            	str6.addAttribute("sbs_no", "1");
				 	            	str6.addAttribute("cms", "1");
				 	            	str6.addAttribute("company_name", "");
				 	            	str6.addAttribute("title", "");
				 	            	str6.addAttribute("tax_area_name", "");
				 	            	str6.addAttribute("shipping", "0");
				 	            	str6.addAttribute("address1", "");
				 	            	str6.addAttribute("address2", "");
				 	            	str6.addAttribute("address3", "");
				 	            	str6.addAttribute("address4", "");
				 	            	str6.addAttribute("address5", "");
				 	            	str6.addAttribute("address6", "43");
				 	            	str6.addAttribute("zip", "");
				 	            	str6.addAttribute("phone1", ttyy);
				 	            	str6.addAttribute("phone2", "");
				 	            	str6.addAttribute("country_name", "");
				 	            	str6.addAttribute("alternate_id1", res3.getAlternate_id1());
				 	            	str6.addAttribute("alternate_id2", res3.getAlternate_id2());
				 	            	
				 	            	str55.addAttribute("comment_no","1");//map1.get(tax_amt).get("trade_note_buyer")
				 	            	str55.addAttribute("comments", map1.get(tax_amt).get("trade_distrtype_name"));
				 			            	//	str.addAttribute();
				 	            	
				 	            	str99.addAttribute("tender_type", "2");
				 	            	str99.addAttribute("tender_no", "1");
				 	            	str99.addAttribute("taken", map1.get(tax_amt).get("trade_amount_payable"));
				 	            	str99.addAttribute("given", "0");
				 	            	str99.addAttribute("amt", map1.get(tax_amt).get("trade_amount_payable"));
				 	            	str99.addAttribute("doc_no", "");
				 	            	str99.addAttribute("auth", "");
				 	            	str99.addAttribute("reference", "");
				 	            	str99.addAttribute("chk_company", "");
				 	            	str99.addAttribute("chk_first_name", "");
				 	            	str99.addAttribute("chk_last_name", "");
				 	            	str99.addAttribute("chk_work_phone", "");
				 	            	str99.addAttribute("chk_home_phone", "");
				 	            	str99.addAttribute("chk_state_code", "");
				 	            	str99.addAttribute("chk_dl", "");
				 	            	str99.addAttribute("chk_dl_exp_date", "");
				 	            	str99.addAttribute("chk_dob_date", "");
				 	            	str99.addAttribute("crd_exp_month", "");
				 	            	str99.addAttribute("crd_exp_year", "");
				 	            	str99.addAttribute("crd_normal_sale", "");
				 	            	str99.addAttribute("crd_contr_no", "");
				 	            	str99.addAttribute("crd_present", "");
				 	            	str99.addAttribute("crd_zip", "");
				 	            	str99.addAttribute("crd_proc_fee", "");
				 	            	str99.addAttribute("gft_crd_trace_no", "");
				 	            	str99.addAttribute("gft_crd_int_ref_no", "");
				 	            	str99.addAttribute("gft_crd_balance", "");
				 	            	str99.addAttribute("charge_net_days", "");
				 	            	str99.addAttribute("charge_disc_days", "");
				 	            	str99.addAttribute("charge_disc_perc", "");
				 	            	str99.addAttribute("pmt_date", "");
				 	            	str99.addAttribute("pmt_remark", "");
				 	            	str99.addAttribute("matched", "");
				 	            	str99.addAttribute("manual_name", "");
				 	            	str99.addAttribute("manual_remark", "");
				 	            	str99.addAttribute("transaction_id", "");
				 	            	str99.addAttribute("avs_code", "");
				 	            	str99.addAttribute("chk_type", "");
				 	            	str99.addAttribute("cashback_amt", "");
				 	            	str99.addAttribute("l2_result_code", "");
				 	            	str99.addAttribute("signature_map", "");
				 	            	str99.addAttribute("orig_crd_name", "");
				 	            	str99.addAttribute("tender_state", "");
				 	            	str99.addAttribute("failure_msg", "");
				 	            	str99.addAttribute("proc_date", "");
				 	            	str99.addAttribute("orig_currency_name", "");
				 	            	str99.addAttribute("eftdata0", "");
				 	            	str99.addAttribute("eftdata1", "");
				 	            	str99.addAttribute("eftdata2", "");
				 	            	str99.addAttribute("eftdata3", "");
				 	            	str99.addAttribute("eftdata4", "");
				 	            	str99.addAttribute("eftdata5", "");
				 	            	str99.addAttribute("eftdata6", "");
				 	            	str99.addAttribute("eftdata7", "");
				 	            	str99.addAttribute("eftdata8", "");
				 	            	str99.addAttribute("eftdata9", "");
				 	            	str99.addAttribute("cardholder_name", "");
				 	            	str99.addAttribute("give_rate", "");
				 	            	str99.addAttribute("take_rate", "");
				 	            	str99.addAttribute("base_taken", "");
				 	            	str99.addAttribute("base_given", "");
				 	            	str99.addAttribute("cent_txn_id", "");
				 	            	str99.addAttribute("balance_remaining", "");
				 	            	str99.addAttribute("cent_commit_txn", "");
				 	            	str99.addAttribute("emv_aid", "");
				 	            	str99.addAttribute("emv_applabel", "");
				 	            	str99.addAttribute("emv_card_exp_date", "");
				 	            	str99.addAttribute("emv_cyrpto_type", "");
				 	            	str99.addAttribute("emv_cryptogram", "");
				 	            	str99.addAttribute("emv_pin_statement", "");
				 	            	str99.addAttribute("cayan_sf_id", "");
				 	            	str99.addAttribute("crd_name", "");
				 	            	str99.addAttribute("currency_name", "");
				 	            	
//				 	            	orig_tax_amt=map1.get("orig_price")/1.17*0.17
//				 	            	  tax_amt=price/1.17*0.17
				 	        //    	System.out.println("map2.get(tax_amt).get(tax_amt).size()="+map2.get(tax_amt).size());
				 	            	
				 	            	 int invc_no2=1;	 
				 	 //           	 System.out.println("map2.get(tax_amt).size()="+map2.get(tax_amt).size());
				 	            	for (int j = 0; j < map2.get(tax_amt).size(); j++) {
				 	     //       		System.out.println("j="+j);
				 	            		String invc_no22=String.valueOf(invc_no2);
				 	            		String tax_amt11=String.valueOf(j);
				 	            	//	System.out.println("map2.get(tax_amt).get(tax_amt11).get(sku_code)="+map2.get(tax_amt).get(tax_amt11).get("sku_code"));
				 	     //       		System.out.println("=====================================11");
				 	            		List<Postesalescreaterequest1> postList00 = dao.getSql(conn,map2.get(tax_amt).get(tax_amt11).get("sku_code"));//map1.get(tax_amt).get("sku_code")
				 	            		 if(postList00.size()==0){
				 	            			String sb= df1 +"    "+"trade_code="+map1.get(tax_amt).get("trade_code")+"     导入失败！";
				 	            			String sb1=df1 +"    "+"失败原因：该upc不存在或者错误！";
				 			    			p.println(sb);
				 			    			p.println(sb1);
				 	            	 }
				 	            		 Postesalescreaterequest1 res11=postList00.get(0);
				 	            		List<Postesalescreaterequest1> postList11 = dao.getSql3(conn,res11.getItem_sid());
						 				Postesalescreaterequest1 res22=postList11.get(0);
				 	            		 Element str111=str11.addElement("INVC_ITEM");
					 	            	Element str1111=str111.addElement("INVN_BASE_ITEM");
					 	            	
					 	            	double ac;
					 	            	double ac1;
					 	            	//System.out.println("item_price="+map1.get("item_price"));
					 	            	//System.out.println(map1.get("item_price"));
					 	            	//System.out.println("============================================");
					 	           // 	System.out.println("map2.get(tax_amt).get(tax_amt11).get(item_price)="+map2.get(tax_amt).get(tax_amt11).get("item_price"));
					 	            		ac=Double.parseDouble(map2.get(tax_amt).get(tax_amt11).get("item_price"));
					 	            		ac=ac/1.17;
					 	            		ac=ac*0.17;
					 	            			//System.out.println(ac);
					 	            			String orig_tax_amt1=String.valueOf(ac);
					 	            			String at=map2.get(tax_amt).get(tax_amt11).get("item_amount_subtotal");
					 	            			double at1=Double.parseDouble(at);
					 	            			String at2=map2.get(tax_amt).get(tax_amt11).get("item_num");
					 	            			double at22=Double.parseDouble(at2);
					 	            			at1=at1/at22;
					 	            			at=String.valueOf(String.format("%.2f", at1));
					 	            			ac1=Double.parseDouble(at);;
					 	            			ac1=ac1/1.17;
					 	            			ac1=ac1*0.17;
					 	            			String tax_amt1=String.valueOf(ac1);
//					 	            	i=Integer.parseInt(s);
					 	            	str111.addAttribute("item_pos", invc_no22);//map1.get(tax_amt).get("item_pos")
					 	            	invc_no2++;
					 	            	
					 	            	str111.addAttribute("item_sid", res11.getItem_sid());
					 	            	str111.addAttribute("qty", map2.get(tax_amt).get(tax_amt11).get("item_num"));
					 	            	String X;
					 	            	if(map2.get(tax_amt).get(tax_amt11).get("item_price")==null){
					 	            		X=map2.get(tax_amt).get(tax_amt11).get("item_price");
					 	            		X="";
					 	            	}else{
					 	            		X=map2.get(tax_amt).get(tax_amt11).get("item_price");
					 	            	}
					 	            	str111.addAttribute("orig_price", X);
					 	            	str111.addAttribute("orig_tax_amt", orig_tax_amt1);
					 	            	str111.addAttribute("price", at);
					 	            	str111.addAttribute("tax_code", "0");
					 	            	str111.addAttribute("tax_perc", "17");
					 	            	str111.addAttribute("tax_amt", tax_amt1);
					 	            	str111.addAttribute("tax_code2", "");
					 	            	str111.addAttribute("tax_perc2", "");
					 	            	str111.addAttribute("tax_amt2", "");
					 	            	str111.addAttribute("cost", "0");
					 	            	str111.addAttribute("price_lvl", "1");
					 	            	str111.addAttribute("spif", "0");
					 	            	str111.addAttribute("sched_no", "");
					 	            	str111.addAttribute("comm_code", "");
					 	            	str111.addAttribute("comm_amt", "");
					 	            	str111.addAttribute("cust_fld", "");
					 	            	str111.addAttribute("scan_upc", "");
					 	            	str111.addAttribute("serial_no", "");
					 	            	str111.addAttribute("lot_number", "");
					 	            	str111.addAttribute("kit_flag", "0");
					 	            	str111.addAttribute("pkg_item_sid", "");
					 	            	str111.addAttribute("pkg_seq_no", "");
					 	            	str111.addAttribute("orig_cmpnt_item_sid", "");
					 	            	str111.addAttribute("detax", "0");
					 	            	str111.addAttribute("usr_disc_perc", "0");
					 	            	str111.addAttribute("udf_value1", "");
					 	            	str111.addAttribute("udf_value2", "");
					 	            	str111.addAttribute("udf_value3", "");
					 	            	str111.addAttribute("udf_value4", "");
					 	            	str111.addAttribute("activity_perc", "100");
					 	            	str111.addAttribute("activity_perc2", "0");
					 	            	str111.addAttribute("activity_perc3", "0");
					 	            	str111.addAttribute("activity_perc4", "0");
					 	            	str111.addAttribute("activity_perc5", "0");
					 	            	str111.addAttribute("comm_amt2", "0");
					 	            	str111.addAttribute("comm_amt3", "0");
					 	            	str111.addAttribute("comm_amt4", "0");
					 	            	str111.addAttribute("comm_amt5", "0");
					 	            	str111.addAttribute("so_sid", "");
					 	            	str111.addAttribute("so_orig_item_pos", "");
					 	            	str111.addAttribute("item_origin", "");
					 	            	str111.addAttribute("pkg_no", "");
					 	            	str111.addAttribute("shipto_cust_sid", "");
					 	            	str111.addAttribute("shipto_addr_no", "");
					 	            	str111.addAttribute("orig_cost", "");
					 	            	str111.addAttribute("item_note1", "");
					 	            	str111.addAttribute("item_note2", "");
					 	            	str111.addAttribute("item_note3", "");
					 	            	str111.addAttribute("item_note4", "");
					 	            	str111.addAttribute("item_note5", "");
					 	            	str111.addAttribute("item_note6", "");
					 	            	str111.addAttribute("item_note7", "");
					 	            	str111.addAttribute("item_note8", "");
					 	            	str111.addAttribute("item_note9", map2.get(tax_amt).get(tax_amt11).get("itemnote9"));
					 	            	str111.addAttribute("item_note10", map2.get(tax_amt).get(tax_amt11).get("crm_remark"));
					 	            	str111.addAttribute("promo_flag", "0");
					 	            	str111.addAttribute("gift_activation_code", "");
					 	            	str111.addAttribute("gift_transaction_id", "");
					 	            	str111.addAttribute("returned_qty", "");
					 	            	str111.addAttribute("ref_item_pos", "");
					 	            	str111.addAttribute("ref_invc_sid", "");
					 	            	str111.addAttribute("gift_add_value", "0");
					 	            	str111.addAttribute("ref_invc_no", "");
					 	            	str111.addAttribute("alt_upc", "");
					 	            	str111.addAttribute("alt_alu", "");
					 	            	str111.addAttribute("alt_cost", "");
					 	            	str111.addAttribute("alt_vend_code", "");
					 	            	str111.addAttribute("orig_prc_bdt", "");
					 	            	str111.addAttribute("prc_bdt", "");
					 	            	str111.addAttribute("gift_eftdata0", "");
					 	            	str111.addAttribute("gift_eftdata1", "");
					 	            	str111.addAttribute("gift_eftdata2", "");
					 	            	str111.addAttribute("gift_eftdata3", "");
					 	            	str111.addAttribute("gift_eftdata4", "");
					 	            	str111.addAttribute("gift_eftdata5", "");
					 	            	str111.addAttribute("gift_eftdata6", "");
					 	            	str111.addAttribute("gift_eftdata7", "");
					 	            	str111.addAttribute("gift_eftdata8", "");
					 	            	str111.addAttribute("gift_eftdata9", "");
					 	            	str111.addAttribute("subloc_code", "");
					 	            	str111.addAttribute("subloc_id", "");
					 	            	str111.addAttribute("tender_state", "0");
					 	            	str111.addAttribute("failure_msg", "");
					 	            	str111.addAttribute("proc_date", "");
					 	            	str111.addAttribute("cent_commit_txn", "0");
					 	            	str111.addAttribute("price_flag", "0");
					 	            	str111.addAttribute("force_orig_tax", "");
					 	            	str111.addAttribute("sn_qty", "");
					 	            	str111.addAttribute("sn_active", "");
					 	            	str111.addAttribute("sn_received", "");
					 	            	str111.addAttribute("sn_sold", "");
					 	            	str111.addAttribute("sn_transferred", "");
					 	            	str111.addAttribute("sn_so_reserved", "");
					 	            	str111.addAttribute("sn_returned", "");
					 	            	str111.addAttribute("sn_returned_to_vnd", "");
					 	            	str111.addAttribute("sn_adjusted", "");
					 	            	str111.addAttribute("tax_perc_lock", "");
					 	            	str111.addAttribute("ref_store_no", "");
					 	            	str111.addAttribute("tax_area2_name", "");
					 	            	str111.addAttribute("empl_sbs_no", "1");
					 	            	str111.addAttribute("empl_name", trade_emp_code);
					 	            	str111.addAttribute("disc_reason_name", "");
					 	            	str111.addAttribute("empl_sbs_no2", "");
					 	            	str111.addAttribute("empl_name2", "");
					 	            	str111.addAttribute("empl_sbs_no3", "");
					 	            	str111.addAttribute("empl_name3", "");
					 	            	str111.addAttribute("empl_sbs_no4", "");
					 	            	str111.addAttribute("empl_name4", "");
					 	            	str111.addAttribute("empl_sbs_no5", "");
					 	            	str111.addAttribute("empl_name5", "");
					 	            	str111.addAttribute("ship_method", "");
					 	            	str111.addAttribute("item_reason_type", "");
					 	            	str111.addAttribute("item_reason_name", "");
					 	            	
//					 	            			Postesalescreaterequest1 res3=postList.get(0);
//					 	            			Postesalescreaterequest1 res2=postList1.get(0);
//					 	            			
//					 	            			Postesalescreaterequest1 res4=postList2.get(0);
					 	            	if(res2.getUdf3().equals("")){
					 	            		str1111.addAttribute("item_sid",res11.getItem_sid());//
					 	            	//	System.out.println("-------------upc----------------"+map2.get(tax_amt).get(tax_amt11).get("sku_code"));
					 		            	str1111.addAttribute("upc", map2.get(tax_amt).get(tax_amt11).get("sku_code"));//map1.get(tax_amt).get("sku_code")
					 		            	str1111.addAttribute("alu", "");
					 		            	str1111.addAttribute("style_sid", res11.getStyle_sid());
					 		            	str1111.addAttribute("dcs_code", res11.getDcs_code());
					 		            	str1111.addAttribute("vend_code", res11.getVend_code());
					 		            	str1111.addAttribute("scale_no", res11.getScale_no());
					 		            	str1111.addAttribute("description1", res11.getDescription1());
					 		            	str1111.addAttribute("description2", res11.getDescription2());
					 		            	str1111.addAttribute("description3", res11.getDescription3());
					 		            	str1111.addAttribute("description4", res11.getDescription4());
					 		            	str1111.addAttribute("attr", res11.getAttr());
					 		            	str1111.addAttribute("siz", res11.getSiz());
					 		            	str1111.addAttribute("use_qty_decimals", "");
					 		            	str1111.addAttribute("tax_code", "");
					 		            	str1111.addAttribute("flag", "");
					 		            	str1111.addAttribute("ext_flag", "");
					 		            	str1111.addAttribute("item_no", "");
					 		            	str1111.addAttribute("udf3_value", res2.getUdf3());
					 		            	str1111.addAttribute("udf4_value", res2.getUdf3());
					 		            	str1111.addAttribute("udf5_value", res2.getUdf3());
					 		            	str1111.addAttribute("udf6_value", res2.getUdf3());
					 		            	str1111.addAttribute("aux1_value", res2.getUdf3());
					 		            	str1111.addAttribute("aux2_value", res2.getUdf3());
					 		            	str1111.addAttribute("aux3_value", res2.getUdf3());
					 		            	str1111.addAttribute("aux4_value", res2.getUdf3());
					 		            	str1111.addAttribute("aux5_value", res2.getUdf3());
					 		            	str1111.addAttribute("aux6_value", res2.getUdf3());
					 		            	str1111.addAttribute("aux7_value", res2.getUdf3());
					 		            	str1111.addAttribute("aux8_value", res2.getUdf3());
					 	            		
					 	            	}else{
					 	            		str1111.addAttribute("item_sid", res11.getItem_sid());
					 		            	str1111.addAttribute("upc", map2.get(tax_amt).get(tax_amt11).get("sku_code"));
					 		            	str1111.addAttribute("alu", "");
					 		            	str1111.addAttribute("style_sid", res11.getStyle_sid());
					 		            	str1111.addAttribute("dcs_code", res11.getDcs_code());
					 		            	str1111.addAttribute("vend_code", res11.getVend_code());
					 		            	str1111.addAttribute("scale_no", res11.getScale_no());
					 		            	str1111.addAttribute("description1", res11.getDescription1());
					 		            	str1111.addAttribute("description2", res11.getDescription2());
					 		            	str1111.addAttribute("description3", res11.getDescription3());
					 		            	str1111.addAttribute("description4", res11.getDescription4());
					 		            	str1111.addAttribute("attr", res11.getAttr());
					 		            	str1111.addAttribute("siz", res11.getSiz());
					 		            	str1111.addAttribute("use_qty_decimals", "");
					 		            	str1111.addAttribute("tax_code", "");
					 		            	str1111.addAttribute("flag", "");
					 		            	str1111.addAttribute("ext_flag", "");
					 		            	str1111.addAttribute("item_no", "");
					 		            	str1111.addAttribute("udf3_value", res22.getUdf3());
					 		            	str1111.addAttribute("udf4_value", res22.getUdf4());
					 		            	str1111.addAttribute("udf5_value", res22.getUdf5());
					 		            	str1111.addAttribute("udf6_value", res22.getUdf6());
					 		            	str1111.addAttribute("aux1_value", res22.getUdf7());
					 		            	str1111.addAttribute("aux2_value", res22.getUdf8());
					 		            	str1111.addAttribute("aux3_value", res22.getUdf9());
					 		            	str1111.addAttribute("aux4_value", res22.getUdf10());
					 		            	str1111.addAttribute("aux5_value", res22.getUdf11());
					 		            	str1111.addAttribute("aux6_value", res22.getUdf12());
					 		            	str1111.addAttribute("aux7_value", res22.getUdf13());
					 		            	str1111.addAttribute("aux8_value", res22.getUdf14());
					 	            		
					 	            	}
					 	            	
									}
				 	            	
				 	            	//--------------------------------------------------------------------------------------------------------------------

				 	            	//--------------------------------------------------------------------------------------------------------------------	
				 	            	
				 	    //        	System.out.println("map1.get(tax_amt).get(trade_code)="+map1.get(tax_amt).get("trade_code"));
				 	    //        	System.out.println(map3.get(tax_amt).get("0")+"-----        ==");
				 	            	if(map3.get(tax_amt).get("0")!=null){
				 	            			 int invc_no11=invc_no2;	 
				 	    //        			 System.out.println("map3.get(tax_amt).size()="+map3.get(tax_amt).size());
							 	            	for (int j = 0; j < map3.get(tax_amt).size(); j++) {
							 	            		String invc_no111=String.valueOf(invc_no11);
							 	            		String tax_amt11=String.valueOf(j);
							 	            	//	System.out.println("map2.get(tax_amt).get(tax_amt11).get(sku_code)="+map2.get(tax_amt).get(tax_amt11).get("sku_code"));
							 	  //          		System.out.println(map3.get(tax_amt).get(tax_amt11).get("upc"));
							 	  //          		System.out.println("map3.get(tax_amt).get(tax_amt11).get(upc)="+map3.get(tax_amt).get(tax_amt11).get("upc"));
							 	            		//得修改回来
							 	            		//map3.get(tax_amt).get(tax_amt11).get("upc")
							 	            		List<Postesalescreaterequest1> postList00 = dao.getSql(conn,map3.get(tax_amt).get(tax_amt11).get("upc"));//map1.get(tax_amt).get("sku_code")
							 	            		 Postesalescreaterequest1 res11=postList00.get(0);
							 	            		 
							 	            		List<Postesalescreaterequest1> postList11 = dao.getSql3(conn,res11.getItem_sid());
									 				Postesalescreaterequest1 res22=postList11.get(0);
							 	            		 Element str111=str11.addElement("INVC_ITEM");
								 	            	Element str1111=str111.addElement("INVN_BASE_ITEM");

								 	            //	double ac;
								 	            	double ac1;
								 	            	//System.out.println("item_price="+map1.get("item_price"));
								 	            	//System.out.println(map1.get("item_price"));
//								 	            	System.out.println("============================================");
//								 	            	System.out.println("map2.get(tax_amt).get(tax_amt11).get(item_price)="+map2.get(tax_amt).get(tax_amt11).get("item_price"));
//								 	            		ac=Double.parseDouble(map2.get(tax_amt).get(tax_amt11).get("item_price"));
//								 	            		ac=ac/1.17;
//								 	            		ac=ac*0.17;
//								 	            			//System.out.println(ac);
//								 	            			String orig_tax_amt1=String.valueOf(ac);
								 	            	
								 	            			ac1=Double.parseDouble(map3.get(tax_amt).get(tax_amt11).get("price"));;
								 	            			ac1=ac1/1.17;
								 	            			ac1=ac1*0.17;
								 	            			String tax_amt1=String.valueOf(ac1);
//								 	            	i=Integer.parseInt(s);
								 	            	str111.addAttribute("item_pos", invc_no111);//map1.get(tax_amt).get("item_pos")
								 	            	invc_no11++;
								 	            	str111.addAttribute("item_sid", res11.getItem_sid());
								 	            	str111.addAttribute("qty", "0");
//								 	            	String X;
//								 	            	if(map2.get(tax_amt).get(tax_amt11).get("item_price")==null){
//								 	            		X=map2.get(tax_amt).get(tax_amt11).get("item_price");
//								 	            		X="";
//								 	            	}else{
//								 	            		X=map2.get(tax_amt).get(tax_amt11).get("item_price");
//								 	            	}
								 	            	str111.addAttribute("orig_price", "0");
								 	            	str111.addAttribute("orig_tax_amt", "0");
//								 	            	double tt11=Double.valueOf(map3.get(tax_amt).get(tax_amt11).get("price"));
//								 	            	tt11=Math.abs(tt11);
//								 	            	String sr1=String.valueOf(String.format("%.2f", tt11));
								 	            	String price1=map3.get(tax_amt).get(tax_amt11).get("price");
								 	            	double price11=Double.parseDouble(price1);
								 	            	price11=price11*(-1);
								 	            	price1=String.valueOf(price11);
								 	            	str111.addAttribute("price", price1);
								 	            	str111.addAttribute("tax_code", "0");
								 	            	str111.addAttribute("tax_perc", "17");
								 	            	str111.addAttribute("tax_amt", tax_amt1);
								 	            	str111.addAttribute("tax_code2", "");
								 	            	str111.addAttribute("tax_perc2", "");
								 	            	str111.addAttribute("tax_amt2", "");
								 	            	str111.addAttribute("cost", "0");
								 	            	str111.addAttribute("price_lvl", "1");
								 	            	str111.addAttribute("spif", "0");
								 	            	str111.addAttribute("sched_no", "");
								 	            	str111.addAttribute("comm_code", "");
								 	            	str111.addAttribute("comm_amt", "");
								 	            	String qty1=map3.get(tax_amt).get(tax_amt11).get("qty");
								 	            	int qty11=Integer.parseInt(qty1);
								 	            	qty11=qty11*(-1);
								 	            	qty1=String.valueOf(qty11);
								 	            	str111.addAttribute("cust_fld", qty1);
								 	            	str111.addAttribute("scan_upc", "");
								 	            	str111.addAttribute("serial_no", "");
								 	            	str111.addAttribute("lot_number", "");
								 	            	str111.addAttribute("kit_flag", "0");
								 	            	str111.addAttribute("pkg_item_sid", "");
								 	            	str111.addAttribute("pkg_seq_no", "");
								 	            	str111.addAttribute("orig_cmpnt_item_sid", "");
								 	            	str111.addAttribute("detax", "0");
								 	            	str111.addAttribute("usr_disc_perc", "0");
								 	            	str111.addAttribute("udf_value1", "");
								 	            	str111.addAttribute("udf_value2", "");
								 	            	str111.addAttribute("udf_value3", "");
								 	            	str111.addAttribute("udf_value4", "");
								 	            	str111.addAttribute("activity_perc", "100");
								 	            	str111.addAttribute("activity_perc2", "0");
								 	            	str111.addAttribute("activity_perc3", "0");
								 	            	str111.addAttribute("activity_perc4", "0");
								 	            	str111.addAttribute("activity_perc5", "0");
								 	            	str111.addAttribute("comm_amt2", "0");
								 	            	str111.addAttribute("comm_amt3", "0");
								 	            	str111.addAttribute("comm_amt4", "0");
								 	            	str111.addAttribute("comm_amt5", "0");
								 	            	str111.addAttribute("so_sid", "");
								 	            	str111.addAttribute("so_orig_item_pos", "");
								 	            	str111.addAttribute("item_origin", "");
								 	            	str111.addAttribute("pkg_no", "");
								 	            	str111.addAttribute("shipto_cust_sid", "");
								 	            	str111.addAttribute("shipto_addr_no", "");
								 	            	str111.addAttribute("orig_cost", "");
								 	            	str111.addAttribute("item_note1", "");
								 	            	str111.addAttribute("item_note2", "");
								 	            	str111.addAttribute("item_note3", "");
								 	            	str111.addAttribute("item_note4", "");
								 	            	str111.addAttribute("item_note5", "");
								 	            	str111.addAttribute("item_note6", "");
								 	            	str111.addAttribute("item_note7", "");
								 	            	str111.addAttribute("item_note8", "");
								 	            	str111.addAttribute("item_note9", map3.get(tax_amt).get(tax_amt11).get("itemnote9"));
								 	            	str111.addAttribute("item_note10", map3.get(tax_amt).get(tax_amt11).get("crm_remark"));
								 	            	str111.addAttribute("promo_flag", "0");
								 	            	str111.addAttribute("gift_activation_code", "");
								 	            	str111.addAttribute("gift_transaction_id", "");
								 	            	str111.addAttribute("returned_qty", "");
								 	            	str111.addAttribute("ref_item_pos", "");
								 	            	str111.addAttribute("ref_invc_sid", "");
								 	            	str111.addAttribute("gift_add_value", "0");
								 	            	str111.addAttribute("ref_invc_no", "");
								 	            	str111.addAttribute("alt_upc", "");
								 	            	str111.addAttribute("alt_alu", "");
								 	            	str111.addAttribute("alt_cost", "");
								 	            	str111.addAttribute("alt_vend_code", "");
								 	            	str111.addAttribute("orig_prc_bdt", "");
								 	            	str111.addAttribute("prc_bdt", "");
								 	            	str111.addAttribute("gift_eftdata0", "");
								 	            	str111.addAttribute("gift_eftdata1", "");
								 	            	str111.addAttribute("gift_eftdata2", "");
								 	            	str111.addAttribute("gift_eftdata3", "");
								 	            	str111.addAttribute("gift_eftdata4", "");
								 	            	str111.addAttribute("gift_eftdata5", "");
								 	            	str111.addAttribute("gift_eftdata6", "");
								 	            	str111.addAttribute("gift_eftdata7", "");
								 	            	str111.addAttribute("gift_eftdata8", "");
								 	            	str111.addAttribute("gift_eftdata9", "");
								 	            	str111.addAttribute("subloc_code", "");
								 	            	str111.addAttribute("subloc_id", "");
								 	            	str111.addAttribute("tender_state", "0");
								 	            	str111.addAttribute("failure_msg", "");
								 	            	str111.addAttribute("proc_date", "");
								 	            	str111.addAttribute("cent_commit_txn", "0");
								 	            	str111.addAttribute("price_flag", "0");
								 	            	str111.addAttribute("force_orig_tax", "");
								 	            	str111.addAttribute("sn_qty", "");
								 	            	str111.addAttribute("sn_active", "");
								 	            	str111.addAttribute("sn_received", "");
								 	            	str111.addAttribute("sn_sold", "");
								 	            	str111.addAttribute("sn_transferred", "");
								 	            	str111.addAttribute("sn_so_reserved", "");
								 	            	str111.addAttribute("sn_returned", "");
								 	            	str111.addAttribute("sn_returned_to_vnd", "");
								 	            	str111.addAttribute("sn_adjusted", "");
								 	            	str111.addAttribute("tax_perc_lock", "");
								 	            	str111.addAttribute("ref_store_no", "");
								 	            	str111.addAttribute("tax_area2_name", "");
								 	            	str111.addAttribute("empl_sbs_no", "1");
								 	            	str111.addAttribute("empl_name", trade_emp_code);
								 	            	str111.addAttribute("disc_reason_name", "");
								 	            	str111.addAttribute("empl_sbs_no2", "");
								 	            	str111.addAttribute("empl_name2", "");
								 	            	str111.addAttribute("empl_sbs_no3", "");
								 	            	str111.addAttribute("empl_name3", "");
								 	            	str111.addAttribute("empl_sbs_no4", "");
								 	            	str111.addAttribute("empl_name4", "");
								 	            	str111.addAttribute("empl_sbs_no5", "");
								 	            	str111.addAttribute("empl_name5", "");
								 	            	str111.addAttribute("ship_method", "");
								 	            	str111.addAttribute("item_reason_type", "");
								 	            	str111.addAttribute("item_reason_name", "");
								 	            	
//								 	            			Postesalescreaterequest1 res3=postList.get(0);
//								 	            			Postesalescreaterequest1 res2=postList1.get(0);
//								 	            			
//								 	            			Postesalescreaterequest1 res4=postList2.get(0);
								 	            	if(res2.getUdf3().equals("")){
								 	            		str1111.addAttribute("item_sid",res11.getItem_sid());//
								 	            	//	System.out.println("-------------upc----------------"+map2.get(tax_amt).get(tax_amt11).get("sku_code"));
								 		            	str1111.addAttribute("upc", map3.get(tax_amt).get(tax_amt11).get("upc"));//map1.get(tax_amt).get("sku_code")
								 		            	str1111.addAttribute("alu", "");
								 		            	str1111.addAttribute("style_sid", res11.getStyle_sid());
								 		            	str1111.addAttribute("dcs_code", res11.getDcs_code());
								 		            	str1111.addAttribute("vend_code", res11.getVend_code());
								 		            	str1111.addAttribute("scale_no", res11.getScale_no());
								 		            	str1111.addAttribute("description1", res11.getDescription1());
								 		            	str1111.addAttribute("description2", res11.getDescription2());
								 		            	str1111.addAttribute("description3", res11.getDescription3());
								 		            	str1111.addAttribute("description4", res11.getDescription4());
								 		            	str1111.addAttribute("attr", res11.getAttr());
								 		            	str1111.addAttribute("siz", res11.getSiz());
								 		            	str1111.addAttribute("use_qty_decimals", "");
								 		            	str1111.addAttribute("tax_code", "");
								 		            	str1111.addAttribute("flag", "");
								 		            	str1111.addAttribute("ext_flag", "");
								 		            	str1111.addAttribute("item_no", "");
								 		            	str1111.addAttribute("udf3_value", res2.getUdf3());
								 		            	str1111.addAttribute("udf4_value", res2.getUdf3());
								 		            	str1111.addAttribute("udf5_value", res2.getUdf3());
								 		            	str1111.addAttribute("udf6_value", res2.getUdf3());
								 		            	str1111.addAttribute("aux1_value", res2.getUdf3());
								 		            	str1111.addAttribute("aux2_value", res2.getUdf3());
								 		            	str1111.addAttribute("aux3_value", res2.getUdf3());
								 		            	str1111.addAttribute("aux4_value", res2.getUdf3());
								 		            	str1111.addAttribute("aux5_value", res2.getUdf3());
								 		            	str1111.addAttribute("aux6_value", res2.getUdf3());
								 		            	str1111.addAttribute("aux7_value", res2.getUdf3());
								 		            	str1111.addAttribute("aux8_value", res2.getUdf3());
								 	            		
								 	            	}else{
								 	            		str1111.addAttribute("item_sid", res11.getItem_sid());
								 		            	str1111.addAttribute("upc", map3.get(tax_amt).get(tax_amt11).get("upc"));
								 		            	str1111.addAttribute("alu", "");
								 		            	str1111.addAttribute("style_sid", res11.getStyle_sid());
								 		            	str1111.addAttribute("dcs_code", res11.getDcs_code());
								 		            	str1111.addAttribute("vend_code", res11.getVend_code());
								 		            	str1111.addAttribute("scale_no", res11.getScale_no());
								 		            	str1111.addAttribute("description1", res11.getDescription1());
								 		            	str1111.addAttribute("description2", res11.getDescription2());
								 		            	str1111.addAttribute("description3", res11.getDescription3());
								 		            	str1111.addAttribute("description4", res11.getDescription4());
								 		            	str1111.addAttribute("attr", res11.getAttr());
								 		            	str1111.addAttribute("siz", res11.getSiz());
								 		            	str1111.addAttribute("use_qty_decimals", "");
								 		            	str1111.addAttribute("tax_code", "");
								 		            	str1111.addAttribute("flag", "");
								 		            	str1111.addAttribute("ext_flag", "");
								 		            	str1111.addAttribute("item_no", "");
								 		            	str1111.addAttribute("udf3_value", res22.getUdf3());
								 		            	str1111.addAttribute("udf4_value", res22.getUdf4());
								 		            	str1111.addAttribute("udf5_value", res22.getUdf5());
								 		            	str1111.addAttribute("udf6_value", res22.getUdf6());
								 		            	str1111.addAttribute("aux1_value", res22.getUdf7());
								 		            	str1111.addAttribute("aux2_value", res22.getUdf8());
								 		            	str1111.addAttribute("aux3_value", res22.getUdf9());
								 		            	str1111.addAttribute("aux4_value", res22.getUdf10());
								 		            	str1111.addAttribute("aux5_value", res22.getUdf11());
								 		            	str1111.addAttribute("aux6_value", res22.getUdf12());
								 		            	str1111.addAttribute("aux7_value", res22.getUdf13());
								 		            	str1111.addAttribute("aux8_value", res22.getUdf14());
								 	            		
								 	            	}
								 	            	
												}
				 	            			
				 	            		}
				 	            	
				 	           	
//				 	            	
//				 	            	String t=map1.get(tax_amt).get("trade_amount_delivery");
//				 	            	String t1=map1.get(tax_amt).get("trade_amount_offset_coupon");
//				 	            	String t2=map1.get(tax_amt).get("trade_amount_offset_integral");
				 	            	double t3=0;
				 	            	
//				 	            	double tt=Double.valueOf(t);
//				 	            		t3=tt;
				 	            	
				 	            	//trade_amount_delivery+amount
//				 	            	int tt1=Integer.parseInt(t1);
//				 	            	int tt2=Integer.parseInt(t2);
				 	            	//trade_amount_delivery+amount
				 	            		for (int j = 0; j < map3.get(tax_amt).size(); j++) {
				 	            			if(map3.get(tax_amt).get("0")!=null){
				 	            				String jj=String.valueOf(j);
						 	            		String ttt=map3.get(tax_amt).get(jj).get("amount");
						 	            		double tt1=Double.valueOf(ttt);
							 	            	t3=t3+tt1;
						 	            		
						 	            	}
										}
				 	            
				 	            	
				 	            	String sr=String.valueOf(String.format("%.2f", t3));
				 	            	 
				 	            	
				 	            	str88.addAttribute("fee_type", "0");
				 	            	str88.addAttribute("tax_perc", "0");
				 	            	str88.addAttribute("tax_incl", "0");
				 	            	str88.addAttribute("amt", sr);
				 	            	str88.addAttribute("fee_name", "Fee");
				 	            	
				 	            	invc_no++;
				 	            	String sb=df1 +"    "+"trade_code="+map1.get(tax_amt).get("trade_code")+"     导入成功！";
				 	            	String sb1=map1.get(tax_amt).get("trade_code");
				 	            	//scode11.add(map1.get(tax_amt).get("trade_code"));
				 	            	
					    			p.println(sb);
					    			p22.println(sb1);
			 	            	}else if(postList6.size()>0){
			 	            		//通知单子取消
				 	            	 String XX1="&trade_code["+i+"]="+map1.get(tax_amt).get("trade_code");
			            			 String a1="{https://wx.fionacos.com/index.php?s=ThdApi-Index-index&\"method\":\"oauth.trade.listcb\",\"nowtime\":\"2017-06-29 10:30:00\",\"page\":\"1\",\"page_size\":\"10\",\"system\":\"pos\"}";
			     	 				 String ur1=url+"&method=oauth.trade.listcb&nowtime=2017-06-29 10:30:00&page=1&page_size=200&system=pos";
			     	 				 ur1=ur1+XX1;
			     	 				 scode1=it.tune(a1, ur1);
			     	 				 System.out.println("通知单子取消");
			 	            	}
			 	     			
			 				}else{
			 					String sb1=df1 +"    "+"trade_code="+map1.get(tax_amt).get("trade_code")+"     导入失败！";
			 					String sb2=df1 +"    "+"失败原因："+"该会员不存在或信息错误！";
			 					p.println(sb1);
			 					p.println(sb2);
			 					continue;
			 				//	doc.remove(root);
			 				}
		            		 
		            	 }
		         			
		 				
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						 SimpleDateFormat df11001 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
							FileOutputStream fos001;
							fos001 = new FileOutputStream("logsale/log/"+df11001.format(new Date())+"log.txt", true);
							PrintStream pp001 = new PrintStream(fos001);
							e.printStackTrace(pp001.printf("", ""));
						continue;
					}
	            	
	            	
	            	
	            }
	            	 
	            try {
	            	 String msg="";
	 			
	 			System.out.println("处理");
				} catch (Exception e) {
					// TODO: handle exception
			     	System.out.println("==================================================================");
			     	System.out.println("信息返回失败！！！");
			     	e.printStackTrace();
					 SimpleDateFormat df11001 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
						FileOutputStream fos001;
						fos001 = new FileOutputStream("logsale/log/"+df11001.format(new Date())+"log.txt", true);
						PrintStream pp001 = new PrintStream(fos001);
						e.printStackTrace(pp001.printf("", ""));
				}
	           
	            
     	
     	
    	File file4 = new File("txtsale/txt.txt");//Text文件
		BufferedReader br1 = new BufferedReader(new FileReader(file4));
		String sss= br1.readLine();
		    if("".equals(sss.trim())){
		    	sss="101";
		    }
		
        	int sss1=Integer.parseInt(sss);
        	System.out.println("ss1="+sss1);
        	sss1++;
        	System.out.println("ss1="+sss1);
        	if(sss1>200){
        		sss1=100;
        	}
        	FileOutputStream fos1 = new FileOutputStream("txtsale/txt.txt");
			//	System.out.println("--txt----------OK------");
			PrintStream p1 = new PrintStream(fos1);
			p1.print(sss1);
			p1.flush();
   			p1.close();
   			br1.close();
			try {
				 File file=new File("txtsale/Invoice"+sss+".xml");
			       //     File file1=new File("txt/log.txt");
			        	OutputStream out=new FileOutputStream(file);
			        //	OutputStream out1=new FileOutputStream(file1);
			        	//设置文档存储是的格式
			        	OutputFormat format=OutputFormat.createPrettyPrint();
			        	//保存的编码集是什么
			        	format.setEncoding("UTF-8");
			        	XMLWriter xmlwriter=new XMLWriter(out,format);
			        	
			        	//写
			        	xmlwriter.write(doc);
			        	xmlwriter.flush();
			        	xmlwriter.close();
			        	out.close();
//			        	添加dos命令
//			        	门店需要添加
//				    String command1="cmd /c copy "+xmlpath+"Invoice"+sss+".xml "+copyxmlpath+" & cmd /c move "+xmlpath+"Invoice"+sss+".xml "+path;
//				    String command2= "cmd /c  cd "+ecmpath+" & cmd /c ecmproc /in /a ";
//			       
//			        
//			            Runtime.getRuntime().exec(command1);
//			            Runtime.getRuntime().exec(command2);
			        
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("信息写入失败！");
				e.printStackTrace();
				 SimpleDateFormat df11001 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
					FileOutputStream fos001;
					fos001 = new FileOutputStream("logsale/log/"+df11001.format(new Date())+"log.txt", true);
					PrintStream pp001 = new PrintStream(fos001);
					e.printStackTrace(pp001.printf("", ""));
			}
     	
     	
			OutputStream out1=null;
	        	
	        	try {
	        		String doc1=String.valueOf(invc_no);
		        	File file1=new File("invc_no.txt");
		   	        out1=new FileOutputStream(file1);		   	        	
//		   	        	写

		   	        	out1.write(doc1.getBytes());
		   	        	out1.flush();
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("invc_no出错！！！！");
				}finally{
					if(out1!=null){
		   	        	out1.close();
					}
				}
	        	
	        	
	        	
//	        	

			} catch (Exception e) {
				System.out.println("未找到指定路径的文件!");
				
				e.printStackTrace();
				 SimpleDateFormat df11001 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
					FileOutputStream fos001;
					fos001 = new FileOutputStream("logsale/log/"+df11001.format(new Date())+"log.txt", true);
					PrintStream pp001 = new PrintStream(fos001);
					e.printStackTrace(pp001.printf("", ""));
			}
			
			
		}
			catch (Exception e) {
			System.out.println(e.getMessage());
			
	}
			
		System.out.println("---------End---------");
}
	/*
	 * 锟斤拷取锟斤拷锟斤拷锟侥硷拷
	 */
	private void reDelploy() {
		
		try {
			asList = cocoImpl.reDeploy(asList);
			// 获取值并且附值
			method = asList.get(0);
			nowtime = asList.get(1);
			page = asList.get(2);
			page_size=asList.get(3);
			system = asList.get(4);
			url=asList.get(8);
			store=asList.get(9);
			path=asList.get(10);
			xmlpath=asList.get(11);
			ecmpath=asList.get(12);
			copyxmlpath=asList.get(13);
		} catch (Exception e) {
			System.out.println("deploy:"+e.getMessage());
		}
	}



	@Test
	public Map<String,Map<String,String>> json(List<String>list){
		Map<String,Map<String,String>> map=new HashMap<String,Map<String,String>>();  
		for(int i=0;i<list.size();i++){
		String s=list.get(i);
		//System.out.println(s);
	//String s1=	s.substring(27,s.length()-2);
	demo d=new demo();
//	System.out.println("s="+s);
//	System.out.println("s1="+s1);
//	String s2=StringUtils.substringBeforeLast(s1, "},"); 
//	String s3=StringUtils.substringBefore(s2, "},"); 
//	System.out.println("s2[0]="+s2);
//	System.out.println("s3[0]="+s3);
	map=d.str(s);
		}
		return map;
	}
	
	public Map<String,Map<String,Map<String,String>>> json1(List<String>list){
		Map<String,Map<String,Map<String,String>>> map=new HashMap<String,Map<String,Map<String,String>>>();  
		for(int i=0;i<list.size();i++){
		String s=list.get(i);
		//System.out.println(s);
	//String s1=	s.substring(27,s.length()-2);
	demo d=new demo();
//	System.out.println("s="+s);
//	System.out.println("s1="+s1);
//	String s2=StringUtils.substringBeforeLast(s1, "},"); 
//	String s3=StringUtils.substringBefore(s2, "},"); 
//	System.out.println("s2[0]="+s2);
//	System.out.println("s3[0]="+s3);
	map=d.str1(s);
		}
		return map;
	}
	
	public Map<String,Map<String,Map<String,String>>> json2(List<String>list){
		Map<String,Map<String,Map<String,String>>> map=new HashMap<String,Map<String,Map<String,String>>>();  
		for(int i=0;i<list.size();i++){
		String s=list.get(i);
		//System.out.println(s);
	//String s1=	s.substring(27,s.length()-2);
	demo d=new demo();
//	System.out.println("s="+s);
//	System.out.println("s1="+s1);
//	String s2=StringUtils.substringBeforeLast(s1, "},"); 
//	String s3=StringUtils.substringBefore(s2, "},"); 
//	System.out.println("s2[0]="+s2);
//	System.out.println("s3[0]="+s3);
	map=d.str2(s);
		}
		return map;
	}

	private static String method;
	private static String nowtime;
	private static String page;
	private static String page_size;
	private static String system;
	private String itemOrgId;
	private String baseCurrencyCode;
	private String tenderCode;
	private static String Store_no;
	private static String sbs_no;
	private static String startTime;
	private static String endTime;
	private static String url;
	private static String store;
	private static String path;
	private static String xmlpath;
	private static String ecmpath;
	private static String copyxmlpath;
//	private int startTime;
//	private int endTime;
}
