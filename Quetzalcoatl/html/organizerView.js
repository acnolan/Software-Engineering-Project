/*  This file defines the API integration functions for the organizer view.
 *  I have no idea how to actually invoke the AWS api, so all the functions
 *  that need to be handled by the cloud will be implemented here, in
 *  accordance with the API specification at swaggerHub. */

// = READ FUNCTIONS = 

/*!
 *  Retrieve slots for the given from AWS.
 *
 *  Once data is loaded, it should be passed to `parse()` as an array.
 *
 *  @param date The week requested contains this date
 * 
 *  @return nothing
 */
function show_week_schedule(id, startdate, enddate) {
    // Endpoint to create at
    var endpoint = "https://jf0kmiscz8.execute-api.us-east-2.amazonaws.com/qualpha/getschedule";
    // Create the request
    var xhr = new XMLHttpRequest();
    xhr.open('POST', endpoint);
    // Assign the form data
    var args = {
        'id2': id,
        'userid': token,
        'startTime': startdate.getFullYear() + "-" + (startdate.getMonth() + 1) + "-" + (startdate.getDate() + 1) + " " + startdate.getHours() + ":" + startdate.getMinutes() + ":" + startdate.getSeconds(),
        //startTime': startdate.getTime(),
        'endTime': enddate.getFullYear() + "-" + (enddate.getMonth() + 1) + "-" + (enddate.getDate() + 1) + " " + enddate.getHours() + ":" + enddate.getMinutes() + ":" + enddate.getSeconds()
    };
    // Assign the callback handler
    xhr.onload = callback_show_week_schedule;
    // Send the request
    xhr.send(JSON.stringify(args));
    // Notify the user
    plain_message("Loading...");
}

function callback_show_week_schedule() {
    // Do a do
    if (this.readyState == 4 && this.status == 200) {
        var data = JSON.parse(this.responseText);
        console.log(data);
        //success(data.response);
        // Save the info
        sched_data = data;
        // Extract the schedule data
        parse(sched_data.slots, sched_data.slotlength);
        
        var text2 = document.createTextNode("or give this code to people you would like to add: "+sched_data.participationCode);
        document.getElementById("partDiv").innerHTML = "";
        document.getElementById("partDiv").appendChild(text2);
    } else if(this.readyState == 4) {
        error("Bad status " + this.status);
    }
}

function old_show_week_schedule(date) {
    // Get the actual data and then pass it
    var data = [
        {
            "id": "test0",
            "startTime": 600,
            "startDate": {
                "day": 26,
                "mon": 11,
                "year": 2018
            },
            "isBlocked": true
        },
        {
            "id": "test1",
            "startTime": 630,
            "startDate": {
                "day": 26,
                "mon": 11,
                "year": 2018
            },
            "isBlocked": false
        },
        {
            "id": "test2",
            "startTime": 660,
            "startDate": {
                "day": 26,
                "mon": 11,
                "year": 2018
            },
            "isBlocked": true
        },
        {
            "id": "test3",
            "startTime": 690,
            "startDate": {
                "day": 26,
                "mon": 11,
                "year": 2018
            },
            "isBlocked": true
        },
        {
            "id": "test4",
            "startTime": 630,
            "startDate": {
                "day": 27,
                "mon": 11,
                "year": 2018
            },
            "isBlocked": true
        },
        {
            "id": "test5",
            "startTime": 600,
            "startDate": {
                "day": 30,
                "mon": 11,
                "year": 2018
            },
            "isBlocked": true
        }
    ];
    // Call parse with the data once you've got it.
    parse(data);
    //parse([]);
}

function callback_refresh_schedule() {
    // Do a do
    if (this.readyState == 4 && this.status == 200) {
    	setup();
    } else if(this.readyState == 4) {
        error("Bad status " + this.status);
    }
}

function open_time_slot(id) {
	// Generate request to server from slots
	var serverRequest = {};
	serverRequest["isClose"] = false;
	serverRequest["slotids"] = id;

    // Endpoint to create at
    var endpoint = "https://jf0kmiscz8.execute-api.us-east-2.amazonaws.com/qualpha/toggleslots";
    // Create the request
    var xhr = new XMLHttpRequest();
    xhr.open('POST', endpoint);
    // Create the JSON object
    var args =	JSON.stringify(serverRequest); 
    // Assign the callback handler
    xhr.onload = callback_refresh_schedule;
    // Send the request
    xhr.send(args);
    // Notify the user
    //plain_message("Loading...");
}

