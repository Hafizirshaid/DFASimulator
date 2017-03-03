import requests
url = 'http://dev.smartcone.cs.wmich.edu/rest-services/api/smartCone/1'
data = '{
	"latitude"      : 50.11,
	"longitue"      : 50.11,
	"alertType"     : "construction_zone"
}'
response = requests.post(url, data=data)

