#!/usr/bin/python
# -*- coding: UTF-8 -*-

import traceback
import urllib.request
import global_list

from bs4 import BeautifulSoup

# 全局变量url
url = 'https://www.panda.tv/'


# 获取房间列表页
def get_broadcast_list(game):
    # 重新输出直播平台搜索关键词
    if game == 'LOL':
        game = 'lol'
    elif game == 'DOTA2':
        game = 'dota2'
    elif game == 'hearthstone':
        game = 'hearthstone'
    elif game == 'kingglory':
        game = 'kingglory'

    room_list_url = url+'cate/%s' % game
    try:
        # 获取熊猫房间列表页面
        req = urllib.request.Request(room_list_url)
        # 添加请求头
        for i in global_list.HEADERS:
            req.add_header(i, global_list.HEADERS[i])

        web_page = urllib.request.urlopen(req)
        data = web_page.read()
        data = data.decode('UTF-8')
        return data
    except Exception:
        traceback.print_exc()
        print('获取熊猫直播%s分页失败' % game)


# 获取房间列表节点
def get_broadcast_room(game):
    # 解析列表页
    soup = BeautifulSoup(get_broadcast_list(game), 'lxml')
    # 获取房间列表节点
    room_node_list = soup.select('#sortdetail-container li[class="video-list-item video-no-tag video-no-cate "]')
    return room_node_list


# 获取房间信息
def get_room_info(game):
    room_node_list = get_broadcast_room(game)

    # 房间列表
    room_list = []

    # 循环每个房间节点组建房间基本信息
    for li in room_node_list:
        # 房间信息dict
        room_info = {}
        # soup = BeautifulSoup(li, 'lxml')
        # 获取房间名称
        room_info['title'] = li.find('span', class_='video-title').string
        # 获取播主名字
        room_info['broadcaster'] = li.find('span', class_='video-nickname').text.strip()
        # 获取观看人数
        viewers = li.find('span', class_='video-number').string
        # 如果观看人数过万则转化为纯数字格式
        if viewers[-1] == "万":
            viewers = viewers[:-1]
            viewers = int(float(viewers) * 10000)
        else:
            viewers = int(viewers)
        room_info['viewers'] = viewers
        # 获取直播图路径
        room_info['img'] = li.find('img').attrs['data-original']
        # 获取房间路径
        room_info['roomUrl'] = url + li.find('a', class_='video-list-item-wrap').attrs['href']
        # print(room_info)
        # 将单个房间添加到房间列表中
        if viewers >= 10000:
            room_list.append(room_info)

    print(room_list)
    return room_list


# def put_data_in_redis():
#     r = redis.Redis(host='localhost', port=6379, db=0)
#     r.set('douyu_lol_rooms', get_room_info())
#
# if __name__ == "__main__":
#     put_data_in_redis()
#     scheduler = BlockingScheduler()
#     scheduler.add_job(put_data_in_redis, 'cron', minute='*/30')
#
#     scheduler.start()