import hashlib
x = 11
y = 1
#这里可以调节挖矿难度，也就是哈希的长度
while hashlib.sha256(f'{x*y}'.encode("utf-8")).hexdigest()[7:7]!="00":
  print(x*y)
  y +=1
print("找到了：",(x*y))