function close_time_slot(id) {
	// Generate request to server from slots
	var serverRequest = {};
	serverRequest["isClose"] = true;
	serverRequest["slotids"] = id;

    // Endpoint to create at
    var endpoint = "https://jf0kmiscz8.execute-api.us-east-2.amazonaws.com/qualpha/toggleslots";
    // Create the request
    var xhr = new XMLHttpRequest();
    xhr.open('POST', endpoint);
    // Create the JSON object
    var args =	JSON.stringify(serverRequest); 
    // Assign the callback handler
    xhr.onload = callback_refresh_schedule;
    // Send the request
    xhr.send(args);
    // Notify the user
    //plain_message("Loading...");
}

function delete_sch(id2) {
	// Generate request to server from slots
	var serverRequest = {};
	serverRequest["id2"] = id2;

    // Endpoint to create at
    var endpoint = "https://jf0kmiscz8.execute-api.us-east-2.amazonaws.com/qualpha/deleteschedule";
    // Create the request
    var xhr = new XMLHttpRequest();
    xhr.open('POST', endpoint);
    // Create the JSON object
    var yeet=Number(get_urlparam("id"));
    var args =	{
        "id2": yeet
    }//JSON.stringify(serverRequest); 
    // Assign the callback handler
    //xhr.onload = callback_refresh_schedule;
    // Send the request
    xhr.send(JSON.stringify(args));
    // Notify the user
    //plain_message("Loading...");
}

function loadUsers(){
    //var id = parseInt(get_cookie("token"));
	var endpoint = "https://jf0kmiscz8.execute-api.us-east-2.amazonaws.com/qualpha/getallusers";
	var xhr = new XMLHttpRequest();
    xhr.open('POST', endpoint);
    // Assign the form data
    var args = {};
    // Assign the callback handler
    xhr.onload = loadUserPost;
    // Send the request
    xhr.send(JSON.stringify(args));
}

function loadUserPost(){
    if (this.readyState == 4 && this.status == 200) {
        var data = JSON.parse(this.responseText);
        //success("Got some schedules for ya");
        console.log(data);
        const regex = /Successfully getted schedules: ([0-9]+)/gm;
        console.log(data.names);
        //document.getElementById("organized").innerHTML = "";
        document.getElementById("toAdd").innerHTML="";
        for(var i=0;i<data.names.length;i++){
            var element = document.createElement("option");
            var text = document.createTextNode(data.names[i]);
            element.setAttribute('value',data.names[i])
            console.log(data.names[i]);
            element.appendChild(text);
            document.getElementById("toAdd").appendChild(element);
        }
    } else if(this.readyState == 4) {
        error("Bad status " + this.status);
    }
}

function add_participant(){
	var endpoint = "https://jf0kmiscz8.execute-api.us-east-2.amazonaws.com/qualpha/addparticipant";
	var xhr = new XMLHttpRequest();
    xhr.open('POST', endpoint);
    // Assign the form data
    var name = document.getElementById("toAdd").value;
    var schId = Number(get_urlparam("id"));
    var args = {
        "name": name,
        "schId": schId
    };
    // Assign the callback handler
    xhr.onload = add_part_post;
    // Send the request
    xhr.send(JSON.stringify(args));
}

function add_part_post(){
    if(this.readyState == 4 && this.status == 200) {
        var data = JSON.parse(this.responseText);
        success(data.name + " is now participating in this schedule!");
    } else if(this.readyState == 4) {
        error("Server returned error " + this.status);
        console.log(this.responseText);
    }
}

function update_bounds(){
    var endpoint = "https://jf0kmiscz8.execute-api.us-east-2.amazonaws.com/qualpha/extenddates";
	var xhr = new XMLHttpRequest();
    xhr.open('POST', endpoint);
    // Assign the form data
    var date = document.getElementById("bound_update_date").value;
    var schId = Number(get_urlparam("id"));
    var args = {
        "scheduleID": schId,
        "modTime": date
    };
    // Assign the callback handler
    xhr.onload = update_bounds_post;
    // Send the request
    xhr.send(JSON.stringify(args));
}

function update_bounds_post(){
    if(this.readyState == 4 && this.status == 200) {
        var data = JSON.parse(this.responseText);
        success("Schedule is extended now! (Assuming you chose a date that is outside your current schedule)");
        setup();
        document.getElementById("dcmodal").setAttribute("onscreen","false");
    } else if(this.readyState == 4) {
        error("Server returned error " + this.status);
        console.log(this.responseText);
    }
}

