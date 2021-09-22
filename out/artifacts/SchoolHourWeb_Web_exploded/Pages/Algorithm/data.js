
var GET_DATA_URL = buildUrlWithContextPath("Algorithm/getData");




function generateTableHead(table, data) {
    let thead = table.createTHead();
    let row = thead.insertRow();
    for (let key of data) {
        let th = document.createElement("th");
        let text = document.createTextNode(key);
        th.appendChild(text);
        row.appendChild(th);
    }
}

function generateTable(table, data) {
    for (let element of data) {
        let row = table.insertRow();
        for (key in element) {
            let cell = row.insertCell();
            let text = document.createTextNode(element[key]);
            cell.appendChild(text);
        }
    }
}

//let table = document.querySelector("table");
//let data = Object.keys(mountains[0]);
//generateTableHead(table, data);
//generateTable(table, mountains);

function createTimeTable(data) {
    let timeData = [
        { PARAMETER: "Days", VALUE: data.numberOfDays },
        { PARAMETER: "Hours", VALUE: data.numberOfHoursInADay },
        { PARAMETER: "Hard Rules Weight", VALUE: data.hardRulesWeight },
        { PARAMETER: "Soft Rules Weight", VALUE: 100-data.hardRulesWeight },



    ];
    let headers= Object.keys(timeData[0]);
    let timetable = document.getElementById("attributesContent");
    generateTableHead(timetable, headers);
    generateTable(timetable, timeData);

}

function getSubjectName(subjects, id) {
    let dtosubject=subjects[id];
    return dtosubject.name;
}

function createTeachersTable(data) {

    const teachers= data.teachers;
    const subjects=data.subjects;
    let teachersData = [];
    //{ID:t.teacher.id,Name: t.teacher.name,Working_Hours: t.teacher.workingHours }
    for (let [key, value] of Object.entries(teachers))
    {
        let subjectsNames="";
        for (let  sub of Object.entries(value.teacher.teaching))
        {
          //  console.log(sub[1])
            subjectsNames+=getSubjectName(subjects,sub[1])+",";
        }

        teachersData.push({ID:key,Name:value.teacher.name,Subjects: subjectsNames,Working_hours:value.teacher.workingHours});
    } //teachers.forEach((values,keys) => {teachersData.push({ID:values})})


    let headers= Object.keys(teachersData[0]);
    let teachersTable = document.getElementById("teachersContent");
    generateTableHead(teachersTable, headers);
    generateTable(teachersTable, teachersData);
}

function createSubjectsTable(subjects) {

    let subjectsData = [];
    //{ID:t.teacher.id,Name: t.teacher.name,Working_Hours: t.teacher.workingHours }
    for (let [key, value] of Object.entries(subjects))
    {


        subjectsData.push({ID:key,Name:value.name});
    } //teachers.forEach((values,keys) => {teachersData.push({ID:values})})


    let headers= Object.keys(subjectsData[0]);
    let subjectsTable = document.getElementById("subjectsContent");
    generateTableHead(subjectsTable, headers);
    generateTable(subjectsTable, subjectsData);
}

function createClassesTable(data) {

    const classrooms= data.classrooms;
    const subjects=data.subjects;
    let classesData = [];
    for (let [key, value] of Object.entries(classrooms))
    {
        let subjects2hours="";
        for (let  [key2, value2] of Object.entries(value.classroom.SubjectId2WeeklyHours))
        {
            //  console.log(sub[1])
            subjects2hours+=getSubjectName(subjects,key2)+"-"+value2+",";
        }

        classesData.push({ID:key,Name:value.classroom.name,Requirement : subjects2hours});
    } //teachers.forEach((values,keys) => {teachersData.push({ID:values})})


    let headers= Object.keys(classesData[0]);
    let classesTable = document.getElementById("classesContent");
    generateTableHead(classesTable, headers);
    generateTable(classesTable, classesData);
}


function createRulesTable(ruless) {

    let rulesData = [];
    //{ID:t.teacher.id,Name: t.teacher.name,Working_Hours: t.teacher.workingHours }
    for (let rule of Object.entries(ruless))
    {
        console.log(rule.name);
       rulesData.push({Name:rule[1].name,Type: rule[1].implementationLevel,Parameters:rule[1].etc});
    } //teachers.forEach((values,keys) => {teachersData.push({ID:values})})


    let headers= Object.keys(rulesData[0]);
    let rulesTable = document.getElementById("rulesContent");
    generateTableHead(rulesTable, headers);
    generateTable(rulesTable, rulesData);
}

// gets a DTOEvolutionaryAlgorithmSettings instance
function updateData(datas) {
    createTimeTable(datas);
    createTeachersTable(datas);
    createSubjectsTable(datas.subjects);
    createClassesTable(datas)
    createRulesTable(datas.rules)
}

function getDTOSchoolHoursData() {
    $.ajax({
        url: GET_DATA_URL + "?id=" + new URLSearchParams(window.location.search).get("id"),
        success: function(data) {
            updateData(data);
        }
    });
}






//activate the timer calls after the page is loaded
$(function() {
    getDTOSchoolHoursData();


});