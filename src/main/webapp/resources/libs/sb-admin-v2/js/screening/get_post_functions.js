function httpGet(theUrl){
    var xmlHttp = null;

    xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", theUrl, false );
    xmlHttp.send( null );
    return xmlHttp.responseText;
};

function httpPost(theUrl, jsondata){
    var xmlHttp = null;

    xmlHttp = new XMLHttpRequest();
    xmlHttp.open("POST", theUrl, false);
    xmlHttp.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');

    // send the collected data as JSON
    xmlHttp.send(jsondata);

    while (xmlHttp.onprogress){}
    
    xmlHttp.onloadend = function () {
        return xmlHttp.responseText;
    };
    return xmlHttp.responseText;
};