import json
with open("api.config",'r') as load_f:
     jsonstr = json.load(load_f)
print(jsonstr['test_type'])
