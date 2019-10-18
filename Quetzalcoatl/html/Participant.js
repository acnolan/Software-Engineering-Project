function bookMeeting(){
    // Get the selected thing
    var selected = document.querySelector("table.calendar td[selected='true']");
    if(selected == null) {
        error("You must select a slot to book!");
        console.log('beep');
        return;
    }
    var endpoint = "https://jf0kmiscz8.execute-api.us-east-2.amazonaws.com/qualpha/bookmeeting";
    var xhr = new XMLHttpRequest();
    xhr.open('POST', endpoint);
    var slot_id = selected.getAttribute("slot_id");
    var part_id = parseInt(get_cookie("token"));
    var args = {
        'slotID': slot_id,
        'participantID': part_id
    }
    xhr.onload = bookMeeting_post;
    // Send the request
    xhr.send(JSON.stringify(args));
    // Notify the user
    plain_message("Loading...");
    // Deselect
    selected.setAttribute("selected","false");
}

function bookFilteredMeeting(event){
	eventid = event.target.getAttribute("meetingid");
    var endpoint = "https://jf0kmiscz8.execute-api.us-east-2.amazonaws.com/qualpha/bookmeeting";
    var xhr = new XMLHttpRequest();
    xhr.open('POST', endpoint);
    var part_id = parseInt(get_cookie("token"));
    var args = {
        'slotID': eventid,
        'participantID': part_id
    }
    xhr.onload = bookFilteredMeeting_post;
    // Send the request
    xhr.send(JSON.stringify(args));
}

function bookMeeting_post(){
    if (this.readyState == 4 && this.status == 200) {
        success("Meeting Booked!");
        setup();
    } else if(this.readyState == 4) {
        error("Bad status " + this.status);
    }
}

function bookFilteredMeeting_post(){
    if (this.readyState == 4 && this.status == 200) {
        success("Meeting Booked!");
        hide_filter_results();
        setup();
    } else if(this.readyState == 4) {
        error("Bad status " + this.status);
    }
}

function cancel(){
    // Get the selected thing
    var selected = document.querySelector("table.calendar td[selected='true']");
    if(selected == null) {
        error("You must select a slot to cancel!");
        console.log('beep');
        return;
    }
    if(selected.className!="reserved"){
        error("You can only cancel your own meeting");
        return;
    }
    var endpoint = "https://jf0kmiscz8.execute-api.us-east-2.amazonaws.com/qualpha/bookmeeting";
    var xhr = new XMLHttpRequest();
    xhr.open('POST', endpoint);
    var slot_id = selected.getAttribute("slot_id");
    var part_id = 0;
    var args = {
        'slotID': slot_id,
        'participantID': part_id
    }
    xhr.onload = bookMeeting_post2;
    // Send the request
    xhr.send(JSON.stringify(args));
    // Notify the user
    plain_message("Loading...");
    // Deselect
    selected.setAttribute("selected","false");
}

function bookMeeting_post2(){
    if (this.readyState == 4 && this.status == 200) {
        success("Meeting Cancelled!");
        setup();
    } else if(this.readyState == 4) {
        error("Bad status " + this.status);
    }
}

function filterSetup(){
	console.log("filter setup");
	var endpoint = "https://jf0kmiscz8.execute-api.us-east-2.amazonaws.com/qualpha/getslots";
	var xhr = new XMLHttpRequest();
    xhr.open('POST', endpoint);
    // Assign the form data
    var id2 = Number(get_urlparam("id"));
    var args = {
        "id2": id2
    };
    // Assign the callback handler
    xhr.onload = filter_setup_get_slots;
    // Send the request
    console.log(JSON.stringify(args))
    xhr.send(JSON.stringify(args));
}

function filter_setup_get_slots(){
    if (this.readyState == 4 && this.status == 200) {
        var indata = JSON.parse(this.responseText);
        console.log(indata);
        var data = indata.slots;
        console.log(data);
        //var sched_id = 0;
        //var yearmin = 0;
      //  var yearmax = 0;
    //    var monthmin = 0;
  //      var monthmax = 0;
//        var dayofwmin = 0;
//          var dayofwmax = 0;
//            var dayofmmin = 0;
//              var dayofmmax = 0;
//        var timeslotmin = 0;
//          var timeslotmax = 0;
//        var slotinterval = 0;
//"startdate":"2018-11-29","enddate":"2018-11-29","starttime":300,"endtime":1020,"slotlength":15,
//"slots": {"id":6723,"scheduleid":63,"datetime":"Nov 20, 2018 5:30:00 AM","bookedby":0}
//
		// Find filter limits
        for(var i = 0; i < data.length; i++) {
        	var date = new Date(data[i].datetime);
        	if(!years.includes(date.getFullYear())){
        		years.push(date.getFullYear());
        	}
        	if(!months.includes(date.getMonth())){
        		months.push(date.getMonth());
        	}
        	if(!dayofw.includes(date.getDay()) && (date.getDay() != 0) && (date.getDay() != 6)){
        		dayofw.push(date.getDay());
        	}
        	if(!dayofm.includes(date.getDate()) && (date.getDay() != 0) && (date.getDay() != 6)){
        		dayofm.push(date.getDate());
        	}
        	if(!timeslots.includes(date.getHours()*60 + date.getMinutes())){
        		timeslots.push(date.getHours()*60 + date.getMinutes());
        	}
        }
        years.sort(function(a, b){return a - b});
        months.sort(function(a, b){return a - b});
        dayofw.sort(function(a, b){return a - b});
        dayofm.sort(function(a, b){return a - b});
        timeslots.sort(function(a, b){return a - b})
        console.log("Years " + years);
        console.log("Months " + months);
        console.log("Dayofw " + dayofw);
        console.log("Dayofm " + dayofm);
        console.log("Timeslots " + timeslots);
        filter_options_setup();
    } else if(this.readyState == 4) {
        error("Bad status " + this.status);
    }
}

function filter_by_selected(){
	console.log("filtering");
	var endpoint = "https://jf0kmiscz8.execute-api.us-east-2.amazonaws.com/qualpha/filterslots";
	var xhr = new XMLHttpRequest();
    xhr.open('POST', endpoint);
    // Assign the form data
    var year = document.getElementById("year").value;
    var month = document.getElementById("month").value;
    var dayOfWeek = document.getElementById("dayOfWeek").value;
    var dayOfMonth = document.getElementById("dayOfMonth").value;
    var timeslot = document.getElementById("filterslot").value;
    var id2 = Number(get_urlparam("id"));
    var args = {
        "id2": id2,
        "year": year,
        "month": month,
        "dayOfWeek": dayOfWeek,
        "dayOfMonth": dayOfMonth,
        "timeslot" : timeslot
    };
    // Assign the callback handler
    xhr.onload = filter_slot_callback;
    // Send the request
    console.log(JSON.stringify(args));
    xhr.send(JSON.stringify(args));
}

function filter_slot_callback(){
    if (this.readyState == 4 && this.status == 200) {
        var data = JSON.parse(this.responseText);
        console.log(data);
        console.log("screaming");
        document.getElementById("filter_results").innerHTML = '';
        generate_filter_buttons(data);
        show_filter_results();
        console.log("continued screaming");
    } else if(this.readyState == 4) {
        error("Bad status " + this.status);
    }
}
