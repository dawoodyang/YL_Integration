import requests
import json
import math
import hashlib
import time
import datetime
import cx_Oracle as cx
import random 
import xml.etree.ElementTree as ET  # XML Create
import logging    #Log object


class webservice:
# [Class Setup info]---------------------------------------------------------------------------------------------------[+]
    url_token = 'http://testpicksys.retailo2o.com/erpExternal/api/comm/getToken.do?_platform_num='
    url_getorder = 'http://testpicksys.retailo2o.com/picksysdi/api/orderOperate/getPageRetailProInfo.do'
    t_token = ''
    t_platform_num = ''
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

    CreateDOCList = []  # save Doc_No in check time,if document can input
    errorlist = []  # test
    dns = cx.makedsn('192.168.10.27', '1521', 'rproods')  # setup Oracle info
    connection = cx.connect('reportuser', 'report',
                            dns)  # connection to Oracle
    cur = connection.cursor()    # Create Cursor for select run
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
#---[Oracle]--------------------------------------------------------[Check Part]------------------------------------------[+]


    def itemCheck(self, p_itemcode):

        self.cur.execute(
            "select item_sid,local_upc,description2,text3 from cms.INVN_SBS where ALU="+p_itemcode)
        if self.cur.fetchone != None:
           result = list(self.cur.fetchone())
        else:
           result = []

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


#---[Oracle]--------------------------------------------------------[Check Part]------------------------------------------[-]

    def getsid(self):
        sid = '16'
        x = [random.randint(10, 18) for i in range(8)]
        for i in x:
            sid = sid+str(i)
        return sid

    def TimeStampToDateTime(self,p_TimeStamp,p_type):
         p_TimeStamp = p_TimeStamp[0:10]
         timeStamp = int(p_TimeStamp)
  
         timeArray = time.localtime(timeStamp)
         if p_type =='Create':
            otherStyleTime = time.strftime("%Y-%m-%dT%H:%M:%S", timeArray)
         if p_type =='Modify':
            otherStyleTime = time.strftime("%Y-%m-%dT%H:%M:%S+08:00", timeArray)

         return otherStyleTime 


    def gettoken(self):
         get_respons = requests.get(self.url_token+self.t_platform_num)
         self.t_token = json.loads(get_respons.text).get(
             'content').get('result')
         print('---------   Get Token   ---------')
         print('Token = '+self.t_token)


    def getorder(self):
         # ----- Http Headers ----------- +
         headers = {
                   "Content-Type": "application/json",
                   "Accept": "*/*",
                   "Accept-Encoding": "gzip,deflate,br",
                   "Connection": "keep-alive"
                    }
         # ----- Http Headers ----------- -

         # ----- Set Base Info ---------- +
         self.t_timeStamp = str(round(time.time()*1000))
         self.key_value['_platform_num'] = self.t_platform_num
         self.key_value['token'] = self.t_token
         self.key_value['timeStamp'] = self.t_timeStamp
         paramJSON = {'_platform_num': self.t_platform_num, 'pageIndex': 1, 'pageSize': 10,
             'createTimeFrom': self.t_startDate, 'createTimeTo': self.t_endDate, 'deptCode': 8888}

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
           JsonStr = json.loads(get_respons.text)
           print('success:'+str(JsonStr['success']))
           print('PageCount:'+str(JsonStr['content']
                 ['result']['pageCond']['count']))
           # -------Get JSON Str---------------------

# [JSON-Check Document / Customer / Items]-----------------------------------------------------------------------------------------------------------------------[+]
       # -------Loop List -----------------------(+)
           print('------- Get Document ------->')
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
                   print('Document Is Old '+str(str_checkinvc))
               # ------------------------Header-------------------------[+]

               print('------------------Lines------------------------')
               if str_checkinvc == None:
                 for docDetail in docList['details']:

                    print('[Lines]--------->   DocumentNo:'+docList['dealCode']+'   ITEM NO:'+docDetail['goodsName']+'  Name:' +
                          docDetail['barCode']+'   Quantity:'+str(docDetail['amount'])+'  Price:' + str(docDetail['tradePrice']))
                 # ------ Check Item ---------------------------------[+]
                    str_checkitem = self.itemCheck(docDetail['goodsName'])
                    if str_checkitem != '':
                       print('Item Sid:'+str(str_checkitem))
                       print('List 0:'+str(str_checkitem[3]))
                    else:
                       print('Item Not Found!')
                       documentError = documentError + 1
                  # ------- Check if document not error then insert to created list -------[+]
                 if documentError == 0:
                    self.CreateDOCList.append(docList['dealCode'])
                    print(docList['dealCode']+' will Created')
                  # -----------------------------------------------------------------------[-]

                  # ------ Check Item ---------------------------------[-]
                
        # -------Loop List -----------------------(-)
# [JSON-Check Document / Customer / Items]-----------------------------------------------------------------------------------------------------------------------[-]           



#                                                             [------]                                                                                           [-]



#---------Create XML part----------------------------------------------------------------------------------------------------------------------------------------[+]
         #--- Check invc_sid if can use  ------------------[+]       
         getsidstr  = self.getsid()
         invc_check = self.invc_sid_check(getsidstr)
         if invc_check == None :
            print('sid can use')
         else:
            print('sid exist:'+str(invc_check))
         #--- Check invc_sid if can use  ------------------[-]  

#---------Create XML part----------------------------------------------------------------------------------------------------------------------------------------[-]


         self.cur.close
         self.connection.close

         # except BaseException:
         #  print('ERROR:--->TimeOut 15 ')
         #  print(get_respons.text)

    
         



  
# -----Run Main ----------------

API = webservice('101397','2020-01-01 00:00:00','2021-03-01 23:59:59')
API.gettoken()
API.getorder()
