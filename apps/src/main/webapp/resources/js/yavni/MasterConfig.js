function initCities(id, asyncFlag) {
	asyncFlag = typeof asyncFlag !== 'undefined' ? asyncFlag : true;
	$.ajax({
		url : "/mvc/listCities",
		dataType : 'json',
		async: asyncFlag,
		success : function(cities) {
			var i;
			var citiesDD = document.getElementById(id);
			var html = '<option>Select</option>';
			for (i = 0; i < cities.length; i++) {
				html += "<option value='" + cities[i] + "'>" + cities[i] + "</option>";
			}
			citiesDD.innerHTML = html;
		}
	});
}

function initStates(id, asyncFlag) {
	asyncFlag = typeof asyncFlag !== 'undefined' ? asyncFlag : true;
	$.ajax({
		url : "/mvc/listStates",
		dataType : 'json',
		async: asyncFlag,
		success : function(state) {
			var i;
			var stateDD = document.getElementById(id);
			var html = '<option>Select</option>';
			for (i = 0; i < state.length; i++) {
				html += '<option value=' + state[i] + '>' + state[i]
						+ '</option>';
			}
			stateDD.innerHTML = html;
		}
	});
}

function initCountries(id, asyncFlag) {
	asyncFlag = typeof asyncFlag !== 'undefined' ? asyncFlag : true;
	$.ajax({
		url : "/mvc/listCountries",
		dataType : 'json',
		async: asyncFlag,
		success : function(countries) {
			var i;
			var countryDD = document.getElementById(id);
			var html = '<option>Select</option>';
			for (i = 0; i < countries.length; i++) {
				html += '<option value=' + countries[i] + '>' + countries[i]
						+ '</option>';
			}
			countryDD.innerHTML = html;
		}
	});
}

function initBanks(id) {
	$.ajax({
		url : "/mvc/listBanks",
		dataType : 'json',
		success : function(bankNames) {
			var i;
			var bankNamesDD = document.getElementById(id);
			var html = '';
			for (i = 0; i < bankNames.length; i++) {
				html += '<option value=' + bankNames[i][0] + '>'
						+ bankNames[i][1] + '</option>';
			}
			bankNamesDD.innerHTML = html;
		}
	});
}

function selectDropdown(source, selectTarget) {
	var value = document.getElementById(source).value;
	var target = document.getElementById(selectTarget);
	for(var i=0;i<target.options.length;i++) {
		if(target.options[i].value != '' && target.options[i].value == value) {
			target.options[i].selected = 'selected';
			break;
		}
	}
	
}