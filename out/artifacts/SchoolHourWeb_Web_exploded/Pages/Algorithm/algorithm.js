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
let progressBarGenTitle;
let progressBarTimeTitle;
let progressBarFitnessTitle;
let genProgress;
let timeProgress;
let fitnessProgress;

let runButton;
let pauseResumeButton;
let stopButton;

let currentBestSolution = null;
let bestSolutionGeneralDetails;
let BestSolutionTabsPanel;
let Teacher;
let Class;
let Rules;
// creating constants
function createConsts()
{

    progressBarGen = document.getElementById("progressBarGen");
    progressBarTime = document.getElementById("progressBarTime");
    progressBarFitness = document.getElementById("progressBarFitness");

    progressBarGenWrapper = document.getElementById("progressBarGenWrapper");
    progressBarTimeWrapper = document.getElementById("progressBarTimeWrapper");
    progressBarFitnessWrapper = document.getElementById("progressBarFitnessWrapper");

    progressBarGenTitle = document.getElementById("progressBarGenTitle");
    progressBarTimeTitle = document.getElementById("progressBarTimeTitle");
    progressBarFitnessTitle = document.getElementById("progressBarFitnessTitle");

    genProgress = document.getElementById("genProgress");
    timeProgress = document.getElementById("timeProgress");
    fitnessProgress = document.getElementById("fitnessProgress");

    runButton = document.getElementById("runButton");
    pauseResumeButton = document.getElementById("pauseResumeButton");
    stopButton = document.getElementById("stopButton");

    BestSolutionTabsPanel = document.getElementById("BestSolutionTabsPanel");
    Teacher = document.getElementById("Teacher");
    Class = document.getElementById("Class");
    bestSolutionGeneralDetails = document.getElementById("bestSolutionGeneralDetails");
    Rules = document.getElementById("Rules");
}

// algorithm controls Accessibility and update progress bars
function setControlButtonsAccessibility(algorithmStatus)
{
    runButton.disabled = algorithmStatus !== "STOPPED";
    pauseResumeButton.disabled = algorithmStatus === "STOPPED";
    stopButton.disabled = algorithmStatus === "STOPPED";


    if(algorithmStatus === "PAUSED")
    {
        pauseResumeButton.innerText = "Resume";
    }
    else
    {
        pauseResumeButton.innerText = "Pause";
    }
}

function updateProgress(status)
{
    const percent="%";
    let gen=((status.dtoAlgorithmContidions.NumberOfGenerations/END_GEN)*100).toFixed(2)+percent;
    progressBarGen.style.width=gen;
    progressBarGen.innerText=gen;
    progressBarGenTitle.innerText = "Generation: " + status.dtoAlgorithmContidions.NumberOfGenerations;

    if(progressBarGenWrapper.style.display === "")
        progressBarGenTitle.innerText += " / " + END_GEN;

    let time=((status.dtoAlgorithmContidions.time/(END_TIME*60))*100).toFixed(2)+percent;
    progressBarTime.style.width=time;
    progressBarTime.innerText=time;
    progressBarTimeTitle.innerText = "time: " + new Date(status.dtoAlgorithmContidions.time * 1000).toISOString().substr(11, 8);


    if(progressBarTimeWrapper.style.display === "")
        progressBarTimeTitle.innerText += " / " + new Date(END_TIME * 60 * 1000).toISOString().substr(11, 8);

    let fitness=((status.dtoAlgorithmContidions.fitness/END_FITNESS)*100).toFixed(2)+percent;
    progressBarFitness.style.width=fitness;
    progressBarFitness.innerText=fitness;
    progressBarFitnessTitle.innerText = "fitness: " + status.dtoAlgorithmContidions.fitness;

    if(progressBarFitnessWrapper.style.display === "")
        progressBarFitnessTitle.innerText += " / " + END_FITNESS;
}

function getAlgorithmStatus() {
    $.ajax({
        url: ALGORITHM_STATUS_URL+idParam,
        success: function(status) {
            updateProgress(status);
            setControlButtonsAccessibility(status.algorithmStatus);
        }
    });
}

// set progress bars size
function getEndConditions() {
    $.ajax({
        url: ALGORITHM_END_CONDITIONS_URL+idParam,
        success: function(endConditions) {
            END_GEN=endConditions.NumberOfGenerations;
            END_TIME=endConditions.time;
            END_FITNESS= endConditions.fitness;

            progressBarTimeWrapper.style.display = END_TIME === DO_NOT_SHOW? "none" : "";
            progressBarGenWrapper.style.display = END_GEN === DO_NOT_SHOW? "none" : "";
            progressBarFitnessWrapper.style.display = END_FITNESS === DO_NOT_SHOW? "none" : "";
            //inline
        }
    });
}

// related to best solutions view options
function showHideBestSolution(id)
{
    $(".tabcontent").css("display", "none");
    current = document.getElementById(id);
    current.style.display="";

    if(id === "Teacher")
    {
        let teachersComoBox = document.getElementById("teachersComoBox");
        createTeacherOrClassroomTableById(teachersComoBox.value, true);
    }
    else if(id === "Class")
    {
        let classroomsComoBox = document.getElementById("classroomsComoBox");
        createTeacherOrClassroomTableById(classroomsComoBox.value, false);
    }
}

