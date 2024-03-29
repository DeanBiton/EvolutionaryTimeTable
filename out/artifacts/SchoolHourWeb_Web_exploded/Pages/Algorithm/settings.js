// end conditions
function endConditionCheckBoxClicked(id)
{
    if(id === "fitnessCheckBox")
        fitness.disabled = fitnessCheckBox.checked !== true;
    else if(id === "generationNumberCheckBox")
        generationNumber.disabled = generationNumberCheckBox.checked !== true;
    else
        time.disabled = timeCheckBox.checked !== true;
}

// mutation Table
function checkMutation()
{
    var message = null;
    var message1 = null;
    var message2 = null;

    if (isNaN(mutationProbability.value) || mutationProbability.value === "")
        message1 = "probability is not a Double";
    else if(mutationProbability.value < 0 || mutationProbability.value > 1)
        message1 = "probability is not between 0 and 1";

    if(mutationType.value === "Flipping")
    {
        if(Number.isInteger(mutationParameter.value) || mutationParameter.value === "")
            message2 = "Max tuple is not an Integer";
        else if(mutationParameter.value <= 0)
            message2 = "Max tuple must be positive";
    }
    else
    {
        if(Number.isInteger(mutationParameter.value) || mutationParameter.value === "")
            message2 = "Total tuples is not an Integer";
    }

    if(message1 !== null && message2 !== null)
    {
        message = message1 + "\n" + message2;
    }
    else if(message1 !== null)
        message = message1;
    else if(message2 !== null)
        message = message2;

        return message;
}

function findButtonRowNumber()
{
    // returns undefine if not found
    buttonRowNumber = undefined;

    for(var rowNumber = 1; rowNumber < mutationsTable.rows.length; rowNumber++)
    {
        var row = mutationsTable.rows[rowNumber];
        if(row.cells[3].innerText === "clicked")
        {
            buttonRowNumber = rowNumber;
            break;
        }
    }

    return buttonRowNumber;
}

function deleteMutation()
{
    var rowNumber = findButtonRowNumber();
    mutationsTable.deleteRow(rowNumber);
    if(mutationsTable.rows.length === 1)
        mutationsTable.deleteRow(0);

    //document.getElementsByTagName("tr")[rowNumber].remove();
}

function addMutationToTable(mutationType, probability, mutationParameter, flippingComponent)
{
    const deleteMutationButton = document.createElement("button");
    deleteMutationButton.innerText = "Delete";
    deleteMutationButton.addEventListener("click", function () {
        deleteMutationButton.innerText = "clicked";
        deleteMutation();
    });
    if(disable === true)
        deleteMutationButton.disabled = true;

    let row = {Mutation : mutationType, Probability : probability ,
        ETC : mutationType === "Flipping"? ("max tuples: " + mutationParameter +", flipping component: "+ flippingComponent)
            : "total tuples: " + mutationParameter,
        "": deleteMutationButton};

    if(mutationsTable.rows.length === 0)
    {
        let headers = Object.keys(row);
        generateTableHead(mutationsTable, headers);
    }

    addRowToTable(mutationsTable, row);
}

function addHTMLMutation()
{
    var errorMessage = checkMutation();
    if(errorMessage === null)
    {
        addMutationToTable(mutationType.value, mutationProbability.value, mutationParameter.value, flippingComponent.value);
        mutationMessage.innerText = "";
    }
    else
    {
        mutationMessage.innerText = errorMessage;
    }
}

// Updating settings
function updateShowEveryGeneration(settings)
{
    showEveryGeneration.value = settings.showEveryGeneration;
}

function updateEndConditions(settings)
{
    if(settings.dtoEndConditions.fitness !== -1)
    {
        fitnessCheckBox.checked = true;
        fitness.disabled = false;
        fitness.value = settings.dtoEndConditions.fitness;
    }

    if(settings.dtoEndConditions.NumberOfGenerations !== -1)
    {
        generationNumberCheckBox.checked = true;
        generationNumber.disabled = false;
        generationNumber.value = settings.dtoEndConditions.NumberOfGenerations;
    }

    if(settings.dtoEndConditions.time !== -1)
    {
        timeCheckBox.checked = true;
        time.disabled = false;
        time.value = settings.dtoEndConditions.time;
    }
}

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

