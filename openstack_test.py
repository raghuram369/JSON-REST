#!/usr/bin/python
import pycurl
from StringIO import StringIO
import json
def server_info():
	global tenant_id
	global token_id
	global stack_name
	global vm_name
	global tenant_name
	string = 'X-Auth-Token: ' +token_id
	print token_id
	URL = 'https://public.fuel.local:8774/v2/'+tenant_id+'/servers/detail'
	buffer = StringIO()
	c1 = pycurl.Curl()
	#c1.setopt(c1.URL, 'https://public.fuel.local:8774/v2/a3bd570fc43b42569e269f6fae065eb4/servers/detail')
	c1.setopt(c1.URL, URL)
	c1.setopt(c1.HTTPHEADER, [string])
	c1.setopt(c1.SSL_VERIFYPEER,False)
	c1.setopt(c1.WRITEDATA, buffer)
	c1.perform()
	c1.close()
	body2 = buffer.getvalue()
	server_json = json.loads(body2)
	server = server_json["servers"]
	for element in  server:
		metadata =  element["metadata"]
		if metadata:
			stack = metadata["metering.stack"]
			vm = element["name"]
			alarm_info(stack, vm)
def alarm_info(stack , vm):
	global tenant_id
	global stack_name
	global token_id
	global vm_name
	global data
	buffer = StringIO()
	c2 = pycurl.Curl()
	c2.setopt(c2.URL, 'https://public.fuel.local:8777/v2/alarms')
	string = 'X-Auth-Token: ' +token_id
	c2.setopt(c2.HTTPHEADER, [string])
	c2.setopt(c2.SSL_VERIFYPEER,False)
	c2.setopt(c2.WRITEDATA, buffer)
	c2.perform()
	c2.close()
	body1 = buffer.getvalue()
	full_jsonn = json.loads(body1)
	for element in full_jsonn:
		alarm = element 
		threshold = alarm["threshold_rule"]
		alarm_name = element["name"]
		query = threshold["query"]
		if  query:
			json_query = query[0]
			name = json_query["value"]
			if name == stack:
				data['stack_name'] = stack
				data['vm_name'] = vm
				data['alarm_name'] = alarm_name
				list.append(data)
				data = {}
def main():
	buffer = StringIO()
	global tenant_id
	global token_id
	global stack_name
	global vm_name
	global list
	global data
	stack_name = ""
	vm_name = ""
	list = []
	data = {}
		
	print "Please enter tenant_id :"
	tenant_id = raw_input()
	c = pycurl.Curl()
	url = "'https://public.fuel.local:5000/v2.0/tokens'"
	c.setopt(c.URL, 'https://public.fuel.local:5000/v2.0/tokens')
	c.setopt(c.HTTPHEADER, ['Content-Type: application/json'])
	c.setopt(c.POSTFIELDS,'{"auth": {"tenantName": "'"cchalla"'", "passwordCredentials": {"username": "'"vinodp"'", "password": "'"Test@123"'"}}}')
	c.setopt(c.SSL_VERIFYPEER,False)
	c.setopt(c.WRITEDATA, buffer)
	c.perform()
	c.close()
	body = buffer.getvalue()
	full_json = json.loads(body)
	access = full_json["access"]
	token = access["token"]
	token_id = token['id']
	server_info()

if __name__ == '__main__':
	main()
	print json.dumps(list)
