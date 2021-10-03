const IS_Username_Exist_URL = buildUrlWithContextPath("login/userCheck");


function isUsernameExist() {
    $.ajax({
        method: "POST",
        url: IS_Username_Exist_URL,
        timeout: 2000,
        processData: false,
        contentType: false,
        error: function (errorObj) {
            $("#fileUploadErrorDiv").text("ERROR " + errorObj.responseText)
        },
        success: function(redirectPath) {
            if(redirectPath !== "")
                myRedirect(redirectPath);
        }
    });
}

function login()
{
    var IsNull= '@Session["JSESSIONID"]'!= null;

    console.log( IsNull);
    if(sessionStorage.getItem("JSESSIONID")!=null)
    {
        document.getElementById("loginForm").submit();
        $("#loginForm").submit();
    }
    /*console.log("started function login");
    $.ajax({
        data: $("#loginForm").serialize(),
        url: $("#loginForm").action,
        timeout: 2000,
        error: function(errorObject) {

        },
        success: function(nextPageUrl) {
            console.log("success");

            window.location.replace(nextPageUrl);
        }

    });*/
}

$(function() { // onload...do
    isUsernameExist();

    $("#loginForm").submit(function() {
        $.ajax({
            data: $(this).serialize(),
            url: this.action,
            timeout: 2000,

            error: function (errorObject) {
                console.error("Failed to login !");
                console.log(errorObject.responseText)
                $("#loginError").text(errorObject.responseText);
            },
            success: function(nextPageUrl) {
                window.location.replace(nextPageUrl);
            }
        });

        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    });
});