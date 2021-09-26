//activate the timer calls after the page is loaded
const ALGORITHM_CONTROLS_URL = buildUrlWithContextPath("Algorithm/algorithmControls");
const ALGORITHM_STATUS_URL = buildUrlWithContextPath("Algorithm/algorithmStatus");
const ALGORITHM_END_CONDITIONS_URL = buildUrlWithContextPath("Algorithm/algorithmEndConditions");
const ALGORITHM_BEST_SOLUTION_URL  = buildUrlWithContextPath("Algorithm/algorithmBestSolution");

let END_TIME;
let END_GEN;
let END_FITNESS;
const DO_NOT_SHOW=-1;

let progressBarGen ;
let progressBarTime ;
let progressBarFitness;
let progressBarGenWrapper;
let progressBarTimeWrapper;
let progressBarFitnessWrapper;

let runButton;
let pauseResumeButton;
let stopButton;

let currentBestSolution = null;


function setButtonsAccessibility(algorithmStatus)
{
    runButton.disabled = algorithmStatus !== "STOPPED";
    pauseResumeButton.disabled = algorithmStatus === "STOPPED";
    stopButton.disabled = algorithmStatus === "STOPPED";

    if(algorithmStatus === "STOPPED")
    {
        pauseResumeButton.innerText = "Pause";
    }
}

function createConsts()
{

     progressBarGen = document.getElementById("progressBarGen");
     progressBarTime = document.getElementById("progressBarTime");
     progressBarFitness = document.getElementById("progressBarFitness");

     progressBarGenWrapper = document.getElementById("progressBarGenWrapper");
     progressBarTimeWrapper = document.getElementById("progressBarTimeWrapper");
     progressBarFitnessWrapper = document.getElementById("progressBarFitnessWrapper");

    runButton = document.getElementById("runButton");
    pauseResumeButton = document.getElementById("pauseResumeButton");
    stopButton = document.getElementById("stopButton");
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
            setButtonsAccessibility(status.algorithmStatus);
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

function pauseResumeClicked()
{
    pauseResumeButton.onclick = function()  {
        submitAlgorithmFunction(this);

        if(pauseResumeButton.innerText === "Resume")
            pauseResumeButton.innerText = "Pause";
        else
            pauseResumeButton.innerText = "Resume";
    }
}

function addListeners()
{
    pauseResumeClicked();
}





function showHideBestSolution(id)
{
    $(".tabcontent").css("display", "none");
    current = document.getElementById(id);
    current.style.display="";

}


function createRawTable(DTOTuples) {

    let rawDataTable = document.getElementById("RawTable");
    $(rawDataTable).empty();

    let rawData = [];
    for (let tuple of Object.entries(DTOTuples))
    {
        rawData.push({Day:tuple[1].day,Hour:tuple[1].hour,Classroom:tuple[1].classroom.classroom.name,Teacher:tuple[1].teacher.teacher.name,Subject:tuple[1].subject.name});
    }


    let headers= Object.keys(rawData[0]);
    //rawData.sort((firstEl, secondEl) => { ... } );
    generateTable(rawDataTable, rawData);
    generateTableHead(rawDataTable, headers);

}
function createRulesTable(bestSolution) {

    let RulesDataTable = document.getElementById("RulesTable");
    $(RulesDataTable).empty();

    let rulesData = [];
    for (let [key, value] of Object.entries(bestSolution.rulesScores))
    {
        rulesData.push({Name:key,Type:key.implementationLevel,Parameters:key.etc,Score:value});
    }


    let headers= Object.keys(rulesData[0]);
    //rawData.sort((firstEl, secondEl) => { ... } );
    generateTable(RulesDataTable, rulesData);
    generateTableHead(RulesDataTable, headers);

}

function createOptions(map, id,name)
{
    let select = document.getElementById(id);
    for (let [key, value] of Object.entries(map))
    {
        let option = document.createElement("option");
        option.value=key;
        option.innerText=value[name].name;
        select.appendChild(option);

    }
}

function createTeachersTableComoBox(bestSolution)
{
    createOptions(bestSolution.schoolHoursData.teachers,"teachersComoBox","teacher");
}
function createClassroomsTableComoBox(bestSolution)
{
    createOptions(bestSolution.schoolHoursData.classrooms,"classroomsComoBox","classroom");
}

function createBestSolution(bestSolution)
{
    createRawTable(bestSolution.dtoTuples);
    createRulesTable(bestSolution);
    createTeachersTableComoBox(bestSolution);
    createClassroomsTableComoBox(bestSolution);

}



function getBestSolution()
{
    $.ajax({
        method: "GET",
        url: ALGORITHM_BEST_SOLUTION_URL + idParam,
        timeout: 2000,
        success: function (bestSolution) {
            currentBestSolution=bestSolution;
           createBestSolution(currentBestSolution)
        }
    });

    return false
}



function createNewROw(numberOfDays)
{//////mini function for createTeacherTableById
    let element={Hour:""};

    for(let i=1;i<+numberOfDays;i++)
    {
        const kkey="Day"+i;
        element[kkey]=0;
    }
    console.log(element);
    return element;
}
function createTeacherTableById(value)
{
    //////i tried but it doesnt work
    let numberOfDays= currentBestSolution.schoolHoursData.numberOfDays;
    let numberOfHoursInADay= currentBestSolution.schoolHoursData.numberOfHoursInADay;

    let teacherDataTable = document.getElementById("TeacherTable");
    $(teacherDataTable).empty();

    let teacherData = [];

    for (let hour=1;hour<=numberOfHoursInADay;hour++)
    {

        let newRow=createNewROw(numberOfDays);
        for (let day=1;day<=numberOfDays;day++)
        {
            let dayName= "Day"+day;
            const result = currentBestSolution.dtoTuples.filter(tuple => tuple.day===day&&tuple.hour===hour&&tuple.teacher.teacher.id===value);

            //newRow.dayName=
            //result.forEach(element=>teacherData.push({Hour:hour,dayName:element.classroom.classroom.name,Parameters:key.etc,Score:value});

        }

    }




        for (let tuple of Object.entries(currentBestSolution.dtoTuples))
    {

        teacherData.push({Name:key,Type:key.implementationLevel,Parameters:key.etc,Score:value});
    }


    let headers= Object.keys(teacherData[0]);
    //rawData.sort((firstEl, secondEl) => { ... } );
    generateTable(teacherDataTable, teacherData);
    generateTableHead(teacherDataTable, headers);
}

function tablesInit()
{
    const teachersComoBox = document.getElementById("teachersComoBox");
    const classroomsComoBox = document.getElementById("classroomsComoBox");


    teachersComoBox.addEventListener("change", function () {
        // console.log(teachersComboBox.value);
        createTeacherTableById(teachersComoBox.value);
    })

    classroomsComoBox.addEventListener("change", function () {
        // console.log(classesComboBox.value);
       createClassTableById(classroomsComoBox.value);/////still doesnt exist
    })
}

$(function() {
    createConsts();
    addListeners();
    createTopNav();
    getEndConditions();
    setInterval(getAlgorithmStatus, 200);
    tablesInit();
});
