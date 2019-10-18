/* General JavaScript functions */

// User token
var token = "";

function do_login() {
    // Get the username
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    // Debug
    //alert("Username is " + username + " and password is " + password);
    // "Validate" the credentials - DEBUG ONLY!
    var endpoint = "https://jf0kmiscz8.execute-api.us-east-2.amazonaws.com/qualpha/login";
	var xhr = new XMLHttpRequest();
    xhr.open('POST', endpoint);
    // Assign the form data
    var args = {
        'username': username,
        'password': password
    };
    // Assign the callback handler
    xhr.onload = post_login;
    // Send the request
    food_message();
    xhr.send(JSON.stringify(args));
}

function post_login() {
    // Get the server response
    if(this.readyState == 4 && this.status == 200) {
        var data = JSON.parse(this.responseText);
        // Check result
        console.log(data.userId);
        if(data.userId > 0 && data.httpCode==200) {
            // Save the username and token
            document.cookie = "username=" + data.username;
            document.cookie = "token=" + data.userId;
            document.cookie = "isAdmin=" + data.isAdmin;
            // Relocate page
            success("Login Successful!");
            setTimeout(function() {
                location.href = "mainpage.html";
            }, 1500);
        } else {
            error("Authentication Failure: invalid login");
        }
    } else if(this.readyState == 4) {
        error("Server returned error " + this.status);
        console.log(this.responseText);
    }
}

function do_logout() {
    // Destroy the session cookie
    document.cookie = "username=; expires=Thu, 01 Jan 1970 00:00:00 UTC";
    document.cookie = "token=; expires=Thu, 01 Jan 1970 00:00:00 UTC";
    // Return to the landing page
    location.href = "landing.html";
}

/* Show an error */
function error(text) {
    // Create the error
    var error = document.createElement("div");
    error.className = "message error";
    error.innerHTML = text;
    // Add to the error tray
    document.getElementById("message_tray").appendChild(error);
    // Delete after time
    setTimeout(function() {
        error.parentElement.removeChild(error);
    },3000);
}

/* Show a success */
function success(text) {
    // Create the message
    var goodie = document.createElement("div");
    goodie.className = "message success";
    goodie.innerHTML = text;
    // Add to the message tray
    document.getElementById("message_tray").appendChild(goodie);
    // Delete after time
    setTimeout(function() {
        goodie.parentElement.removeChild(goodie);
    },3000);
}

/* Show a plain message */
function plain_message(text) {
    // Create the message
    var msg = document.createElement("div");
    msg.className = "message";
    msg.innerHTML = text;
    // Add to the message tray
    document.getElementById("message_tray").appendChild(msg);
    // Delete after time
    setTimeout(function() {
        msg.parentElement.removeChild(msg);
    },3000);
}

function food_message() {
    // Create the message
    var msg = document.createElement("div");
    msg.className = "message";
    msg.innerHTML = "This may take a while, click here to order some takeout while you wait";
    msg.addEventListener("click", function(){window.open("https://www.kingcheftakeout.com/", "_blank");});
    // Add to the message tray
    document.getElementById("message_tray").appendChild(msg);
    // Delete after time
    setTimeout(function() {
        msg.parentElement.removeChild(msg);
    },3000);
}

/* Logged in user check */
function logged_in() {
    // Get the login cookie
    console.log(document.cookie);
    var username = get_cookie("username");
    var t = get_cookie("token");
    if(username == "" || t == "") {
        setTimeout(function() {
            error("Login cookie missing D:");
        }, 500);
        console.log(username, t);
    }
    // Update the username
    document.getElementById("_username").innerText = username;
    // Save the token
    token = t;
}

/* Get a cookie */
function get_cookie(name) {
    // Get all cookie data
    var cookies = document.cookie.split(";");
    // Check each cookie
    for(var i = 0; i < cookies.length; i++) {
        var data = cookies[i].split("=", 2);
        console.log(data[0]);
        if(data[0] == name) {
            return data[1];
        } else if((data[0]) == " " +name) {
            return data[1];
        }
    }
    // No cookie :(
    return "";
}

/* Get a url parameter */
function get_urlparam(name) {
    // Get params
    var params = window.location.href.split("?",2);
    if(params.length == 2) {
        params = params[1];
    } else {
        return "";
    }
    // Get all param data
    var params = window.location.href.split("?")[1].split("&");
    // Check each param
    for(var i = 0; i < params.length; i++) {
        var data = params[i].split("=", 2);
        console.log(data[0]);
        if(data[0] == name) {
            return data[1];
        } else if((data[0]) == " " +name) {
            return data[1];
        }
    }
    // No param :(
    return "";
}

function signup(){
    // Get the username
    var username = document.getElementById("username2").value;
    var password = document.getElementById("password2").value;
    // Debug
    //alert("Username is " + username + " and password is " + password);
    // "Validate" the credentials - DEBUG ONLY!
    var endpoint = "https://jf0kmiscz8.execute-api.us-east-2.amazonaws.com/qualpha/signup";
	var xhr = new XMLHttpRequest();
    xhr.open('POST', endpoint);
    // Assign the form data
    var args = {
        'username': username,
        'password': password
    };
    // Assign the callback handler
    xhr.onload = signup_post;
    // Send the request
    food_message();
    xhr.send(JSON.stringify(args));
}

function signup_post(){
    if(this.readyState == 4 && this.status == 200) {
        var data = JSON.parse(this.responseText);
        // Check result
        console.log(data.userId);
        if(data.httpCode==200) {
            setTimeout(
                do_login2(data.username,data.password)
            , 1500);
        } else {
            error("Authentication Failure: invalid login");
        }
    } else if(this.readyState == 4) {
        error("Server returned error " + this.status);
        console.log(this.responseText);
    }
}

function do_login2(username, password) {
    // Get the username
    //var username = document.getElementById("username").value;
    //var password = document.getElementById("password").value;
    // Debug
    //alert("Username is " + username + " and password is " + password);
    // "Validate" the credentials - DEBUG ONLY!
    var endpoint = "https://jf0kmiscz8.execute-api.us-east-2.amazonaws.com/qualpha/login";
	var xhr = new XMLHttpRequest();
    xhr.open('POST', endpoint);
    // Assign the form data
    var args = {
        'username': username,
        'password': password
    };
    
    // Assign the callback handler
    xhr.onload = post_login;
    // Send the request
    setTimeout(function() {
        xhr.send(JSON.stringify(args));
    }, 1500);
}

// Execute the logged in user check after a schmiddle
if ( document.URL.includes("landing.html") ) {
    //do literally nothing
    console.log("Skipping login on landing page");
}else{
    setTimeout(logged_in, 500);
}
