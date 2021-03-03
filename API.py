import requests
import json
import math
import hashlib
import time
import datetime
import cx_Oracle as cx
import random
import xml.etree.ElementTree as ET  # XML Create
import logging  # Log object
import os         # for save files


class webservice:
    # [Class Setup info]---------------------------------------------------------------------------------------------------[+]
    test_type = 'test'
    url_token = 'http://testpicksys.retailo2o.com/erpExternal/api/comm/getToken.do?_platform_num='
    url_getorder = 'http://testpicksys.retailo2o.com/picksysdi/api/orderOperate/getPageRetailProInfo.do'
    t_token = ''
    t_platform_num = ''
    t_deptcode = ''
    t_store_no = ''
    t_timeStamp = ''
    t_startDateStamp = ''
    t_EndDateStamp = ''
    t_startDate = ''
    t_endDate = ''
    key_value = {}
    key_value['_platform_num'] = ''
    key_value['token'] = ''
    key_value['timeStamp'] = ''
    key_value['param'] = ''
    key_value['sign'] = ''
    t_invc_no = 0  # start int get from Oracle Invoice MAX
    CreateDOCList = []  # save Doc_No in check time,if document can input
    errorlist = []  # test
    CustInfo = []
    ItemInfo = []


    dns = cx.makedsn('192.168.10.27', '1521', 'rproods')  # setup Oracle info
    connection = cx.connect('reportuser', 'report',
                            dns)  # connection to Oracle
    cur = connection.cursor()    # Create Cursor for select run
    # ------------------[    Log Part     ]-------------------------------------------------[+]
    LOG_FORMAT = "%(asctime)s - %(levelname)s - %(message)s"
    logging.basicConfig(filename='API.log',
                        level=logging.INFO, format=LOG_FORMAT)
    logging.info(
        '-                                                               -')
    logging.info(
        'API Program Start Datetime:     [  '+str(time.strftime('%y-%m-%d %H:%M:%S'))+'  ]')
    # ------------------[    Log Part     ]-------------------------------------------------[-]


# [Class Setup info]---------------------------------------------------------------------------------------------------[-]


    def __init__(self, p_platform_num, p_startDateTime, p_endDateTime):
        print('--------   Main Object   --------')
        self.t_platform_num = p_platform_num

        self.t_startDateStamp = str(
            int(time.mktime(time.strptime(p_startDateTime, "%Y-%m-%d %H:%M:%S"))))
        self.t_EndDateStamp = str(
            int(time.mktime(time.strptime(p_endDateTime, "%Y-%m-%d %H:%M:%S"))))
        self.t_startDate = p_startDateTime
        self.t_endDate = p_endDateTime
        print('Set platform    = '+self.t_platform_num)

        print('Start TimeStamp = '+self.t_startDateStamp)
        print('End TimeStamp   = '+self.t_EndDateStamp)
        print('Start Date      = '+self.t_startDate)
        print('End Date        = '+self.t_endDate)
