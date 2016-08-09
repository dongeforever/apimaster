## api master
### a web tool for managing and testing your restful api
* add descriptions for rest api, so your partner can easily understand it.
* execute via the http, and show the result 
* assert the result
* inject the result into next api's parameter, so the testing can be continueous
* support both unit test and integrating test

##使用方式一：编写配置文件直接使用
```json
{
    "caseId": 3,
    "method": "POST",
    "url": "http://123.57.0.49:8004/waybill/{waybillId}",
    "pathVariables": {
        "waybillId": 1
    },
    "headers": {
        "authorization": "HC 13141023040:1daDCUUS+kXkZIOEnjnWx0dUrSo2B0Tq+w8cmWJd36Q=",
        "X-HC-Lng": "116.48278",
        "X-HC-Lat": "39.997786",
        "X-HC-Coordinate-Time": "1465985708000"
    },
    "requestBodyType": "FORM",
    "responseBodyType": "JSON",
    "requestBody": {
        "state": "ACCEPT",
        "force": 1
    },
    "assertions": [
        {
            "name": "status",
            "value": "response.status / 100 == 2",
            "type": "JSON"
        }
    ],
    "injects": [
        {
            "fromUnitId": 0,
            "injectExp": {
                "value": "request.header.\"X-HC-Coordinate-Time\" = timestamp",
                "type": "JSON"
            }
        },
        {
            "fromUnitId": 2,
            "injectExp": {
                "value": "request.path.waybillId = response.body[0].waybills[0].waybillId",
                "type": "JSON"
            }
        }
    ]
}
```

## 使用方式二：部署成web应用，稍后更新
