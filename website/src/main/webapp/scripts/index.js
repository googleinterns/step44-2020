//TODO: Encapsulate the function below into smaller functions.
async function getQueue(query) {
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
    console.log(volumeData);
    
    const refresh = await fetch('/RefreshData')//new
    .then(response => response.json())
    .then((refreshData) => {
        console.log(refreshData);
      return refreshData;
    });
     console.log(refresh);
  volumeDataIndex = 1;

  const results = document.getElementById('results');

  results.innerHTML = setToEmpty();

  restaurants.forEach((restaurant) => {

    stars = buildStars(restaurant['rating']);

    color = getColor(volumeDataIndex);

    lineLength = buildLine(volumeDataIndex, color);

    results.innerHTML += buildRestaurantCard(restaurant, stars, lineLength);

    volumeDataIndex++;
  });

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

function setToEmpty() {
  return "";
}

function buildStars(rating) {
  stars = "";

  for (i = 0; i < parseInt(rating); i++) {
    stars += '<i class="fa fa-star" aria-hidden="true"></i>';
  }

  if (parseFloat(rating) - parseInt(rating) > .5) {
    stars += '<i class="fa fa-star-half-o" aria-hidden="true"></i>';
  }

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
    + restaurant['formattedAddress'] + '</h6><p class="card-text">'
    + stars + '</p><a href="#" class="card-link">' + lineLength
    + '<i class="fa fa-angle-down"></i></a></div></div>';

}