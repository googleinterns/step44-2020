//TODO: Encapsulate the function below into smaller functions.
async function getQueue(query) {
  const results = document.getElementById('results');

  results.innerHTML = '<div class="text-center">'
    + '<div class="spinner-border text-primary" role="status">'
    + '<span class="sr-only" > Loading...</span></div></div>';

  results.scrollIntoView();


  const restaurants = await fetch('/searchRequest?query=' + query)
    .then(response => response.json())
    .then((restaurantsData) => {
      return restaurantsData['results'];
    });

  const volumeData = await fetch('/MockData')
    .then(response => response.json())
    .then(
      (restaurantVolumeData) => {
        return restaurantVolumeData;
      });

  placeData = await fetch('/detailedRequest?placeID=' + buildPlaceQuery(restaurants))
    .then(response => response.json())
    .then((placeServletData) => {
      return placeServletData;
    });

  volumeDataIndex = 1;

  results.innerHTML = setToEmpty();

  restaurants.forEach((restaurant) => {

    stars = buildStars(restaurant['rating']);

    color = getColor(volumeDataIndex);

    lineLength = buildLine(volumeDataIndex, color);

    results.innerHTML += buildRestaurantCard(restaurant, stars, lineLength);

    volumeDataIndex++;
  });

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
  line = "<div>";

  for (i = 0; i < volumeData; i++) {
    line += '<i class="m-1 fa fa-user" style="color: ' + color + '" aria-hidden="true"></i>';
  }

  line += "</div>";

  return line;
}

function buildRestaurantCard(restaurant, stars, lineLength) {
  return '<div class="m-1 card"><div class="card-body"><h5 class="card-title">'
    + restaurant['name'] + '</h5><h6 class="card-subtitle mb-2 text-muted">'
    + restaurant['formattedAddress'] + '</h6>'
    + stars + lineLength + buildCollapsibleCard(restaurant['placeId']) + '</div></div>';

}

function buildCollapsibleCard(placeId) {
  return '<a data-toggle="collapse" href="#' + placeId + '" role="button" aria-expanded="false" aria-controls="collapseExample" class="card-link">'
    + '<i class="fa fa-angle-down"></i></a>' + '<div class="collapse" id="' + placeId + '">'
    + '<div class="card card-body"> Anim pariatur cliche reprehenderit, enim eiusmod high'
    + 'life accusamus terry richardson ad squid. Nihil anim keffiyeh helvetica, craft beer '
    + 'labore wes anderson cred nesciunt sapiente ea proident.</div></div>';
}