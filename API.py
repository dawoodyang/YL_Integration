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
            "select item_sid,local_upc,description2,text3 from cms.INVN_SBS where alu="+p_itemcode)
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

        self.cur.execute(
            "select cust_sid from cms.customer where info1='"+p_custno+"'")
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
            sid = '16'
            x = [random.randint(10, 18) for i in range(8)]
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
                str_checkinvc = self.invcCheck(docList['dealCode'])
                if str_checkinvc == None:
                    print('Document Is New')
                else:
                    print('Document already exist '+str(str_checkinvc))
                    logging.info(self.getCurrDatetime(
                    )+'  |Note|-------->   Document already exist '+str(str_checkinvc))  # Append Log

                str_checkcust = self.customer_check(str(docList['buyerId']))
                if str_checkcust == None:
                    print('Customer Not Exist')
                    documentError = 1
                    logging.info(self.getCurrDatetime(
                    )+'  |Error|-------->   Customer Not Exist '+str(docList['buyerId']))  # Append Log
                else:
                    print('Cust is Found ')

                # ------------------------Header-------------------------[+]

                print('------------------Lines------------------------')
                if str_checkinvc == None:
                    for docDetail in docList['details']:

                        print('[Lines]--------->   DocumentNo:'+docList['dealCode']+'   ItemName:'+docDetail['goodsName']+'  Barcode:' +
                              docDetail['barCode']+'   Quantity:'+str(docDetail['amount'])+'  Price:' + str(docDetail['tradePrice']))
                    # ------ Check Item ---------------------------------[+]
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
                       # ------- Check if document not error then insert to created list -------[+]
                    if documentError == 0:
                        self.CreateDOCList.append(docList['dealCode'])
                        if self.test_type == 'test':
                            # ----test-------
                            print(docList['dealCode']+' will Created')
                       # -----------------------------------------------------------------------[-]

                       # ------ Check Item ---------------------------------[-]

        # -------Loop List -----------------------(-)
# [JSON-Check Document / Customer / Items]-----------------------------------------------------------------------------------------------------------------------[-]


#                                                             [------]                                                                                           [-]


# ---------[     Create XML part       ]----------------------------------------------------------------------------------------------------------------------------------------[+]
        if str(JsonStr['success']) == 'True' and str(JsonStr['content']['result']['pageCond']['count']) > '1':
            self.t_invc_no = self.getInvcNo  # get Invc_No
            logging.info(self.getCurrDatetime() +
                         '  |Note|-------->[Create  XML Document]')  # Append Log
            """ xml_doc = ET.Element('DOCUMENT')
            logging.info("Create Document Tree")
            invoices = ET.SubElement(xml_doc,'INVOICES')
            """
            for docList in JsonStr['content']['result']['list']:

                if docList['dealCode'] in self.CreateDOCList:
                    print(docList['dealCode']+'    :'+'Go to Created XML')

                    getsidstr = self.getsid()  # ---------    Get INVC_SID
                    if self.test_type == 'test':
                        print('Get Invoice SID:'+getsidstr)


# ---------[     Create XML part       ]----------------------------------------------------------------------------------------------------------------------------------------[-]

        self.cur.close
        self.connection.close

        # except BaseException:
        #  print('ERROR:--->TimeOut 15 ')
        #  print(get_respons.text)


# -----Run Main ----------------

API = webservice('101661', '2021-02-05 00:00:00', '2021-02-05 23:59:59')
API.getorder()
