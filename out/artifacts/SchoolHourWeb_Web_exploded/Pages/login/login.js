
$(function() { // onload...do
    login();
});


$("#loginForm").submit(function() {
    $.ajax({
        data: $(this).serialize(),
        url: this.action,
        timeout: 2000,
        error: function(errorObject) {
            console.error("Failed to login !");
            $("#error-placeholder").append(errorObject.responseText)
        },
        success: function(nextPageUrl) {
            window.location.replace(nextPageUrl);
        }
    });

    // by default - we'll always return false so it doesn't redirect the user.
    return false;
});

function login()
{
    var IsNull= '@Session["JSESSIONID"]'!= null;

    console.log( IsNull);
    if(    sessionStorage.getItem("JSESSIONID")!=null   )
    {
        document.getElementById("loginForm").submit();

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