#!/usr/bin/python
# -*- coding: UTF-8 -*-

import redis
import global_list
from spiders import broadcast_douyu, broadcast_huya, broadcast_panda, broadcast_zhanqi

from apscheduler.schedulers.blocking import BlockingScheduler


# 将爬出的该游戏在所有平台上的房间放入redis
def put_data_in_redis(game, live_list):
    r = redis.Redis(host='127.0.0.1', port=6379, db=0)
    r.set('%s_rooms' % game, live_list)


# 将直播房间放入数据库
def load_live_list():
    # 循环GLOBAL_GAME内所有游戏
    for game in global_list.GLOBAL_GAME:
        # 汇集该游戏在所有平台上的直播房间
        live_list = []
        live_list.append(broadcast_zhanqi.get_room_info(game))
        live_list.append(broadcast_panda.get_room_info(game))
        live_list.append(broadcast_huya.get_room_info(game))
        live_list.append(broadcast_douyu.get_room_info(game))

        print(live_list)
        # 放入redis中
        put_data_in_redis(game, live_list)


# 定时任务
def scheduler_tasks():
    # 先执行一遍
    load_live_list()

    # 通过定时任务来持续刷新房间列表，每30分钟一次
    scheduler = BlockingScheduler()
    scheduler.add_job(load_live_list, 'cron', minute='*/15')

    scheduler.start()

scheduler_tasks()


