//creates the string for post requests
function makePostParamString(title, year, director, stars, review, format) {
	var dataFormat = format;
	var items;
	switch (dataFormat) {
		case 'application/json':
			var data = { "title": title, "year": year, "director": director, "stars": stars, "review": review };
			items = JSON.stringify(data);
			break;
		case 'application/xml':
			var data =
				"<film>"
				+ "<title>" + title + "</title>"
				+ "<year>" + year + "</year>"
				+ "<stars>" + director + "</stars>"
				+ "<director>" + stars + "</director>"
				+ "<review>" + review + "</review>"
				+ "</film>";
			items = (data);
			break;
		case 'text/plain':
			var data =
				title + "#" + year + "#" + director + "#" + stars + "#" + review;
			items = data;
			break;
	}
	return (items);
}

//creates the string for update (put) requests
function makeupdateParamString(id, title, year, director, stars, review, format) {
	var dataFormat = format;
	var items;

	switch (dataFormat) {
		case 'application/json':
			var data = { "id": id, "title": title, "year": year, "director": director, "stars": stars, "review": review };
			items = JSON.stringify(data);
			break;


		case 'application/xml':
			var data =
				"<film>"
				+ "<id>" + id + "</id>"
				+ "<title>" + title + "</title>"
				+ "<year>" + year + "</year>"
				+ "<stars>" + director + "</stars>"
				+ "<director>" + stars + "</director>"
				+ "<review>" + review + "</review>"
				+ "</film>";
			items = (data);
			break;
		case 'text/plain':
			var data =
				id + "#" + title + "#" + year + "#" + director + "#" + stars + "#" + review;
			items = data;
			break;
	}
	return (items);
}

////creates the string for delete requests
function makeDeleteParamString(id, format) {
	var dataFormat = format;
	var items;
	switch (dataFormat) {
		case 'application/json':
			var data = { "id": id };
			items = JSON.stringify(data);
			break;
		case 'application/xml':
			var data =
				"<film>"
				+ "<id>" + id + "</id>"
				+ "</film>";
			items = (data);
			break;
		case 'text/plain':
			var data = id;
			items = data;
			break;
	}
	return (items);
}