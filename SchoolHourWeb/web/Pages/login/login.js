
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
    console.log("started function login");
    $.ajax({
        data: $(loginForm).serialize(),
        url: this.action,
        timeout: 2000,
        error: function(errorObject) {

        },
        success: function(nextPageUrl) {
            window.location.replace(nextPageUrl);
        }

    });
}