# ---[Oracle]--------------------------------------------------------[Check Part]------------------------------------------[+]

    def getGlobStore(self):
        #  取当前店铺的全局店铺号码
        self.cur.execute(
            "select s.glob_store_code,s.store_no from cms.default_store df,cms.store s where df.sbs_no=1 and df.default_doc_type=0 and df.store_no=s.store_no and s.sbs_no=1")
        getstr = self.cur.fetchone()
        if getstr != None:
            result = list(getstr)
            self.t_deptcode = result[0]
            self.t_store_no = str(result[1])
            print('DetpCode(GLOBStore):'+self.t_deptcode)
            print('StoreNo:'+str(self.t_store_no))
        else:
            logging.info(self.getCurrDatetime(
            )+'  |Error|-------->     Glob_store not found')  # Append Log

    def getInvcNo(self):
        #  取当前系统中电商单据的最大单据号码并  +  1
        self.cur.execute(
            "select nvl(max(invc_no)+1,'1') from cms.invoice t ,cms.default_store df where df.sbs_no=1 and df.default_doc_type=0 and df.store_no=t.store_no and length(t.tracking_no)>6 order by t.invc_no ")
        getstr = self.cur.fetchone()
        result = list(getstr)
        return result[0]

    def getCurrDatetime(self):
        return str(time.strftime('%y-%m-%d %H:%M:%S'))

    def itemCheck(self, p_itemcode):

        self.cur.execute(
            "select t.item_sid,p.price,t.local_upc,t.alu,t.style_sid,t.dcs_code,t.vend_code,t.description1,t.description2,t.description3,t.description4,t.siz "+
            "from  cms.invn_sbs t ,cms.invn_sbs_price p where t.item_sid=p.item_sid(+) and p.price_lvl(+)=1 and t.alu="+p_itemcode)
        getstr = self.cur.fetchone()
        if getstr != None:
            result = list(getstr)
        else:
            result = None

        return result

    def invcCheck(self, p_invcno):

        self.cur.execute(
            "select invc_sid,tracking_no from cms.invoice where tracking_no='"+p_invcno+"'")
        result = self.cur.fetchone()

        return result

    def invc_sid_check(self, p_invcsid):

        self.cur.execute(
            "select invc_sid from cms.invoice where invc_sid="+p_invcsid)
        result = self.cur.fetchone()

        return result

    def customer_check(self, p_custno):
             
        sql="select t.cust_sid,t.cust_id,t.store_no,t.first_name,t.last_name,t.info1,nvl(t.info2,''),to_char(t.modified_date,'YYYY-MM-DD"'"T"'"HH24:MI:SSTZH:TZM'),"
        sql=sql+"(select max(phone1)from cms.cust_address a where t.cust_sid=a.cust_sid ) from cms.customer t where t.info1='"+p_custno+"'"
        self.cur.execute(sql)
        result = self.cur.fetchone()
        return result

    def writeToFile(self, p_str):
        patch = '.\\log\\Api'+str(time.strftime('%y%m%d%H%M%S'))+'.json'
        with open(patch, 'w') as wf:
            wf.write(p_str)


# ---[Oracle]--------------------------------------------------------[Check Part]------------------------------------------[-]


    def getsid(self):
        invc_check = ''
        sidcount = 0
        while (invc_check != None):
            sid = '160007'
            x = [random.randint(10, 18) for i in range(6)]
            for i in x:
                sid = sid+str(i)
            sidcount = sidcount + 1
            invc_check = self.invc_sid_check(sid)
            if invc_check == None:
                print('sid can use [count:'+str(sidcount)+']')

        return sid

    def TimeStampToDateTime(self, p_TimeStamp, p_type):
        p_TimeStamp = p_TimeStamp[0:10]
        timeStamp = int(p_TimeStamp)

        timeArray = time.localtime(timeStamp)
        if p_type == 'Create':
            otherStyleTime = time.strftime("%Y-%m-%dT%H:%M:%S", timeArray)
        if p_type == 'Modify':
            otherStyleTime = time.strftime(
                "%Y-%m-%dT%H:%M:%S+08:00", timeArray)

        return otherStyleTime
# [XML Format]--------------------------------------------------------------------------[+]

    def prettify(self, element, indent='  '):
        queue = [(0, element)]  # (level, element)
        while queue:
            level, element = queue.pop(0)
            children = [(level + 1, child) for child in list(element)]
            if children:
                element.text = '\n' + indent * (level+1)  # for child open
            if queue:
                element.tail = '\n' + indent * queue[0][0]  # for sibling open
            else:
                element.tail = '\n' + indent * (level-1)  # for parent close
            queue[0:0] = children  # prepend so children come before siblings