// get best solution button (1. creates raw and rules tables 2. create teacher and classroom combo boxes)
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

    for (let rule of bestSolution.rules)
    {
        rulesData.push({Name:rule.name,Type:rule.implementationLevel,Parameters:rule.etc,Score:rule.score});
    }

    let headers= Object.keys(rulesData[0]);
    generateTable(RulesDataTable, rulesData);
    generateTableHead(RulesDataTable, headers);
}

function createOptions(map, id,name)
{
    let select = document.getElementById(id);
    $(select).empty();

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
    Rules.style.display="none";
}

function getBestSolution()
{
    $.ajax({
        method: "GET",
        url: ALGORITHM_BEST_SOLUTION_URL + idParam,
        timeout: 2000,
        success: function (bestSolution) {
            currentBestSolution=bestSolution;
            if(bestSolution !== null)
            {
                bestSolutionGeneralDetails.style.display="";
                BestSolutionTabsPanel.style.display="";
                createBestSolution(currentBestSolution)
            }
        }
    });

    return false
}

// creating teacher/classroom table
function initializeTeacherOrClassroomTable(table)
{
    let numberOfDays = currentBestSolution.schoolHoursData.numberOfDays;
    let numberOfHoursInADay = currentBestSolution.schoolHoursData.numberOfHoursInADay;
    let header = [];
    let cornerText = "          Day\nHour ";

    table.innerHTML = "<tbody></tbody>";
    header.push(cornerText);
    for(let dayNumber = 1; dayNumber <= numberOfDays; dayNumber++)
    {
        header.push(dayNumber);
    }

    generateTableHead(table, header);
    for(let hourNumber = 1; hourNumber <= numberOfHoursInADay; hourNumber++)
    {
        let row = table.getElementsByTagName('tbody')[0].insertRow();
        let cell;
        let element = document.createElement("th");
        let text = document.createTextNode(hourNumber.toString());
        element.appendChild(text);
        row.appendChild(element);

        for(let dayNumber = 1; dayNumber <= numberOfDays; dayNumber++)
        {
            cell = row.insertCell();
            cell.appendChild(document.createTextNode(""));
        }
    }
}

function addNewLineTextToCell(table, rowNumber, colNumber, textToAdd)
{
    let cell = table.rows[rowNumber].cells[colNumber];
    if(cell)
    {
        cell.innerText = cell.innerText.length !== 0? cell.innerText+="\n": cell.innerText ;
        cell.innerText += textToAdd;
    }
}

function fillTeacherOrClassroomTableCells(table, value, isTeacherTable)
{
    for(let dtoTuple of currentBestSolution.dtoTuples)
    {
        let addToCellString;

        if(isTeacherTable)
        {
            if(dtoTuple.teacher.teacher.id.toString() === value)
            {
                addToCellString = dtoTuple.classroom.classroom.id + " " +
                    dtoTuple.classroom.classroom.name + ", " +
                    dtoTuple.subject.id + " " +
                    dtoTuple.subject.name;
                addNewLineTextToCell(table, dtoTuple.hour, dtoTuple.day, addToCellString);
            }
        }
        else
        {
            if(dtoTuple.classroom.classroom.id.toString() === value)
            {
                addToCellString = dtoTuple.teacher.teacher.id + " " +
                    dtoTuple.teacher.teacher.name + ", " +
                    dtoTuple.subject.id + " " +
                    dtoTuple.subject.name;
                addNewLineTextToCell(table, dtoTuple.hour, dtoTuple.day, addToCellString);
            }
        }
    }
}

function createTeacherOrClassroomTableById(value, isTeacherTable)
{
    let table;

    if(isTeacherTable)
        table = document.getElementById("TeacherTable");
    else
        table = document.getElementById("ClassroomTable");

    initializeTeacherOrClassroomTable(table);
    fillTeacherOrClassroomTableCells(table, value, isTeacherTable);
}

function tablesInit()
{
    const teachersComoBox = document.getElementById("teachersComoBox");
    const classroomsComoBox = document.getElementById("classroomsComoBox");

    teachersComoBox.addEventListener("change", function () {
        createTeacherOrClassroomTableById(teachersComoBox.value, true);
    })

    classroomsComoBox.addEventListener("change", function () {
        createTeacherOrClassroomTableById(classroomsComoBox.value, false);
    })
}

// clicked algorithm controls
function submitAlgorithmFunction(button)
{
    let settingsParams = new URLSearchParams();
    settingsParams.append("buttonString", button.innerText);

    $.ajax({
        method: "POST",
        url: ALGORITHM_CONTROLS_URL + idParam,
        data: settingsParams.toString(),
        timeout: 2000,
        success: function () {}
    });

    return false
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

// initiate best solution view
function initiateBestSolutionView()
{
    bestSolutionGeneralDetails.style.display="none";
    BestSolutionTabsPanel.style.display="none";
    Teacher.style.display="none";
    Class.style.display="none";
}

// on load
$(function() {
    createConsts();
    addListeners();
    createTopNav();
    getEndConditions();
    setInterval(getAlgorithmStatus, 200);
    tablesInit();
    initiateBestSolutionView();
});
