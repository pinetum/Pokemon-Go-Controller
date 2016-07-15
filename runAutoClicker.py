import os
import urllib2
import json
import time

def checkConnected():
	try:
		response = urllib2.urlopen("http://192.168.0.14:8888", timeout = 1)
		return json.load(response)
	except urllib2.URLError as e:
		print e.reason

def clickAction():
	os.system("./autoClicker -x 1000 -y 756")
	time.sleep(1)
	os.system("./autoClicker -x 1000 -y 813")
	
	print "clicking!!"

def start():
	while True:
		if checkConnected() != None:
			clickAction()

start()