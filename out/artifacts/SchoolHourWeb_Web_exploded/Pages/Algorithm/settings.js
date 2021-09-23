
const GET_SETTINGS_URL = buildUrlWithContextPath("Algorithm/getSettings");
const UPDATE_SETTINGS_URL = buildUrlWithContextPath("Algorithm/updateSettings");

// initial population
var initialPopulation;
var initialPopulationMessage;

// selection
var selection;
var elitism;
var selectionParameterText;
var selectionParameter;
var selectionMessage;

// crossover
var crossover;
var numberOfSeparators;
var orientationTypeText;
var orientationType;
var crossoverMessage;

var form;

const ID = new URLSearchParams(window.location.search).get("id");

//initialize constants
function updateConstants()
{
    // initial population
    initialPopulation = document.getElementById("initialPopulation");
    initialPopulationMessage = document.getElementById("initialPopulationMessage");

    // selection
    selection = document.getElementById("selection");
    elitism = document.getElementById("elitism");
    selectionParameterText = document.getElementById("selectionParameterText");
    selectionParameter = document.getElementById("selectionParameter");
    selectionMessage = document.getElementById("selectionMessage");

    //crossover
    crossover = document.getElementById("crossover");
    numberOfSeparators = document.getElementById("numberOfSeparators");
    orientationTypeText = document.getElementById("orientationTypeText");
    orientationType = document.getElementById("orientationType");
    crossoverMessage = document.getElementById("crossoverMessage");

    // update form
    form = document.getElementById("updateSettingsForm");
}

// Updating settings
function updateSelection(settings)
{
    selection.value = settings.dtoSelection.name;
    elitism.value = settings.dtoSelection.elitism;
    const selectionName = settings.dtoSelection.name;

    let toDisplayParameter = selection.value !== "RouletteWheel" ? "" : "none";
    selectionParameterText.style.display = toDisplayParameter;
    selectionParameter.style.display = toDisplayParameter;

    if(selectionName === "Truncation")
    {
        selectionParameterText.innerText = "Top percent: ";
        selectionParameter.value = settings.dtoSelection.truncation.topPercent;
    }
    else if(selectionName === "Tournament")
    {
        selectionParameterText.innerText = "PTE: ";
        selectionParameter.value = settings.dtoSelection.tournament.pte;
    }
}

function updateCrossover(settings)
{
    crossover.value = settings.dtoCrossover.name;
    numberOfSeparators.value = settings.dtoCrossover.numberOfSeparators;

    let toDisplayParameter = crossover.value !== "DayTimeOriented" ? "" : "none";
    orientationTypeText.style.display = toDisplayParameter;
    orientationType.style.display = toDisplayParameter;

    if(crossover.value === "AspectOriented")
        orientationType.value = settings.dtoCrossover.aspectOriented.orientationType;
}

function updateSettings(settings) {
    // settings is a DTOEvolutionaryAlgorithmSettings instance
    initialPopulation.value = settings.initialPopulation;
    updateSelection(settings);
    updateCrossover(settings);
}

function getDTOEvolutionaryAlgorithmSettings() {
    $.ajax({
        url: GET_SETTINGS_URL + "?id=" + new URLSearchParams(window.location.search).get("id"),
        success: function(settings) {
            updateSettings(settings);
        }
    });
}

function updateMessages(messages) {
    initialPopulationMessage.innerText = messages["initialPopulation"];
    selectionMessage.innerText = messages["selection"];
    crossoverMessage.innerText = messages["crossover"];
}

// sends update form
$(function () {
    $("#updateSettingsForm").submit(function () {

        const settingsParams = new URLSearchParams();
        settingsParams.append("id", ID);
        settingsParams.append("initialPopulation", initialPopulation.value);
        settingsParams.append("selection", selection.value);
        settingsParams.append("elitism", elitism.value);
        settingsParams.append("selectionParameter", selectionParameter.value);
        settingsParams.append("crossover", crossover.value);
        settingsParams.append("numberOfSeparators", numberOfSeparators.value);
        settingsParams.append("orientationType", orientationType.value);

        $.ajax({
            method: "POST",
            url: UPDATE_SETTINGS_URL,
            data: settingsParams.toString(),
            timeout: 2000,
            success: function (messages) {
                getDTOEvolutionaryAlgorithmSettings();
                updateMessages(messages);
            }
        });

        return false
    })
});

// change listeners
function selectionChanged()
{
    let toDisplayParameter = selection.value !== "RouletteWheel" ? "" : "none";

    selectionParameterText.style.display = toDisplayParameter;
    selectionParameter.style.display = toDisplayParameter;

    selectionParameterText.innerText = "Top percent: ";
    if (selection.value === "Tournament") {
        selectionParameterText.innerText = "PTE: ";
    }

    selectionParameter.value = "";
    selectionMessage.innerText = "";
}

function crossoverChanged()
{
    let toDisplayParameter = crossover.value !== "DayTimeOriented" ? "" : "none";
    orientationTypeText.style.display = toDisplayParameter;
    orientationType.style.display = toDisplayParameter;
    crossoverMessage.innerText = "";
}

function addListeners()
{
    selection.addEventListener("change", selectionChanged);
    crossover.addEventListener("change", crossoverChanged);
}

// on load
$(function() { // onload...do
    updateConstants();
    addListeners();
    getDTOEvolutionaryAlgorithmSettings();
});