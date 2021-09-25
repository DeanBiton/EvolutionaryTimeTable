//activate the timer calls after the page is loaded
const ALGORITHM_CONTROLS_URL = buildUrlWithContextPath("Algorithm/algorithmControls");
const ALGORITHM_STATUS_URL = buildUrlWithContextPath("Algorithm/algorithmStatus");
const ALGORITHM_END_CONDITIONS_URL = buildUrlWithContextPath("Algorithm/algorithmEndConditions");
let END_TIME;
let END_GEN;
let END_FITNESS;
var algorithmFunction;
const DO_NOT_SHOW=-1;

let progressBarGen ;
let progressBarTime ;
let progressBarFitness;
let progressBarGenWrapper;
let progressBarTimeWrapper;
let progressBarFitnessWrapper;


function createConsts()
{

     progressBarGen = document.getElementById("progressBarGen");
     progressBarTime = document.getElementById("progressBarTime");
     progressBarFitness = document.getElementById("progressBarFitness");

     progressBarGenWrapper = document.getElementById("progressBarGenWrapper");
     progressBarTimeWrapper = document.getElementById("progressBarTimeWrapper");
     progressBarFitnessWrapper = document.getElementById("progressBarFitnessWrapper")
}

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

function updateProgress(status)
{


    const percent="%";
    let gen=((status.dtoAlgorithmContidions.NumberOfGenerations/END_GEN)*100).toFixed(2)+percent;
    progressBarGen.style.width=gen;
    progressBarGen.innerText=gen;

    let time=((status.dtoAlgorithmContidions.time/(END_TIME*60))*100).toFixed(2)+percent;
    progressBarTime.style.width=time;
    progressBarTime.innerText=time;

    let fitness=((status.dtoAlgorithmContidions.fitness/END_FITNESS)*100).toFixed(2)+percent;
    progressBarFitness.style.width=fitness;
    progressBarFitness.innerText=fitness;

}

function getAlgorithmStatus() {
    $.ajax({
        url: ALGORITHM_STATUS_URL+idParam,
        success: function(status) {
            updateProgress(status);
        }
    });
}

function getEndConditions() {
    $.ajax({
        url: ALGORITHM_END_CONDITIONS_URL+idParam,
        success: function(endConditions) {
            END_GEN=endConditions.NumberOfGenerations;
            END_TIME=endConditions.time;
            END_FITNESS= endConditions.fitness;
            const SHOW="";
            if(END_TIME!== DO_NOT_SHOW)
            {
                progressBarTimeWrapper.style.display=SHOW;
            }
            if(END_GEN!== DO_NOT_SHOW)
            {
                progressBarGenWrapper.style.display=SHOW;
            }

            if(END_FITNESS!== DO_NOT_SHOW)
            {
                progressBarFitnessWrapper.style.display=SHOW;
            }

        }
    });
}

$(function() {
    createConsts();
    createTopNav();
    getEndConditions();
    setInterval(getAlgorithmStatus, 200);

});
