import random
run =0
while run<=100:

 sid = '160007'
 x = [random.randint(10,18) for i in range(6)]
 for i in x:
    sid = sid+str(i)
 #print('new sid:'+sid+'length:'+str(len(sid)))
 print('sid:'+sid+' len:'+str(len(sid)))
 run = run +1