function updateMutations(setting)
{
    mutationsTable.innerHTML = "<tbody></tbody>";
    for(let DTOFlipping of setting.dtoMutations.flippings)
    {
        addMutationToTable("Flipping", DTOFlipping.flipping.probability,
            DTOFlipping.flipping.maxTuples, DTOFlipping.flipping.flippingComponent);
    }

    for(let DTOSizer of setting.dtoMutations.sizers)
    {
        addMutationToTable("Sizer", DTOSizer.sizer.probability,
            DTOSizer.sizer.totalTuples, "");
    }
}

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

async function updateSettings(settings) {
    // settings is a DTOEvolutionaryAlgorithmSettings instance
    initialPopulation.value = settings.initialPopulation;
    updateShowEveryGeneration(settings);
    updateSelection(settings);
    updateCrossover(settings);
    updateEndConditions(settings);
    getAlgorithmStatus();
    await sleep(700);
    updateMutations(settings);
}

function getDTOEvolutionaryAlgorithmSettings() {
    $.ajax({
        url: GET_SETTINGS_URL + "?id=" + new URLSearchParams(window.location.search).get("id"),
        success: function(settings) {
            updateSettings(settings);
        }
    });
}

function updatedSuccessfullyColor(message)
{
    if(message==="updated successfully")
        return "green";
    else
        return "red";
}
function updateMessages(messages) {
    initialPopulationMessage.innerText = messages["initialPopulation"];
    initialPopulationMessage.style.color=updatedSuccessfullyColor(messages["initialPopulation"]);
    selectionMessage.innerText = messages["selection"];
    selectionMessage.style.color=updatedSuccessfullyColor(messages["selection"]);
    crossoverMessage.innerText = messages["crossover"];
    crossoverMessage.style.color=updatedSuccessfullyColor(messages["crossover"]);
    endConditionsMessage.innerText = messages["endConditions"];
    endConditionsMessage.style.color=updatedSuccessfullyColor(messages["endConditions"]);
    showEveryGenerationMessage.innerText = messages["showEveryGeneration"];
    showEveryGenerationMessage.style.color=updatedSuccessfullyColor(messages["showEveryGeneration"]);
}

// sends update form
function getMutationParameterFromString(string)
{
    let mutationParameter;
    let colonLocation = string.indexOf(':');
    let commaLocation = string.indexOf(',');

    if(commaLocation === -1)
        mutationParameter = string.substring(colonLocation + 2);
    else
        mutationParameter = string.substring(colonLocation + 2, commaLocation);

    return mutationParameter;
}

function getFlippingComponentFromString(string)
{
    var colonLocation = string.lastIndexOf(':');

    return  string.substring(colonLocation + 2);
}

function getSettingsParams()
{
    let settingsParams = new URLSearchParams();
    settingsParams.append("id", ID);
    settingsParams.append("showEveryGeneration", showEveryGeneration.value);
    settingsParams.append("initialPopulation", initialPopulation.value);
    settingsParams.append("selection", selection.value);
    settingsParams.append("elitism", elitism.value);
    settingsParams.append("selectionParameter", selectionParameter.value);
    settingsParams.append("crossover", crossover.value);
    settingsParams.append("numberOfSeparators", numberOfSeparators.value);
    settingsParams.append("orientationType", orientationType.value);
    for(let rowNumber = 1; rowNumber < mutationsTable.rows.length; rowNumber++)
    {
        row = mutationsTable.rows[rowNumber];
        settingsParams.append("mutation", row.cells[0].innerText);
        settingsParams.append("probability", row.cells[1].innerText);
        settingsParams.append("mutationParameter", getMutationParameterFromString(row.cells[2].innerText));
        if(row.cells[0].innerText === "Flipping")
            settingsParams.append("flippingComponent", getFlippingComponentFromString(row.cells[2].innerText));
        else
            settingsParams.append("flippingComponent", "");
    }

    settingsParams.append("fitnessCheckBox", fitnessCheckBox.checked);
    settingsParams.append("fitness", fitness.value);
    settingsParams.append("generationNumberCheckBox", generationNumberCheckBox.checked);
    settingsParams.append("generationNumber", generationNumber.value);
    settingsParams.append("timeCheckBox", timeCheckBox.checked);
    settingsParams.append("time", time.value);

    return settingsParams;
}

