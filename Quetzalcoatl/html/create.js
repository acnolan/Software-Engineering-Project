/*  This file defines the API integration functions for the create view.
 *  I have no idea how to actually invoke the AWS api, so all the functions
 *  that need to be handled by the cloud will be implemented here, in
 *  accordance with the API specification at swaggerHub. */

var sdate;

function create(organizerId, startTime, endTime, slotLength, startDate, endDate) {
    // Endpoint to create at
    var endpoint = "https://jf0kmiscz8.execute-api.us-east-2.amazonaws.com/qualpha/createschedule";
    // Create the request
    var xhr = new XMLHttpRequest();
    xhr.open('POST', endpoint);
    // Assign the form data
    var id22 = parseInt(get_cookie("token"));
    var name = document.getElementById("schName").value;
    if(name == ""){
        error("please enter a schedule name");
        return;
    }
    var args = {
        'organizerId': id22,
        'startTime': startTime,
        'endTime': endTime,
        'slotLength': slotLength,
        'startDate': startDate,
        'endDate': endDate,
        'id2': id22,
        'name': name
    };
    // Assign the callback handler
    xhr.onload = create_callback;
    // Send the request
    xhr.send(JSON.stringify(args));
    // Notify the user
    plain_message("Loading...");
    // Update
    sdate = startDate;
}

function create_callback(e) {
    // Do a do
    if (this.readyState == 4 && this.status == 200) {
        var data = JSON.parse(this.responseText);
        success(data.response);
        console.log(data);
        const regex = /Successfully created schedule: ([0-9]+)/gm;
        m = regex.exec(data.response);
        console.log(m);
        console.log(sdate);
        // Stringify
        //var date = "" + sdate.getFullYear() + "-" + (sdate.getMonth() + 1) + ("00" + sdate.getDate()).substr(-2,2);
        // Send to the schedule page
        setTimeout(function() {
            location.href = "organizerView.html?id=" + m[1] + "&date=" + sdate;
        }, 1500);
    } else if(this.readyState == 4) {
        error("Bad status " + this.status);
    }
}