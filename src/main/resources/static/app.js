var ws;
var domainWebsocket;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    $("#send").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
	document.cookie = 'cookieParam=' + $("#cookieParam").val();
	
	urlWebsocket = $("#urlWebsocket").val();
	resourceWebsocket = $("#resourceWebsocket").val();
	protocol = 'ws://';
	if (urlWebsocket.startsWith('ws://')
			|| urlWebsocket.startsWith('wss://')) {
		protocol = '';
	}
	slash = '/';
	if (resourceWebsocket.startsWith('/')) {
		slash = '';
	}
	uri = protocol + urlWebsocket + slash + resourceWebsocket;
	
	queryParam = $("#queryParam").val();
	
	if (queryParam) {
		uri += '?' + queryParam;
	}
	console.log('Connecting in ' + uri);
	ws = new WebSocket(uri);
	ws.onmessage = function(data){
		showGreeting(data.data);
	}
	setConnected(true);
	console.log('Connected');
}

function disconnect() {
	if (ws != null) {
        ws.close();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
	var data = JSON.stringify({'name': $("#name").val()})
    ws.send(data);
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});

