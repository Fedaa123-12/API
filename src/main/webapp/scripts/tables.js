//takes the differerent rows of the table and returns a fully functional table
function getFilmTable(rows) {
	var headings =
		["id", "title", "year", "director", "stars", "review", "delete", "update"];
	return (getTable(headings, rows));
}


//generating html table bodies
function getBodyContent(element) {
  element.normalize();
  return(element.childNodes[0].nodeValue);
}

//gets the XML element values and displays them
function getElementValues(element, subElementNames) {
	var values = new Array(subElementNames.length);
	for (var i = 0; i < subElementNames.length; i++) {
		var name = subElementNames[i];
		var subElement = element.getElementsByTagName(name)[0];
		values[i] = getBodyContent(subElement);
	}
	return (values);
}


function showXmlFilmInfo(request, resultRegion) {
	if ((request.readyState == 4) &&
		(request.status == 200)) {
		var xmlDocument = request.responseXML;

		var films = xmlDocument.getElementsByTagName('film');
		var rows = new Array();
		for (var i = 0; i < films.length; i++) {
			var film = films[i];
			var subElements =
				["id", "title", "year", "director", "stars", "review"];
			rows[i] = getElementValues(film, subElements);
		}
		var table = getFilmTable(rows);
		htmlInsert(resultRegion, table);
		runTable();
	}
}


function showJsonFilmInfo(request, resultRegion) {
	if ((request.readyState == 4) &&
		(request.status == 200)) {
		var rawData = request.responseText;
		var Films = eval("(" + rawData + ")");
		var rows = new Array();
		for (var i = 0; i < Films.length; i++) {
			var film = Films[i];
			rows[i] = [film.id, film.title,
			film.year, film.director, film.stars, film.review];

		}
		var table = getFilmTable(rows);
		htmlInsert(resultRegion, table);
		runTable();
	}
}

function showStringFilmInfo(request, resultRegion) {
	if ((request.readyState == 4) &&
		(request.status == 200)) {
		var rawData = request.responseText;
		var films = rawData.split("$");
		var rows = new Array();
		for (var i = 0; i < films.length; i++) {
			if (films[i].length > 1) {  // Ignore blank lines
				rows.push(films[i].split("#"));
			}
		}
		var table = getFilmTable(rows);
		htmlInsert(resultRegion, table);
		runTable();
	}
}

function preFilledUpdate(request) {
	if ((request.readyState == 4) &&
		(request.status == 200)) {
		var rawData = request.responseText;
		var films = rawData.split("#");

		var title = films[0];
		var year = films[1];
		var director = films[2];
		var stars = films[3];
		var review = films[4];

		document.getElementById("updateTitle").value = title;
		document.getElementById("updateYear").value = year;
		document.getElementById("updateDirector").value = director;
		document.getElementById("updateStars").value = stars;
		document.getElementById("updateReview").value = review;
	}

}


