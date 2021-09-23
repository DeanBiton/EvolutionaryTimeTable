
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

function addRowToTable(table, row) {
    let tableRow = table.insertRow();
    for (key in row) {
        let cell = tableRow.insertCell();
        let insertCell;
        if(row[key].tagName === "BUTTON")
            insertCell = row[key];
        else
            insertCell = document.createTextNode(row[key]);
        cell.appendChild(insertCell);
    }
}

