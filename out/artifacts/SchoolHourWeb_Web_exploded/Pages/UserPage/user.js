var refreshRate = 2000; //milli seconds
var USER_LIST_URL = buildUrlWithContextPath("userslist");
var CURRENT_USER_URL = buildUrlWithContextPath("currentUser");
var PROBLEMS_LIST_URL = buildUrlWithContextPath("Problem/problemList");


//users = a list of usernames, essentially an array of javascript strings:
// ["moshe","nachum","nachche"...]
function refreshUsersList(users) {
    //clear all current users
    var usersList = $("#userslist");

    $(usersList).empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(users || [], function(index, username) {
        console.log("Adding user #" + index + ": " + username);

        //create a new <li> tag with a value in it and append it to the #userslist (div with id=userslist) element
        $('<li>' + username + '</li>')
            .appendTo($(usersList));
    });
}


//problems = a list of DTOshortProblem, essentially an array of javascript types:

function refreshProblemsList(problems) {
    //clear all current users
    var problemsList = $("#problemsList");

    $(problemsList).empty();

    $.each(problems || [], function (index,problem){
        let listRow = document.createElement("li");
        listRow.appendChild(document.createTextNode(problem.uploadUser));
        let btn = document.createElement("BUTTON");
        btn.innerText= "enter"
        listRow.appendChild(btn);

        $(listRow).appendTo(problemsList);
    })


    // rebuild the list of users: scan all users and add them to the list of users
    //$.each(problems || [], function(index, problem) {
     //   console.log("Adding user #" + index + ": " + problem);

        //create a new <li> tag with a value in it and append it to the #userslist (div with id=userslist) element
      //  $('<li>' + problem.uploadUser + '</li>')
        //    .appendTo($(problemsList));
  //  });
}
//currentuser = single string
function ajaxCurrentUser() {
    $.ajax({
        url: CURRENT_USER_URL,
        success: function(currentuser) {
            $("#welcome").append('welcome ' +currentuser);
        }
    });
}
function ajaxUsersList() {
    $.ajax({
        url: USER_LIST_URL,
        success: function(users) {
            refreshUsersList(users);
        }
    });
}

function ajaxProblemsList() {
    $.ajax({
        url: PROBLEMS_LIST_URL,
        success: function(problems) {
            refreshProblemsList(problems);
        }
    });
}



//activate the timer calls after the page is loaded
$(function() {
    ajaxCurrentUser();
    //The users list is refreshed automatically every second
    setInterval(ajaxUsersList, refreshRate);
    setInterval(ajaxProblemsList, refreshRate);

});

$(function () {
    $("#uploadForm").on("submit", function () {

        let formData = new FormData();

        formData.append( 'file', $( '#fileInput' )[0].files[0] );

        $.ajax({
            data: formData,
            method: "POST",
            url: this.action,
            timeout: 2000,
            processData: false,
            contentType: false,
            error: function (errorObj) {
                $("#fileUploadErrorDiv").text("ERROR " + errorObj.responseText)
            },
            success: function (goodXmlFile) {
                $("#fileUploadErrorDiv").text("GOOD " +goodXmlFile)
            }
        });

        return false;
    })});