import datetime
import time
hours = int(1)
startTime = time.time() - hours*60*60
startTime = time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(startTime))
print(startTime)