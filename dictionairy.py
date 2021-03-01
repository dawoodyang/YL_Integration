import time
import datetime
import json
class dictionairy:
    key_value = {}
    key_value['token']         = 'DA0FEF30CF4BB5FA8224AC343D618103'
    key_value['_platform_num'] = '101397'
    key_value['timeStamp']     = ''
    key_value['param']         = ''

    def gettimeStamp(self):
        self.key_value['timeStamp'] = str(round(time.time()*1000))
        paramJSON = json.dumps({'date':self.key_value['timeStamp']})
        self.key_value['param'] = paramJSON
        print(str(self.key_value['param']))
        
Run = dictionairy()
Run.gettimeStamp()