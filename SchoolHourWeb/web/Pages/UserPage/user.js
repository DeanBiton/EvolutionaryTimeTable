var refreshRate = 2000; //milli seconds
var USER_LIST_URL = buildUrlWithContextPath("userslist");
var CURRENT_USER_URL = buildUrlWithContextPath("currentUser");


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



//activate the timer calls after the page is loaded
$(function() {
    ajaxCurrentUser();
    //The users list is refreshed automatically every second
    setInterval(ajaxUsersList, refreshRate);

});