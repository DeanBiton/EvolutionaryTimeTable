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

function refreshProblemsTable(problems) {
    //clear all current users
    let problemsTable = document.getElementById("problemsTable");
    problemsTable.innerHTML = "";

    $.each(problems || [], function (index,problem){
        let btn = document.createElement("BUTTON");
        btn.innerText= "enter";

        $(btn).on("click", function () {
            $.ajax({
                method: "POST",
                url: "/SchoolHourWeb_Web_exploded/Pages/Algorithm/enter?id="+problem.id,
                timeout: 2000,
                processData: false,
                contentType: false,
                error: function (errorObj) {
                    $("#fileUploadErrorDiv").text("ERROR " + errorObj.responseText)
                },
                success: function (redirectPath) {
                    myRedirect(redirectPath);
                }
            });

            return false;
        })

        let row = {"Uploader User": problem.uploadUser,
            "Number of days" : problem.NumberOfDays ,
            "Number of hours" : problem.NumberOfHours,
            "Number of subjects" : problem.NumberOfSubjects,
            "Number of teachers" : problem.NumberOfTeachers,
            "Number of classrooms" : problem.NumberOfClasses,
            "Number of hard rules" : problem.NumberOfHardRules,
            "Number of soft rules" : problem.NumberOfSoftRules,
            "Number of solvers" : problem.numberOfTriedUsers,
            "Best fitness " : problem.maxFitness,
            "Enter problem": btn};

        if(problemsTable.rows.length === 0)
        {
            let headers = Object.keys(row);
            generateTableHead(problemsTable, headers);
        }

        addRowToTable(problemsTable, row);
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

function ajaxProblemsTable() {
    $.ajax({
        url: PROBLEMS_LIST_URL,
        success: function(problems) {
            refreshProblemsTable(problems);
        }
    });
}

//activate the timer calls after the page is loaded
$(function() {
    ajaxCurrentUser();
    //The users list is refreshed automatically every second
    setInterval(ajaxUsersList, refreshRate);
    setInterval(ajaxProblemsTable, refreshRate);

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
            success: function (message) {
                $("#fileUploadErrorDiv").text(message)
            }
        });

        return false;
    })});