# [XML Format]--------------------------------------------------------------------------[-]

    def gettoken(self):
        get_respons = requests.get(self.url_token+self.t_platform_num)
        self.t_token = json.loads(get_respons.text).get(
            'content').get('result')
        print('---------   Get Token   ---------')
        print('Token = '+self.t_token)

    def getorder(self):
        self.gettoken()
        # ----- Http Headers ----------- +

        headers = {
            "Content-Type": "application/json",
            "Accept": "*/*",
            "Accept-Encoding": "gzip,deflate,br",
            "Connection": "keep-alive"
        }
        # ----- Http Headers ----------- -

        # ----- Set Base Info ---------- +
        self.getGlobStore()

        self.t_timeStamp = str(round(time.time()*1000))
        self.key_value['_platform_num'] = self.t_platform_num
        self.key_value['token'] = self.t_token
        self.key_value['timeStamp'] = self.t_timeStamp
        paramJSON = {'_platform_num': self.t_platform_num, 'pageIndex': 1, 'pageSize': 10,
                     'createTimeFrom': self.t_startDate, 'createTimeTo': self.t_endDate, 'deptCode': self.t_deptcode}

        self.key_value['param'] = json.dumps(paramJSON)

        print(str(self.key_value['param']))
        # ----- Set Base Info ---------- +

        # ----- Sign Part -------------- +

        str_sign = 'key='+self.t_token+'&_platform_num=' + \
            self.t_platform_num+'&timeStamp='+self.t_timeStamp
        md5 = hashlib.md5()
        md5.update(str_sign.encode(encoding='UTF-8'))
        md5mod = str(md5.hexdigest())
        self.key_value['sign'] = md5mod.upper

        # ----- Sign Part -------------- -

        # ----- Http Request ----------- + (call API)

        print('----------   GetOrder    ----------')
        # try:
        if (1 == 1):
            get_respons = requests.post(
                url=self.url_getorder, headers=headers, data=json.dumps(paramJSON), verify=False)
            # print(get_respons.text)
            # -------Get JSON Str---------------------

            JsonStr = json.loads(get_respons.text)  # Get API JSON

            self.writeToFile(str(JsonStr))
            print('success:'+str(JsonStr['success']))
            print('PageCount:'+str(JsonStr['content']
                                   ['result']['pageCond']['count']))
            # -------Get JSON Str---------------------

# [JSON-Check Document / Customer / Items]-----------------------------------------------------------------------------------------------------------------------[+]
        # -------Loop List -----------------------(+)
            print('-------Check  Document ------->')
            for docList in JsonStr['content']['result']['list']:
                documentError = 0   # if documentError = 1 will not created
                # ------------------------Header-------------------------[+]
                print('------------------ Header ---------------------')
                # print(docList)
                print('[Header]-------->  DocumentNo:'+docList['dealCode']+'   Date:'+str(docList['createTime'])+'  SaleFlag:'+str(docList['saleFlag']

                                                                                                                                   )+'  RecvName:'+docList['recvName']+'  BuyerId:'+str(docList['buyerId'])+' TotalPayment:'+str(docList['dealTotalFee']))
                # ---------------   Check Invoice ------------------------------------
                str_checkinvc = self.invcCheck(docList['dealCode'])
                if str_checkinvc == None:
                    print('Document Is New')
                else:
                    print('Document already exist '+str(str_checkinvc))
                    logging.info(self.getCurrDatetime(
                    )+'  |Note|-------->   Document already exist '+str(str_checkinvc))  # Append Log

                # ---------------   Check Customer ------------------------------------
                str_checkcust = self.customer_check(str(docList['buyerId']))
                if str_checkcust == None:
                    print('Customer Not Exist')
                    documentError = 1
                    logging.info(self.getCurrDatetime(
                    )+'  |Error|-------->   Customer Not Exist '+str(docList['buyerId']))  # Append Log
                else:
                    print('Cust is Found Name:'+str_checkcust[4]+'  Modify date:'+str(str_checkcust[7]))

                # ------------------------Header-------------------------[+]

                print('------------------Lines------------------------')
                if str_checkinvc == None:
                    for docDetail in docList['details']:

                        print('[Lines]--------->   DocumentNo:'+docList['dealCode']+'   ItemName:'+docDetail['goodsName']+'  Barcode:' +
                              docDetail['barCode']+'   Quantity:'+str(docDetail['amount'])+'  Price:' + str(docDetail['tradePrice']))
                    # ------------- Check Item ---------------------------------[+]
                        str_checkitem = self.itemCheck(docDetail['barCode'])
                        if str_checkitem != None:
                            if self.test_type == 'test':  # ----test-------
                                # ----test-------
                                print('Item Sid:'+str(str_checkitem[1]))
                                # ----test-------
                                print('List[3]:'+str(str_checkitem[3]))
                        else:
                            print('  |Error|-------->[Item Not Found]  Document No:' +
                                  docList['dealCode']+'   Barcode:'+docDetail['barCode'])
                            documentError = documentError + 1
                            logging.info(self.getCurrDatetime(
                            )+'  |Error|-------->[Item Not Found]  Document No:'+docList['dealCode']+'   Barcode:'+docDetail['barCode'])  # Append Log
                       # ---------------- Check Item ---------------------------------[-]
                       #
                       # ------- Check if document not error then insert to created list -------[+]
                    if documentError == 0:
                        self.CreateDOCList.append(docList['dealCode'])
                        if self.test_type == 'test':
                            # ----test-------
                            print(docList['dealCode']+' will Created')
                       # -----------------------------------------------------------------------[-]

        # -------Loop List -----------------------(-)
