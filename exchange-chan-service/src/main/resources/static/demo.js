var ws_protocol = 'ws'; // ws 或 wss
var port = 8326

var heartbeatTimeout = 5000; // 心跳超时时间，单位：毫秒
var reconnInterval = 1000; // 重连间隔时间，单位：毫秒

var binaryType = 'blob'; // 'blob' or 'arraybuffer';//arraybuffer是字节
var handler = new DemoHandler()

var tiows

function initWs() {
    var ip = document.getElementById('ip').value
    var queryString = null;
    var param = null;
    tiows = new tio.ws(ws_protocol, ip, port, queryString, param, handler, heartbeatTimeout, reconnInterval, binaryType)
    tiows.connect()
}


function send() {
    var msg = document.getElementById('textId')
    tiows.send(msg.value)
}

function clearMsg() {
    document.getElementById('contentId').innerHTML = ''
}