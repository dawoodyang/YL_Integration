import os
import time
def writeToFile(p_str):
    patch = '.\\log\\Api'+str(time.strftime('%y%m%d%H%M%S'))+'.json'
    with open(patch,'w') as wf:
        wf.write(p_str)

writeToFile('testtesttest')