# [JSON-Check Document / Customer / Items]-----------------------------------------------------------------------------------------------------------------------[-]


#                                                             [------]                                                                                           [-]

        
# ---------[     Create XML part       ]----------------------------------------------------------------------------------------------------------------------------------------[+]
        if str(JsonStr['success']) == 'True' and JsonStr['content']['result']['pageCond']['count']>= 1:
            print('Start Create XML')
            self.t_invc_no = self.getInvcNo  # get Invc_No
            logging.info(self.getCurrDatetime() +
                         '  |Note|-------->[Create  XML Document]')  # Append Log
            xml_doc = ET.Element('DOCUMENT')
            logging.info("Create Document Tree")
            invoices = ET.SubElement(xml_doc,'INVOICES')
            self.t_invc_no = self.getInvcNo()
            for docList in JsonStr['content']['result']['list']:

                if docList['dealCode'] in self.CreateDOCList:
                    #----------------------------------Document Loop ------------------------------------------------------------------------------------------------>>
                    print(docList['dealCode']+'    :'+'Go to Created XML')
                    getsidstr = self.getsid()  # ---------    Get INVC_SID
                    if self.test_type == 'test':
                        print('Get Invoice SID:'+getsidstr)
                    self.CustInfo = self.customer_check(str(docList['buyerId'])) # get customer info

                    if docList['saleFlag']=='0':
                       invc_type='0'
                    else:
                       invc_type='2'

                    #---------------------------------  Invoice Header -------------------------------------------------------------------------------
                    logging.info("Create INVCOICES Tree ")
                    invoice  = ET.SubElement(invoices,'INVOICE',INVC_SID = getsidstr,
                         sbs_no="1",store_no=self.t_store_no,invc_no=str(self.t_invc_no),invc_type=invc_type ,hisec_type="0" ,status="2" ,proc_status="0" ,cust_sid=str(self.CustInfo[0]) ,addr_no="1" ,shipto_cust_sid=str(self.CustInfo[0]),
                         shipto_addr_no="1" ,station="" ,workstation="1" ,orig_store_no=str(self.t_store_no) ,orig_station="" ,use_vat="1" ,vat_options="0" ,so_no="" ,so_sid="" ,cust_po_no="" ,note="",
                         disc_perc="" ,disc_amt="" ,disc_perc_spread="" ,over_tax_perc="" ,over_tax_perc2="" ,tax_reb_perc="" ,tax_reb_amt="" ,rounding_offset="" ,
                         created_date=self.TimeStampToDateTime(str(docList['createTime']),'Create') ,modified_date=self.TimeStampToDateTime(str(docList['createTime']),'Modify') ,post_date=self.TimeStampToDateTime(str(docList['createTime']),'Create') ,tracking_no=str(docList['dealCode']) ,ref_invc_sid="" ,
                         audited="0" ,cms_post_date=self.TimeStampToDateTime(str(docList['createTime']),'Create'),ws_seq_no="" ,cust_fld="" ,held="0" ,drawer_no=str(self.t_store_no) ,controller="8888" ,orig_controller="8888" ,elapsed_time="",
                         till_name="" ,activity_perc="" ,activity_perc2="" ,activity_perc3="" ,activity_perc4="" ,activity_perc5="" ,eft_invc_no="" ,detax="0" ,doc_ref_no="" ,fiscal_doc_id="" ,
                         subloc_code="" ,subloc_id="" ,ship_perc="" ,trans_disc_amt="" ,empl_sbs_no="1" ,empl_name="10005751" ,tax_area_name="CH_VAT" ,tax_area2_name="" ,ref_invc_no="" ,
                         ref_invc_created_date="" ,createdby_sbs_no="1", createdby_empl_name="10005751" ,modifiedby_sbs_no="1" ,modifiedby_empl_name="10005751" ,clerk_sbs_no="1" ,clerk_name="10005751" ,
                         clerk_sbs_no2="" ,clerk_name2="" ,clerk_sbs_no3="" ,clerk_name3="", clerk_sbs_no4="", clerk_name4="", clerk_sbs_no5="" ,clerk_name5="", disbur_reason_type="" ,disbur_reason_name="",
                         doc_reason_code="")
                    #---------------------------------------------------------------------------------------------------------------------------------
                        
                    customer =  ET.SubElement(invoice,'CUSTOMER',cust_sid=str(self.CustInfo[0]),cust_id=str(self.CustInfo[1]) ,store_no=str(self.CustInfo[2]) ,station="" ,first_name=str(self.CustInfo[3]) ,last_name=str(self.CustInfo[4]),price_lvl="", detax="0" ,info1=str(self.CustInfo[5]) ,
                          info2=str(self.CustInfo[6]) ,modified_date=str(self.CustInfo[7]),sbs_no="1" ,cms="0" ,company_name="" ,title="" ,tax_area_name="" ,shipping="0", address1="" ,
                          address2="" ,address3="" ,address4="" ,address5="" ,address6="43" ,zip="", phone1=str(self.CustInfo[8]) ,phone2="", country_name="" ,alternate_id1="" ,alternate_id2="" )                      
                    shiptocustomer =  ET.SubElement(invoice,'SHIPTO_CUSTOMER',cust_sid=str(self.CustInfo[0]),cust_id=str(self.CustInfo[1]) ,store_no=str(self.CustInfo[2]) ,station="" ,first_name=str(self.CustInfo[3]) ,last_name=str(self.CustInfo[4]),price_lvl="", detax="0" ,info1=str(self.CustInfo[5]) ,
                          info2=str(self.CustInfo[6]) ,modified_date=str(self.CustInfo[7]),sbs_no="1" ,cms="0" ,company_name="" ,title="" ,tax_area_name="" ,shipping="0", address1="" ,
                          address2="" ,address3="" ,address4="" ,address5="" ,address6="43" ,zip="", phone1=str(self.CustInfo[8]) ,phone2="", country_name="" ,alternate_id1="" ,alternate_id2="" )  
                    INVC_SUPPLS   = ET.SubElement(invoice,'INVC_SUPPLS')
                    INVC_COMMENTS = ET.SubElement(invoice,'INVC_COMMENTS')
                    INVC_COMMENT  = ET.SubElement(INVC_COMMENTS,'INVC_COMMENT',comment_no="1" ,comments="快递配送")
                    INVC_EXTRAS   = ET.SubElement(invoice,'INVC_EXTRAS')
                    INVC_FEES     = ET.SubElement(invoice,'INVC_FEES')    
                    INVC_FEE      = ET.SubElement(INVC_FEES,'INVC_FEE',fee_type="0", tax_perc="0", tax_incl="0", amt="0" ,fee_name="Fee")
                    INVC_TENDERS  = ET.SubElement(invoice,'INVC_TENDERS')
                    INVC_TENDER   = ET.SubElement(INVC_TENDERS,'INVC_TENDER',tender_type="2", tender_no="1" ,taken=str(docList['dealTotalPayment']) ,given="0" ,amt=str(docList['dealTotalPayment']),doc_no="" ,auth="" ,reference="" ,chk_company="", chk_first_name="",
                              chk_last_name="", chk_work_phone="" ,chk_home_phone="" ,chk_state_code="" ,chk_dl="", chk_dl_exp_date="" ,chk_dob_date="", crd_exp_month="" ,crd_exp_year="" ,
                              crd_normal_sale="", crd_contr_no="" ,crd_present="" ,crd_zip="" ,crd_proc_fee="", gft_crd_trace_no="", gft_crd_int_ref_no="" ,gft_crd_balance="", charge_net_days="",
                              charge_disc_days="" ,charge_disc_perc="" ,pmt_date="", pmt_remark="", matched="", manual_name="" ,manual_remark="" ,transaction_id="" ,avs_code="", chk_type="" ,cashback_amt="" ,
                              l2_result_code="", signature_map="", orig_crd_name="", tender_state="0" ,failure_msg="" ,proc_date="" ,orig_currency_name="", eftdata0="", eftdata1="" ,eftdata2="", eftdata3="" ,
                              eftdata4="" ,eftdata5="", eftdata6="", eftdata7="" ,eftdata8="" ,eftdata9="" ,cardholder_name="", give_rate="" ,take_rate="" ,base_taken="" ,base_given="" ,cent_txn_id="" ,
                              balance_remaining="", cent_commit_txn="", emv_aid="" ,emv_applabel="", emv_card_exp_date="", emv_cyrpto_type="", emv_cryptogram="" ,emv_pin_statement="" ,cayan_sf_id="" ,
                              crd_name="" ,currency_name="")
                    INVC_COUPONS  = ET.SubElement(invoice,'INVC_COUPONS')      


                    #----------------INVC_ITEM  Detail--------------------------------------------------------------------------------------------------->>
                    linecount =0   
                    for docDetail in docList['details']:
                        #----------------Line Loop---------------------------------------------------------------------------------------->>
                        print('[Lines]--------->   DocumentNo:'+docList['dealCode']+'   ItemName:'+docDetail['goodsName']+'  Barcode:' +
                        docDetail['barCode']+'   Quantity:'+str(docDetail['amount'])+'  Price:' + str(docDetail['tradePrice']))
                    # ------------- Check Item ---------------------------------[+]
                        linecount = linecount + 1
                        lineTax = docDetail['saleMoney']*0.13
                        str_checkitem = self.itemCheck(docDetail['barCode'])
                        if str_checkitem != None:
                            INVC_ITEMS    = ET.SubElement(invoice,'INVC_ITEMS')
                            INVC_ITEM     = ET.SubElement(INVC_ITEMS,'INVC_ITEM',item_pos=str(linecount),item_sid=str(str_checkitem[0]), qty=str(docDetail['amount']) ,orig_price=str(str_checkitem[1]) ,orig_tax_amt=str(str_checkitem[1]*0.13),price=str(docDetail['tradePrice']), tax_code="0" ,tax_perc="13" ,tax_amt=str(lineTax) ,
                              tax_code2="" ,tax_perc2="" ,tax_amt2="" ,cost="0" ,price_lvl="1" ,spif="0" ,sched_no="" ,comm_code="" ,comm_amt="" ,cust_fld="" ,scan_upc="" ,serial_no="" ,lot_number="" ,kit_flag="0" ,
                              pkg_item_sid="", pkg_seq_no="", orig_cmpnt_item_sid="" ,detax="0" ,usr_disc_perc="0", udf_value1="", udf_value2="", udf_value3="" ,udf_value4="" ,activity_perc="100" ,activity_perc2="0",
                              activity_perc3="0" ,activity_perc4="0",activity_perc5="0" ,comm_amt2="0" ,comm_amt3="0" ,comm_amt4="0" ,comm_amt5="0", so_sid="", so_orig_item_pos="" ,item_origin="" ,pkg_no="" ,
                              shipto_cust_sid="" ,shipto_addr_no="" ,orig_cost="" ,item_note1="" ,item_note2="" ,item_note3="" ,item_note4="" ,item_note5="" ,item_note6="", item_note7="" ,item_note8="" ,item_note9="" ,item_note10="",
                              promo_flag="0" ,gift_activation_code="", gift_transaction_id="" ,returned_qty="" ,ref_item_pos="", ref_invc_sid="" ,gift_add_value="0" ,ref_invc_no="" ,alt_upc="" ,alt_alu="" ,alt_cost="",
                              alt_vend_code="" ,orig_prc_bdt="" ,prc_bdt="" ,gift_eftdata0="",gift_eftdata1="" ,gift_eftdata2="" ,gift_eftdata3="" ,gift_eftdata4="", gift_eftdata5="" ,gift_eftdata6="" ,gift_eftdata7="" ,gift_eftdata8="",
                              gift_eftdata9="" ,subloc_code="" ,subloc_id="" ,tender_state="0" ,failure_msg="" ,proc_date="" ,cent_commit_txn="0", price_flag="0", force_orig_tax="" ,sn_qty="", sn_active="", sn_received="" ,sn_sold="" ,
                              sn_transferred="" ,sn_so_reserved="" ,sn_returned="" ,sn_returned_to_vnd="" ,sn_adjusted="" ,tax_perc_lock="0" ,ref_store_no="", tax_area2_name="" ,empl_sbs_no="1" ,empl_name="10005751", disc_reason_name="" ,
                              empl_sbs_no2="", empl_name2="" ,empl_sbs_no3="" ,empl_name3="" ,empl_sbs_no4="" ,empl_name4="" ,empl_sbs_no5="", empl_name5="" ,ship_method="" ,item_reason_type="" ,item_reason_name="")

                            INVN_BASE_ITEM = ET.SubElement(INVC_ITEM,'INVN_BASE_ITEM',item_sid=str(str_checkitem[0]), upc=str(str_checkitem[2]), alu="", style_sid=str(str_checkitem[4]), dcs_code=str(str_checkitem[5]) ,vend_code=str(str_checkitem[6]), scale_no="" ,description1=str(str_checkitem[7]) ,
                              description2=str(str_checkitem[8]), description3=str(str_checkitem[9]),description4=str(str_checkitem[10]), attr="", siz=str(str_checkitem[11]) ,use_qty_decimals="0" ,tax_code="0" ,flag="0", ext_flag="0" ,item_no="" ,
                              udf3_value="" ,udf4_value="" ,udf5_value="", udf6_value="" ,aux1_value="" ,aux2_value="", aux3_value="", aux4_value="", aux5_value="", aux6_value="", aux7_value="" ,aux8_value="")                     
                        #----------------Line Loop----------------------------------------------------------------------------------------<<
                       
                       
            
                    #------------------------------------------------------------------------------------------------------------------------------------<<
                              
                    #----------------end of invoice ---------------------
                    self.t_invc_no= self.t_invc_no+1
                    #--------------------------------------------------Document loop end---------------------------------------------------------------------------<<
                logging.info("Format XML")
                self.prettify(xml_doc)

                tree = ET.ElementTree(xml_doc)
                logging.info("Create XML File Tree")
                tree.write('invoice.xml',encoding='UTF-8',xml_declaration=True)
                logging.info("Write XML FilE")

# ---------[     Create XML part       ]----------------------------------------------------------------------------------------------------------------------------------------[-]

        self.cur.close
        self.connection.close

        # except BaseException:
        #  print('ERROR:--->TimeOut 15 ')
        #  print(get_respons.text)


# -----Run Main ----------------

API = webservice('101661', '2021-02-05 00:00:00', '2021-02-05 23:59:59')
API.getorder()
