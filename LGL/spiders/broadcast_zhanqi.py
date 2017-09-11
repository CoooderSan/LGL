#!/usr/bin/python
# -*- coding: UTF-8 -*-


import traceback
from bs4 import BeautifulSoup

from selenium import webdriver


# 全局变量url
url = 'https://www.zhanqi.tv/'


# 获取房间列表页
def get_broadcast_list(game):
    # 重新输出直播平台搜索关键词
    if game == 'LOL':
        game = 'lol'
    elif game == 'DOTA2':
        game = 'dota2'
    elif game == 'hearthstone':
        game = 'how'
    elif game == 'kingglory':
        game = 'wangzherongyao'


    room_list_url = url+'/games/%s' % game

    try:
        # browser = webdriver.PhantomJS(executable_path="D:\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe")
        browser = webdriver.PhantomJS(executable_path="/root/phantomjs-2.1.1-linux-x86_64/bin/phantomjs")

        browser.get(room_list_url)
        # time.sleep(3)
        soup = BeautifulSoup(browser.page_source, 'lxml')

        browser.quit()
        return soup
    except Exception:
        traceback.print_exc()
        print('获取战旗直播%s分页失败' % game)


# 获取房间列表节点
def get_broadcast_room(game):
    # 解析列表页
    soup = get_broadcast_list(game)
    # 获取房间列表节点
    room_node_list = soup.select('div[data-type="all"] ul li')

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
        room_info['title'] = li.find('span', class_='name').string
        # 获取播主名字
        room_info['broadcaster'] = li.find('span', class_='anchor anchor-to-cut dv').string
        # 获取观看人数
        viewers = li.find('span', class_='dv').string
        # 如果观看人数过万则转化为纯数字格式
        if viewers[-1] == "万":
            viewers = viewers[:-1]
            viewers = int(float(viewers) * 10000)
        else:
            viewers = int(viewers)
        room_info['viewers'] = viewers
        # 获取直播图路径
        room_info['img'] = li.find('img').attrs['src']
        # 获取房间路径
        room_info['roomUrl'] = url + li.find('a', class_='js-jump-link').attrs['href']
        # print(room_info)
        # 将单个房间添加到房间列表中
        if viewers >= 10000:
            room_list.append(room_info)

    # print(room_list)
    return room_list


# def put_data_in_redis():
#
#     r = redis.Redis(host='localhost', port=6379, db=0)
#     r.set('douyu_lol_rooms', get_room_info())
#
#
# if __name__ == "__main__":
#     put_data_in_redis()
    # scheduler = BlockingScheduler()
    # scheduler.add_job(put_data_in_redis, 'cron', minute='*/30')
    #
    # scheduler.start()










