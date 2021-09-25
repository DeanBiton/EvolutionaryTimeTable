//activate the timer calls after the page is loaded
const ALGORITHM_CONTROLS_URL = buildUrlWithContextPath("Algorithm/algorithmControls");
var algorithmFunction;

$(function () {
    $("#algorithmFunction").submit(function (buttonString) {

        let settingsParams = new URLSearchParams();
        settingsParams.append("buttonString", buttonString);

        $.ajax({
            method: "POST",
            url: ALGORITHM_CONTROLS_URL,
            data: settingsParams.toString(),
            timeout: 2000,
            success: function () {

            }
        });

        return false
    })
});

function submitAlgorithmFunction(button)
{
    let settingsParams = new URLSearchParams();
    settingsParams.append("buttonString", button.innerText);

    $.ajax({
        method: "POST",
        url: ALGORITHM_CONTROLS_URL + idParam,
        data: settingsParams.toString(),
        timeout: 2000,
        success: function (message) {
        console.log(message);
        }
    });

    return false
}

$(function() {
    createTopNav();
    //algorithmFunction = document.getElementById("algorithmFunction");
});
