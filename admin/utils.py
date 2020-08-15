from datetime import datetime, date
import time
import os

def generateID(userID=None):
    '''
    generate id following datetime
    '''
    now = datetime.now()
    id = "%04d%02d%02d%02d%02d%02d%06d" % (now.year, now.month, now.day, now.hour, now.minute, now.second, now.microsecond)
    if userID != None:
        id = str(userID) + id
    time.sleep(0.0000001)
    return id

def getNow():
    '''
    get current date
    '''
    today = date.today()
    return str(today.strftime("%B %d, %Y"))

def cls():
    '''
    clear console
    '''
    os.system('cls' if os.name=='nt' else 'clear')