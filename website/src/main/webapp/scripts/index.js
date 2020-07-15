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

  console.log(restaurants);
  console.log(volumeData);
  volumeDataIndex = 1;

  const results = document.getElementById('results');

  restaurants.forEach((restaurant) => {
    stars = "";

    for (i = 0; i < parseInt(restaurant['rating']); i++) {
      stars += '<i class="fa fa-star" aria-hidden="true"></i>';
    }

    if (parseFloat(restaurant['rating']) - parseInt(restaurant['rating']) > .5) {
      stars += '<i class="fa fa-star-half-o" aria-hidden="true"></i>';
    }

    results.innerHTML += '<div class="m-1 card"><div class="card-body"><h5 class="card-title">'
      + restaurant['name'] + '</h5><h6 class="card-subtitle mb-2 text-muted">'
      + restaurant['formattedAddress'] + '</h6><p class="card-text">'
      + stars + '</p><a href="#" class="card-link"><i class="fa fa-angle-down"></i></a></div>'
      + '<div class="progress"><div class="progress-bar" role="progressbar" style="width: '
      + volumeDataIndex * 5 + '% " aria-valuenow=" ' + volumeDataIndex * 5 + '" aria-valuemin="0"aria-valuemax="100"></div></div>'
      + '</div>';

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