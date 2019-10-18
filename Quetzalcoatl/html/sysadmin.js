function getSchedules(){
    var searchTime = document.getElementById("view_from_hours").value;
	var endpoint = "https://jf0kmiscz8.execute-api.us-east-2.amazonaws.com/qualpha/sysadminget";
	var xhr = new XMLHttpRequest();
    xhr.open('POST', endpoint);
    // Assign the form data
    var args = {
        'searchTime': searchTime
    };
    // Assign the callback handler
    xhr.onload = get_callback;
    // Send the request
    xhr.send(JSON.stringify(args));
    // Notify the user
    //plain_message("Loading...");
}

//in the html slap a div with id at the bottom and loop to add to it
function get_callback() {
    // Do a do
    if (this.readyState == 4 && this.status == 200) {
        var data = JSON.parse(this.responseText);
        success("Got some schedules for ya");
        console.log(data);
        const regex = /Successfully getted schedules: ([0-9]+)/gm;
        console.log(data.schedules);
        document.getElementById("results").innerHTML = "";
        for(var i=0;i<data.schedules.length;i++){
            var element = document.createElement("div");
            var text = document.createTextNode(data.schedules[i].name+" made by "+data.schedules[i].username);
            element.appendChild(text);
            document.getElementById("results").appendChild(element);
        }
    } else if(this.readyState == 4) {
        error("Bad status " + this.status);
    }
}

function deleteSchedules(){
    var searchTime = document.getElementById("delete_older_than").value;
	var endpoint = "https://jf0kmiscz8.execute-api.us-east-2.amazonaws.com/qualpha/sysadmindelete";
	var xhr = new XMLHttpRequest();
    xhr.open('POST', endpoint);
    // Assign the form data
    var args = {
        'oldDate': searchTime
    };
    // Assign the callback handler
    xhr.onload = delete_callback;
    // Send the request
    xhr.send(JSON.stringify(args));
    // Notify the user
    //plain_message("Loading...");
}

function delete_callback(){
    if (this.readyState == 4 && this.status == 200) {
        var data = JSON.parse(this.responseText);
        success(data.rowsUpdated + " rows deleted!");
        console.log(data);
        const regex = /Successfully deleted schedules: ([0-9]+)/gm;
        m = regex.exec(data.response);
        console.log(m);
        // Send to the schedule page
    } else if(this.readyState == 4) {
        error("Bad status " + this.status);
    }
}