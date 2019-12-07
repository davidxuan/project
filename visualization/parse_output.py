import json

path = "/Users/davidxuan/Desktop/CS578_Project.nosync/visualization/apache-tomcat-8.5.39_acdc_clustered.rsf"

content = []
d = {}

with open(path, 'r') as f:
    data = f.readlines()
    for line in data:
        line = line.strip().split()
        if "orphan" not in line and line[1].replace(".ss", "") not in d and "eclipse" not in line:
            d[line[1].replace(".ss", "")] = [line[2]]
        else:
            d[line[1].replace(".ss", "")].append(line[2])

for k in d.keys():
    tmp = {}
    tmp['name'] = k
    tmp['imports'] = d[k]
    content.append(tmp)

with open("cluster.json", "w") as f:
    json.dump(content, f)
