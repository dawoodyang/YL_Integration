import json
with open("APIjson.json",'r') as load_f:
     jsonstr = json.load(load_f)
print(str(jsonstr))