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

  const results = document.getElementById('results');

  restaurants.forEach((restaurant) => {
    console.log(restaurant['name']);
    console.log(restaurant['formattedAddress']);
    console.log(restaurant['rating']);

    results.innerHTML += '<div class="m-1 card"><div class="card-body"><h5 class="card-title">'
      + restaurant['name'] + '</h5><h6 class="card-subtitle mb-2 text-muted">'
      + restaurant['formattedAddress'] + '</h6><p class="card-text">'
      + restaurant['rating'] + '</p><a href="#" class="card-link"><i class="fa fa-angle-down"></i></a></div></div>';
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