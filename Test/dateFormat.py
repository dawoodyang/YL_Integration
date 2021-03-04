import time, datetime

def TimeStampToDateTime(p_TimeStamp):
  p_TimeStamp = p_TimeStamp[0:10]
  timeStamp = int(p_TimeStamp)
  
  timeArray = time.localtime(timeStamp)
  otherStyleTime = time.strftime("%Y-%m-%d %H:%M:%S", timeArray)
  return otherStyleTime

def TimeStampToCRDateTime(p_TimeStamp,p_type):
  p_TimeStamp = p_TimeStamp[0:10]
  timeStamp = int(p_TimeStamp)
  timeArray = time.localtime(timeStamp)
  if p_type =='Create':
     otherStyleTime = time.strftime("%Y-%m-%dT%H:%M:%S", timeArray)
  if p_type =='Modify':
     otherStyleTime = time.strftime("%Y-%m-%dT%H:%M:%S+08:00", timeArray)

  return otherStyleTime


print(TimeStampToCRDateTime('1611716769000','Modify'))