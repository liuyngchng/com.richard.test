#!/usr/bin/python3
import argparse
import json

def prs_up_rpt_cmd(s):
    '''
    parse hex data in auto report command which is part of a frame starting with "0x88"
    :param s: a hex string
    :return: a json string
    '''
    dt = {}
    i=0
    dt['cr']=s[i:i+2]
    i+=2
    dt['ctrl']=s[i:i+2]
    i+=2
    dt['l']=s[i:i+2]
    i+=2
    dt['c']=s[i:i+4]
    i+=4
    dt['r']=s[i:i+4]
    i+=4
    dt['seq']=s[i:i+2]
    i+=2
    seq = int(dt['seq'],16)
    #print('seq', seq)
    if seq >= 0 or seq == int('80', 16):
        #print('hit')
        dt['appId']=s[i:i+46]
        i+=46
    dt['dt']=s[i:]
    return json.dumps(dt);
def prs_dt_u(is_nake_dt, s):
    '''
    parse data union
    :param is_nake_dt: True, not a data union; False, normal data union
    :param s: a hex string
    :return: a json string
    '''
    dt={}
    i=0
    if is_nake_dt:
        #print("nake_dt")
        dt['dt']=s
        return json.dumps(dt);
    dt['ctrl']=s[i:i+2]
    i+=2
    dt['n']=s[i:i+2]
    i+=2
    dt['id']=s[i:i+4]
    i+=4
    dt['dt']=s[i:]
    return json.dumps(dt);
def prs_up_rpt_dt_obj(is_nake_dt,id,s):
    '''
    parse auto report data obj
    :param is_nake_dt: True, not a data union; False, normal data union
    :param id: data object id
    :param s: a hex string
    :return: a json string
    '''
    dt={}
    i=0
    if is_nake_dt and id=='8002':
        dt['ext']=prs_at_ext(is_nake_dt, s[i:])
        return json.dumps(dt);
    dt['time']=s[i:i+12]
    i+=12
    dt['sts']=s[i:i+8]
    i+=8
    dt['iVStd']=s[i:i+16]
    i+=16
    dt['iV']=s[i:i+16]
    i+=16
    dt['fVStd']=s[i:i+16]
    i+=16
    dt['fV']=s[i:i+16]
    i+=16
    dt['flwRtStd']=s[i:i+8]
    i+=8
    dt['flwRt']=s[i:i+8]
    i+=8
    dt['t']=s[i:i+8]
    i+=8
    dt['p']=s[i:i+8]
    i+=8
    dt['engSum']=s[i:i+16]
    i+=16
    dt['engDst']=s[i:i+8]
    i+=8
    dt['acsV']=s[i:i+4]
    i+=4
    dt['vlt']=s[i:i+4]
    i+=4
    dt['pwr']=s[i:i+2]
    i+=2
    dt['nb']=s[i:i+8]
    i+=8
    dt['ecl']=s[i:i+4]
    i+=4
    dt['cid']=s[i:i+12]
    i+=12
    dt['rnf']=s[i:i+4]
    i+=4
    #dt['dt']=s[i:]
    if id == '8003':
        dt['ext']=prs_evt_ext(s[i:])
    elif id == '8002':
        dt['ext']=prs_at_ext(is_nake_dt, s[i:])
    return json.dumps(dt);
def prs_evt_ext(s):
    '''
    parse extend data in up evt report data
    :param s: a hex string
    :return: a json string
    '''
    dt={}
    i=0
    dt['evt']=s[i:i+2]
    i+=2
    dt['fvStdS1']=s[i:i+16]
    i+=16
    dt['dt']=[]
    for j in range(3):
        dt1={}
        dt1['date']=s[i:i+6]
        i+=6
        dt1['fVStd']=s[i:i+16]
        i+=16
        dt1['fV']=s[i:i+16]
        i+=16
        dt['dt'].append(dt1)
    return dt
def prs_at_ext(is_nake_dt, s):
    '''
    parse extend data in up auto report data
    :param s: a hex string
    :return: a json string
    '''
    dt={}
    i=0
    if is_nake_dt:
        dt['dt']=s[i:]
        return dt
    dt['date']=s[i:i+6]
    i+=6
    dt['dt']=s[i:]
    return dt
def prs_frm(s):
    '''
    parse hex data in frame start with "0x68"
    :param s:
    :return:
    '''
    return '';




if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument("-c", "--cmd", type=str,help="输入明文指令hex字符串")
    parser.add_argument("-f", "--frame", type=str, help="输入明文帧hex字符串")

    args = parser.parse_args()
    cmd = args.cmd
    frame = args.frame
    if cmd:
        #print("cmd:", cmd)
        dt=prs_up_rpt_cmd(cmd)
        cmd_obj=json.loads(dt)
        #print("cmd_obj", cmd_obj)
        is_nake_dt = False
        if cmd_obj["ctrl"] == "C2" and cmd_obj["seq"] == "81":
            is_nake_dt = True
        dt=prs_dt_u(is_nake_dt, cmd_obj["dt"])
        obj=json.loads(dt)
        if is_nake_dt:
            obj["id"]='8002'
        dt=prs_up_rpt_dt_obj(is_nake_dt,obj["id"], obj["dt"])
    elif frame:
        #print("frame:", frame)
        json=prs_frm(frame)
    print(dt)
