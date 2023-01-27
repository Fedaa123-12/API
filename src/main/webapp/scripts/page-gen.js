// Get the browser-specific request object, either for
// Firefox, Safari, Opera, Mozilla, Netscape, or IE 7 (top entry);
// or for Internet Explorer 5 and 6 (bottom entry). 
function getRequestObject() {
  if (window.XMLHttpRequest) {
    return(new XMLHttpRequest());
  } else if (window.ActiveXObject) { 
    return(new ActiveXObject("Microsoft.XMLHTTP"));
  } else {
    return(null); 
  }
}

//server request to fetch a movie using an id sent through parameters
function getMovieByID(address, id) {
	var request = getRequestObject();
	var paramAddress = address +id;
	request.onreadystatechange = function() { preFilledUpdate(request) };
  	request.open("GET", paramAddress, true);
  	request.setRequestHeader("Accept","text/plain");
  	request.send(null);
}

//gets the value of any html element with a unique id
function getValue(id) {
  return (document.getElementById(id).value);
}

//get a specific value of a html item where a unique id is not possible 
function getValOfMultipleID(val, elementOut){
  var x = document.getElementById(elementOut);
  x.setAttribute("value", val); 
}

//----------------------------doGet----------------------------//
//makes an ajax request using jQuery for a get rquest
function ajaxResultJquery(address, resultRegion) {
	var selectedValue = getValue("dataFormat");	
	var request = $.ajax({
		url: address,
		type: "GET",
		headers:{
			Accept: selectedValue
		}	
	});
	request.done(function(){
		switch(selectedValue) {
			case("application/xml"):
				showXmlFilmInfo(request,resultRegion);
			break;
			case("application/json"):
				showJsonFilmInfo(request,resultRegion)
			break;
			case("text/plain"):
				showStringFilmInfo(request,resultRegion)
			break;
		}
	});	
}

//search film
function searchFilmJQuery(address,resultRegion){
	var selectedValue = getValue("dataFormat");	
	var param1 = getValue('searchBy');var param2 = getValue('search');
	var paramAddress = address + "?searchBy=" + param1 + "&searchinput=" + param2;
	
	var request = $.ajax({
		url: paramAddress,
		type: "GET",
		headers:{
			Accept: selectedValue
		}
	});
	request.done(function(){
		switch(selectedValue) {
			case("application/xml"):
				showXmlFilmInfo(request,resultRegion);
			break;
			case("application/json"):
				showJsonFilmInfo(request,resultRegion)
			break;
			case("text/plain"):
				showStringFilmInfo(request,resultRegion)
			break;
		}
	});
}

//----------------------------doPost----------------------------//
function ajaxPostJQuery(address) {
	var selectedValue = getValue("dataFormat");	
	var title = getValue("insertTitle");
 	var year = getValue("insertYear");
 	var director = getValue("insertDirector");
 	var stars = getValue("insertStars");
 	var review = getValue("insertReview");	
 	
 	
	var film = makePostParamString(title, year, director, stars, review, selectedValue);
	var request = $.ajax({
		url: address,
		type: "POST",
		data: film,
		contentType: selectedValue
	});
	
	request.done(function(msg){
		console.log(msg);
		location.reload();
	});
	
	
}

//----------------------------doPut----------------------------//
//makes a put request to update film details
function ajaxPutJQuery(address) {
	var selectedValue = getValue("dataFormat");
	var id = getValue('getID');	
	var title = getValue("updateTitle");
 	var year = getValue("updateYear");
 	var director = getValue("updateDirector");
 	var stars = getValue("updateStars");
 	var review = getValue("updateReview");	
	var film = makeupdateParamString(id,title, year, director, stars, review, selectedValue);
	
	var request = $.ajax({
		url: address,
		type: "PUT",
		data: film,
		contentType: selectedValue
	});
	request.done(function(msg){
		ajaxResultJquery("getFilm","dataTable");
		console.log(msg);
	});
}

//----------------------------doDelete----------------------------//

//make a delete request to the server based on the format selected 
function ajaxDeleteJquery(address){
	var selectedValue = getValue("dataFormat");
	var id = getValue('getID');
	var film = makeDeleteParamString(id,selectedValue);
	
	var request = $.ajax({
		url: address,
		type: "DELETE",
		data: film,
		contentType: selectedValue
	});
	request.done(function(msg){
		console.log(msg);
	});
}

//dynamically removes films with a fade-out effect  
function removeMe(deleteButton) {	
	$(deleteButton).closest('tr').fadeOut(1000,function(){	
		$(deleteButton).closest('tr').remove();	
	});
}





//----------------------------Table Generator----------------------------//
//Generates the table
function getTable(headings, rows) {
	var table =  "<table class= 'table table-sm' id='main-table'>\n" 
		+getTableHeadings(headings) 
        +getTableBody(rows)
        +"</table>"; 
	return(table);
}

// Insert the html data into the element that has the specified id.
function htmlInsert(id, htmlData) {
	document.getElementById(id).innerHTML = htmlData;
}

