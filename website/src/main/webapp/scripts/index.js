//TODO: Encapsulate the function below into smaller functions.
async function getQueue(query) {
  const results = document.getElementById('results');
  const filters = document.getElementById('filters');

  results.innerHTML = '<div class="animated fadeIn text-center">'
    + '<div class="spinner-border text-primary" role="status">'
    + '<span class="sr-only" > Loading...</span></div></div>';

  results.scrollIntoView();


  const restaurants = await fetch('/searchRequest?query=' + query)
    .then(response => response.json())
    .then((restaurantsData) => {
      return restaurantsData['results'];
    });

  volumeData = await fetch('/MockData')
    .then(response => response.json())
    .then(
      (restaurantVolumeData) => {
        volData = [];
        restaurantVolumeData.forEach((restaurantVolume) => {
          volData.push(parseInt(restaurantVolume.match(/\d+/g)));
        });
        return volData;
      });

  placeIdMap = await fetch('/detailedRequest?placeID=' + buildPlaceQuery(restaurants))
    .then(response => response.json())
    .then((placeServletData) => {
      return placeServletData;
    });

  index = 0;

  results.innerHTML = setToEmpty();

  results.innerHTML = setResultsLabel(query);

  restaurants.forEach((restaurant) => {

    stars = buildStars(restaurant['rating']);

    color = getColor(volumeData[index]);

    lineLength = buildLine(volumeData[index], color);

    results.innerHTML += buildRestaurantCard(restaurant, stars, lineLength, placeIdMap);

    index++;
  });

  filters.innerHTML = buildFiltersMenu();
  results.scrollIntoView();
}

async function getRestaurants(query) {
  fetch('/searchRequest?query=' + query)
    .then(response => response.json())
    .then((restaurantsData) => {
      return restaurantsData['results'];
    });
}

async function getVolumeData() {
  fetch('/MockData')
    .then(response => response.json())
    .then(
      (restaurantVolumeData) => {
        return restaurantVolumeData;
      });
}

function buildPlaceQuery(restaurants) {
  query = "";

  restaurants.forEach((restaurant) => {
    query += restaurant['placeId'] + '.';
  });

  return query.substring(0, query.length - 1);
}

function setToEmpty() {
  return "";
}

function setResultsLabel(query) {
  return '<h5><b> Results for "' + query + '"</b></h5>'
}

function buildStars(rating) {
  stars = "<div>";

  for (i = 0; i < parseInt(rating); i++) {
    stars += '<i class="fa fa-star" aria-hidden="true"></i>';
  }

  if (parseFloat(rating) - parseInt(rating) > .5) {
    stars += '<i class="fa fa-star-half-o" aria-hidden="true"></i>';
  }

  stars += "</div>"

  return stars;
}

function getColor(volumeData) {
  if (volumeData < 6) {
    ;
    return '#008000';
  } else if (volumeData < 13) {
    return '#CCCC00';
  } else {
    return '#FF0000';
  }
}

function buildLine(volumeData, color) {
  if (volumeData == 0) {
    return '<div><p>No Line!</p></div>'
  }
  line = "<div>";

  for (i = 0; i < volumeData; i++) {
    line += '<i class="m-1 fa fa-user" style="color: ' + color + '" aria-hidden="true"></i>';
  }

  line += "</div>";

  return line;
}

function buildRestaurantCard(restaurant, stars, lineLength, placeIdMap) {
  return '<div class="animated fadeIn m-1 card"><div class="card-body"><h5 class="card-title">'
    + restaurant['name'] + '</h5><h6 class="card-subtitle mb-2 text-muted">'
    + restaurant['formattedAddress'] + '</h6>'
    + stars + lineLength + buildCollapsibleCard(restaurant['placeId'], placeIdMap) + '</div></div>';

}

