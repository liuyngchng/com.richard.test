#1. ownthink
https://github.com/ownthink/KnowledgeGraphData

ownthink开源了史上最大规模的中文知识图谱，数据是以（实体、属性、值），（实体、关系、实体）混合的形式组织，数据格式采用csv格式，下载链接见文末


https://zhuanlan.zhihu.com/p/43143014
http://deepdive.stanford.edu/quickstart

#2. neo4j
docker pull neo4j

docker run \
    --publish=7474:7474 --publish=7687:7687 \
    --volume=$HOME/neo4j/data:/data \
    neo4j

#3. docx to txt

soffice --headless --convert-to txt 2.docx --outdir 2.txt
g/^\s*$/d
%s/\s//g
%s/\r//g
%s/\n//g
