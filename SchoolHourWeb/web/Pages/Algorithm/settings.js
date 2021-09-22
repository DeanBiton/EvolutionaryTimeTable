
var GET_SETTINGS_URL = buildUrlWithContextPath("Algorithm/getSettings");
var UPDATE_SETTINGS_URL = buildUrlWithContextPath("Algorithm/updateSettings");

// initial population
var initialPopulation;
var initialPopulationMessage;

// selection
var selection;
var elitism;
var selectionParameterText;
var selectionParameter;
var selectionParameterDiv;
var selectionMessage;

var form;

const ID = new URLSearchParams(window.location.search).get("id");

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
    selectionParameterDiv = document.getElementById("selectionParameterDiv");
    selectionMessage = document.getElementById("selectionMessage");

    form = document.getElementById("updateSettingsForm");
}

/// Updating settings

function updateSelection(settings)
{
    selection.value = settings.dtoSelection.name;
    elitism.value = settings.dtoSelection.elitism;

    var selectionName = settings.dtoSelection.name;
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

// gets a DTOEvolutionaryAlgorithmSettings instance
function updateSettings(settings) {
    initialPopulation.value = settings.initialPopulation;
    updateSelection(settings);
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
}

$(function () {
    $("#updateSettingsForm").submit(function () {

        const settingsParams = new URLSearchParams();
        settingsParams.append("id", ID);
        settingsParams.append("initialPopulation", initialPopulation.value);
        settingsParams.append("selection", selection.value);
        settingsParams.append("elitism", elitism.value);
        settingsParams.append("selectionParameter", selectionParameter.value);

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

function addListeners()
{
    selection.addEventListener("change", function () {
        let toDisplayParamter = selection.value !== "RouletteWheel" ? "" : "none";

        selectionParameterText.style.display = toDisplayParamter;
        selectionParameter.style.display = toDisplayParamter;

        selectionParameterText.innerText = "Top percent: ";
        if (selection.value === "Tournament") {
            selectionParameterText.innerText = "PTE: ";
        }

    });
}

$(function() { // onload...do
    updateConstants();
    addListeners();
    getDTOEvolutionaryAlgorithmSettings();
});