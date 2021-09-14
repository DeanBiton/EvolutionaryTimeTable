//const Http = new XMLHttpRequest();
//const url='/Pages/CheckUserName';
//Http.open("GET", url);
//Http.send();

let cookies = document.cookie;
let loginURL = window.location.href.sub(0, window.location.href.length) + ""

if(!cookies.includes("userName"))
{
    window.location.replace("http://localhost:8080/SchoolHourWeb_Web_exploded/Pages/login/login.html");
}
else
{
    window.location.replace("http://localhost:8080/SchoolHourWeb_Web_exploded/Pages/UserPage/user.html");
}