# -*- coding:utf-8 -*-
import requests
import json
import random
import string
import time


def get(url):
    resp = requests.get(url, timeout=10)  # time out 10 seconds
    print(resp.content, type(resp.content))    # 返回字节形式
    #data = json.loads(resp.content)
    # print(data)
    #return json.dumps(data)
    return resp.content


def post(url, json, headers):
    '''
        post json data as -raw with headers=headers
        curl -X POST 'url' -H 'header_key1: header_value1' -H 'header_key2: header_value2' --data-raw '{"k1":"v1"}'
    '''
    resp = requests.post(url=url, json=json, headers=headers);
    resp.encoding='UTF-8'
    return str(resp.content, 'UTF-8')
    

def get_page(token, page_index):
    url = 'http://my.domain/api/biz/contact/find/page?token='+token
    print("url=", url)
    headers = {"Accept": "application/json","Accept-Encoding": "gzip, deflate","Accept-Language": "en-US,en;q=0.9","Connection": "keep-alive","Content-Type": "application/json;charset=UTF-8","Host": "my.domain","Origin": "http://my.domain","Referer": "http://my.domain/","User-Agent": "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36"
    }
    json={
        "parentId":0,
        "page":{"pageIndex":page_index,"pageSize":100,"orderBy":"sort","orderType":"desc"}
    }
    print("req json {0}".format(json))
    data = post(url, json, headers);
    return data

def crawl_data(token, start_page):
    fileName='c.dat'
    with open(fileName,'a') as file:
        for i in range(start_page, 57):
            a=random.randint(3,6)
            print("request page {0}, sleep {1}s".format(i ,a))
            time.sleep(a)
            data = get_page(token,i)
            file.write("{0}\n".format(data))


if __name__ == "__main__":
    # 这个值需要用自己账号登录后，浏览器按F12进入debug模式，点击网络(Network)查看获取
    token = 'test'
    # 启始页，从 0  开始，如果中间任务终端，可以设置该值，实现断点请求
    start_page = 0
    crawl_data(token, 0)