function buildCollapsibleCard(placeId, placeIdMap) {
  return '<a data-toggle="collapse" href="#' + placeId + '" role="button" aria-expanded="false" aria-controls="collapseExample" class="card-link">'
    + '<i class="fas fa-caret-down"></i></a>' + '<div class="collapse" id="' + placeId + '">'
    + '<div class="card card-body">' + buildInformationSection(placeIdMap[placeId]) + '</div></div>';
}

function buildInformationSection(placeData) {
  infoSection = "";

  if (placeData['openingHours']) {
    infoSection += buildOpeningHoursSection(placeData['openingHours']);
  }

  if (placeData['formattedPhoneNumber']) {
    infoSection += buildPhoneSection(placeData['formattedPhoneNumber']);
  }

  if (placeData['website']) {
    infoSection += buildWebsiteSection(placeData['website']);
  }

  return infoSection;
}

function buildOpeningHoursSection(hourData) {
  ohSection = '<div>';
  if (hourData.openNow) {
    ohSection += '<h6 class="card-text text-success"> Open Now </h6>';
  } else {
    ohSection += '<h6 class="card-text text-danger"> Closed </h6>';
  }

  for (i = 0; i < 7; i++) {
    ohSection += '<p class="card-text">' + hourData['weekdayText'][i] + '</p>';
  }

  ohSection += '</div>';

  return ohSection;
}

function buildPhoneSection(phoneData) {
  return '<div class="mt-1 mb-1 card-link"> <a href="tel:' + parseInt(phoneData, 10)
    + '">' + phoneData + '</a></div>';
}

function buildWebsiteSection(websiteData) {
  return '<div> <a href="' + websiteData
    + '" class="btn btn-primary"> Go To Website </a></div>';
}

function buildFiltersMenu() {
  return `<h5><b> Filters </b></h5>
					<div class="m-1 btn-group btn-group-toggle" data-toggle="buttons">
						<label class="btn btn-primary">
              <input type="checkbox" autocomplete="off">
              <i class="fa fa-star" aria-hidden="true"></i>
              <i class="fa fa-star" aria-hidden="true"></i>
              <i class="fa fa-star" aria-hidden="true"></i> 
            </label>
						<label class="btn btn-primary">
              <input type="checkbox" autocomplete="off">
              <i class="fa fa-star" aria-hidden="true"></i>
              <i class="fa fa-star" aria-hidden="true"></i>
              <i class="fa fa-star" aria-hidden="true"></i>
              <i class="fa fa-star" aria-hidden="true"></i>
            </label>
						<label class="btn btn-primary">
              <input type="checkbox" autocomplete="off"> 
              <i class="fa fa-star" aria-hidden="true"></i>
              <i class="fa fa-star" aria-hidden="true"></i>
              <i class="fa fa-star" aria-hidden="true"></i>
              <i class="fa fa-star" aria-hidden="true"></i>
              <i class="fa fa-star" aria-hidden="true"></i>
            </label>
					</div>
					<div class="m-1 btn-group btn-group-toggle" data-toggle="buttons">
						<label class="btn btn-success">
              <input type="checkbox" autocomplete="off">
              <i class="fa fa-user" aria-hidden="true"></i>
            </label>
						<label class="btn btn-warning">
              <input type="checkbox" autocomplete="off">
                <i class="fa fa-user" aria-hidden="true"></i>
                <i class="fa fa-user" aria-hidden="true"></i>
            </label>
						<label class="btn btn-danger">
              <input type="checkbox" autocomplete="off"> 
              <i class="fa fa-user" aria-hidden="true"></i>
              <i class="fa fa-user" aria-hidden="true"></i>
              <i class="fa fa-user" aria-hidden="true"></i>
            </label>
					</div>
					<div class="m-1 custom-control custom-checkbox">
						<input type="checkbox" class="custom-control-input" id="defaultUnchecked">
						<label class="custom-control-label" for="defaultUnchecked"> Open Now </label>
					</div> `;
}

function gotem() {
  console.log("hehe");
}