function getTableHeadings(headings) {
	var firstRow = "<thead><tr>";
	for(var i=0; i<headings.length; i++) {
		firstRow += "<th>" + headings[i] + "</th>";
	}
	firstRow += "</tr></thead>";
	return(firstRow);
}

//creates table body
function getTableBody(rows) {
	var body = "<tbody>";
	for(var i=0; i<rows.length; i++) {
		body += "<tr>";
		var row = rows[i];
		for(var j=0; j<row.length; j++) {
			body += "<td>" + row[j] + "</td>";
		}
		body += "<td>" + "<button class='deleteBtn btn btn-outline-danger' value="+ row[0]
		+" onclick= getValOfMultipleID(this.value,'getID'),ajaxDeleteJquery"
		+"('getFilm',this.value),removeMe(this)>Delete</td>";
		
		body += "<td>" + "<button class='updateBtn btn btn-outline-info'" 
		+ "value="+ row[0]+" onclick= openUpdateForm(),getValOfMultipleID(this.value,'getID')"
		+",getMovieByID('getFilm?id=',this.value)>Update</td>";
		
		body += "</tr>";
	}
	body += "</tbody>"
	return(body); 	
}




//----------------------------Pagination----------------------------//
function runTable(){
	var tbody = document.querySelector("tbody");
	var pageUl = document.querySelector(".pagination");
	var itemShow = document.querySelector("#itemperpage");
	var tr = tbody.querySelectorAll("tr");
	var emptyBox = [];
	var index = 1;
	var itemPerPage = 60;
	
	for(let i=0; i<tr.length; i++){ emptyBox.push(tr[i]);}//adds empty table rows to our empty array
	
	if(emptyBox.length < 60){itemPerPage = emptyBox.length}
	
	itemShow.onchange = giveTrPerPage;
	
	//runs when the user changes item per page
	function giveTrPerPage(){
		itemPerPage = Number(this.value);//gets the films per page requested number
		displayPage(itemPerPage);
		pageGenerator(itemPerPage);
		getpagElement(itemPerPage);
	}
	
	//displays the table items per page
	function displayPage(limit){
		tbody.innerHTML = '';
			for(let i=0; i<limit; i++){
				tbody.appendChild(emptyBox[i]);
			}
		const  pageNum = pageUl.querySelectorAll('.list');
		pageNum.forEach(n => n.remove());
		
	}
	displayPage(itemPerPage);
	
	//generates the table with data
	function pageGenerator(getem){
		const num_of_tr = emptyBox.length;
		if(num_of_tr <= getem){
			pageUl.style.display = 'none';
		}else{
			pageUl.style.display = 'flex';
			const num_Of_Page = Math.ceil(num_of_tr/getem);
			for(i=1; i<=num_Of_Page; i++){
				const li = document.createElement('li'); li.className = 'list-group-item list';
				const a =document.createElement('a'); a.href = '#';a.className= "link-dark"; a.innerText = i;
				a.setAttribute('data-page', i);
				li.appendChild(a);
				pageUl.insertBefore(li,pageUl.querySelector('.next'));
			}
		}
	}
	pageGenerator(itemPerPage);
	
	//finds all the paged numbers we created to get the last page
	let pageLink = pageUl.querySelectorAll("a");
	let lastPage =  pageLink.length - 2;
	
	//allows the navigation between pages
	function pageRunner(page, items, lastPage, active){
		for(button of page){
			button.onclick = e=>{
				const page_num = e.target.getAttribute('data-page');
				const page_mover = e.target.getAttribute('id');
				if(page_num != null){
					index = page_num;
	
				}else{
					if(page_mover === "next"){
						index++;
						if(index >= lastPage){
							index = lastPage;
						}
					}else{
						index--;
						if(index <= 1){
							index = 1;
						}
					}
				}
				pageMaker(index, items, active);
			}
		}
	
	}
	
	//finds the first active page link
	var pageLi = pageUl.querySelectorAll('.list'); pageLi[0].classList.add("active");
	pageRunner(pageLink, itemPerPage, lastPage, pageLi);
	
	//gets the elemets of the specific page
	function getpagElement(val){
		let pagelink = pageUl.querySelectorAll("a");
		let lastpage =  pagelink.length - 2;
		let pageli = pageUl.querySelectorAll('.list');
		pageli[0].classList.add("active");
		pageRunner(pagelink, val, lastpage, pageli);
	}
	
	
	//generates the table with data in split up into different pages
	function pageMaker(index, item_per_page, activePage){
		const start = item_per_page * index;
		const end  = start + item_per_page;
		const current_page =  emptyBox.slice((start - item_per_page), (end-item_per_page));
		tbody.innerHTML = "";
		for(let j=0; j<current_page.length; j++){
			let item = current_page[j];					
			tbody.appendChild(item);
		}
		Array.from(activePage).forEach((e)=>{e.classList.remove("active");});
		activePage[index-1].classList.add("active");
	}
}

	