$(function () {
    $("#updateSettingsForm").submit(function () {

        const settingsParams = getSettingsParams();

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

function mutationChanged()
{
    let toDisplayParameter = mutationType.value !== "Sizer" ? "" : "none";
    flippingComponentText.style.display = toDisplayParameter;
    flippingComponent.style.display = toDisplayParameter;
    mutationProbability.value = "";
    mutationParameter.value = "";
    mutationMessage.innerText = "";
    if(mutationType.value === "Flipping")
        mutationParameterText.innerText = "Max tuples: ";
    else
        mutationParameterText.innerText = "Total tuples: "
}

function addListeners()
{
    selection.addEventListener("change", selectionChanged);
    crossover.addEventListener("change", crossoverChanged);
    mutationType.addEventListener("change", mutationChanged);
}

// disable page
function disablePage()
{
    inputs = document.getElementsByTagName('input');
    selects = document.getElementsByTagName('select');
    for(let input of inputs)
        input.disabled = true;
    for(let select of selects)
        select.disabled = true;
    updateButton.disabled = true;
    addMutationButton.disabled = true;
}

function getAlgorithmStatus()
{

    $.ajax({
        method: "POST",
        url: ALGORITHM_STATUS_URL+idParam,
        timeout: 2000,
        success: function(status) {
            if(status.algorithmStatus === "RUNNING")
            {
                disable = true;
                disablePage();
            }
            else
                disable = false;
        },
        error: function (errorObj) {
            console.log("ERROR " + errorObj.responseText);
        }
    });
    //async: false,

}

// on load
$(function() { // onload...do
    addListeners();
    getDTOEvolutionaryAlgorithmSettings();
    createTopNav();
});

// Initialize constants
const GET_SETTINGS_URL = buildUrlWithContextPath("Algorithm/getSettings");
const UPDATE_SETTINGS_URL = buildUrlWithContextPath("Algorithm/updateSettings");
const ID = new URLSearchParams(window.location.search).get("id");

// show every generation
const showEveryGeneration = document.getElementById("showEveryGeneration");
const showEveryGenerationMessage = document.getElementById("showEveryGenerationMessage");
// end conditions
const fitnessCheckBox = document.getElementById("fitnessCheckBox");
const fitness = document.getElementById("fitness");
const generationNumberCheckBox = document.getElementById("generationNumberCheckBox");
const generationNumber = document.getElementById("generationNumber");
const timeCheckBox = document.getElementById("timeCheckBox");
const time = document.getElementById("time");
const endConditionsMessage = document.getElementById("endConditionsMessage");

// initial population
const initialPopulation = document.getElementById("initialPopulation");
const initialPopulationMessage = document.getElementById("initialPopulationMessage");

// selection
const selection = document.getElementById("selection");
const elitism = document.getElementById("elitism");
const selectionParameterText = document.getElementById("selectionParameterText");
const selectionParameter = document.getElementById("selectionParameter");
const selectionMessage = document.getElementById("selectionMessage");

// crossover
const crossover = document.getElementById("crossover");
const numberOfSeparators = document.getElementById("numberOfSeparators");
const orientationTypeText = document.getElementById("orientationTypeText");
const orientationType = document.getElementById("orientationType");
const crossoverMessage = document.getElementById("crossoverMessage");

// mutation
const mutationType = document.getElementById("mutationType");
const mutationProbability = document.getElementById("mutationProbability");
const mutationParameterText = document.getElementById("mutationParameterText");
const mutationParameter = document.getElementById("mutationParameter");
const flippingComponentText = document.getElementById("flippingComponentText");
const flippingComponent = document.getElementById("flippingComponent");
const mutationMessage = document.getElementById("mutationMessage");
const mutationsTable = document.getElementById("mutationsTable");
const addMutationButton = document.getElementById("addMutationButton");

// update
const form = document.getElementById("updateSettingsForm");
const updateButton = document.getElementById("updateButton");

//disable page
let disable;
const ALGORITHM_STATUS_URL = buildUrlWithContextPath("Algorithm/algorithmStatus");
