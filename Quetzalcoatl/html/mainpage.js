function isAdmin(){
    //var isSysAdmin = true;
    var isAdmin =  get_cookie("isAdmin");
    if(isAdmin==1){
        document.getElementById("sysAdminArea").style.visibility='visible';
    }
}

function participating(){
    var id = parseInt(get_cookie("token"));
	var endpoint = "https://jf0kmiscz8.execute-api.us-east-2.amazonaws.com/qualpha/getparticipating";
	var xhr = new XMLHttpRequest();
    xhr.open('POST', endpoint);
    // Assign the form data
    var args = {
        "userId": id
    };
    // Assign the callback handler
    xhr.onload = get_part_callback;
    // Send the request
    xhr.send(JSON.stringify(args));
}

function get_part_callback(){
    // Do a do
    if (this.readyState == 4 && this.status == 200) {
        var data = JSON.parse(this.responseText);
        //success("Got some schedules for ya");
        console.log(data);
        const regex = /Successfully getted schedules: ([0-9]+)/gm;
        console.log(data.schedules);
        document.getElementById("participation").innerHTML = "";
        for(var i=0;i<data.schedules.length;i++){
            var element = document.createElement("div");
            var element2 = document.createElement("a");
            var sdate2 = Date.parse(data.schedules[i].startDate);
            var sdate = new Date(sdate2);
            var sdate3 = "" + sdate.getFullYear() + "-" + (sdate.getMonth() + 1) +"-"+ ("00" + sdate.getDate()).substr(-2,2);
            element2.setAttribute('href', 'participant.html?id='+data.schedules[i].realId+'&date='+sdate3);
            element2.setAttribute('class', 'button');
            var text = document.createTextNode(data.schedules[i].name+" made by "+data.schedules[i].username);
            //comment
            element2.appendChild(text);
            element.appendChild(element2);
            document.getElementById("participation").appendChild(element);
        }
    } else if(this.readyState == 4) {
        error("Bad status " + this.status);
    }
}

function organized(){
    var id = parseInt(get_cookie("token"));
	var endpoint = "https://jf0kmiscz8.execute-api.us-east-2.amazonaws.com/qualpha/getorganized";
	var xhr = new XMLHttpRequest();
    xhr.open('POST', endpoint);
    // Assign the form data
    var args = {
        "id2": id
    };
    // Assign the callback handler
    xhr.onload = get_org_callback;
    // Send the request
    xhr.send(JSON.stringify(args));
}

function get_org_callback(){
    // Do a do
    if (this.readyState == 4 && this.status == 200) {
        var data = JSON.parse(this.responseText);
        //success("Got some schedules for ya");
        console.log(data);
        const regex = /Successfully getted schedules: ([0-9]+)/gm;
        console.log(data.schedules);
        document.getElementById("organized").innerHTML = "";
        for(var i=0;i<data.schedules.length;i++){
            var element = document.createElement("div");
            var element2 = document.createElement("a");
            var sdate2 = Date.parse(data.schedules[i].startDate);
            var sdate = new Date(sdate2);
            var sdate3 = "" + sdate.getFullYear() + "-" + (sdate.getMonth() + 1) +"-"+ ("00" + sdate.getDate()).substr(-2,2);
            element2.setAttribute('href', 'organizerView.html?id='+data.schedules[i].realId+'&date='+sdate3);
            element2.setAttribute('class', 'button');
            var text = document.createTextNode(data.schedules[i].name+" made by you");
            element2.appendChild(text);
            element.appendChild(element2);
            document.getElementById("organized").appendChild(element);
        }
    } else if(this.readyState == 4) {
        error("Bad status " + this.status);
    }
}

function addpart2(){
    var id = parseInt(get_cookie("token"));
    var code = document.getElementById("toJoin").value;
	var endpoint = "https://jf0kmiscz8.execute-api.us-east-2.amazonaws.com/qualpha/joinschedule";
	var xhr = new XMLHttpRequest();
    xhr.open('POST', endpoint);
    // Assign the form data
    var args = {
        "id": id,
        "code": code
    };
    // Assign the callback handler
    xhr.onload = addpart2_post;
    // Send the request
    xhr.send(JSON.stringify(args));
}

function addpart2_post(){
    if(this.readyState == 4 && this.status == 200) {
        var data = JSON.parse(this.responseText);
        success("schedule joined!");
        location.reload(true);
    } else if(this.readyState == 4) {
        error("Server returned error " + this.status);
        console.log(this.responseText);
    }
}