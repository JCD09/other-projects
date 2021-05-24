"use strict";
// Client-side interactions with the browser.

// Make connection to server when web page is fully loaded.
// var socket = io.connect();
// $(document).ready(function() {
// 	socket.on('commandReply', function(result) {
// 		var newDiv = $('<div></div>').text(result);
// 		$('#messages').append(newDiv);
// 		$('#messages').scrollTop($('#messages').prop('scrollHeight'));
// 	});
	
// });

// function sendPrimeCommand(message) {
// 	socket.emit('prime', message);
// };


"use strict";
// Client-side interactions with the browser.

// Make connection to server when web page is fully loaded.
var socket = io.connect();
$(document).ready(function() {
	socket.on('commandReply', function(result) {
		var newDiv = $('<div></div>').text(result);
		$('#messages').append(newDiv);
		$('#messages').scrollTop($('#messages').prop('scrollHeight'));
	});
	
});

function sendPrimeCommand(message) {
	socket.emit('prime', message);
};

function send_message(msg_type, message){
	socket.emit('beatbox', msg_type+' '+message.toString()+' ');
}

function adjust_value(mode, value){
	var html_element = "label[for="+mode+"]"
	$(html_element)[1].innerHTML=value;
}