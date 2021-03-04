from ctypes import *
pDll = CDLL("E:/Dep_Object/YL_ZT/SIDGenerator64.dll")
newsid = c_char_p()
result = pDll.GetRandomSID(byref(newsid),'24')
getsid = '16'+str(newsid).replace('c_char_p(','').replace(')','')[0:17]
print('Get RetailPro SID: [ '+getsid+' ] len : '+str(len(str(getsid))))







