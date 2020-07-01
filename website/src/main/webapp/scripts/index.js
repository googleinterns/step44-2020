async function getQueue(query) {
  const restaurants = await fetch('/searchRequest?query=' + query)
    .then(response => response.json())
    .then((restaurantsData) => {
      return restaurantsData["results"];
    });

  console.log(restaurants);
}

async function getVolumeData() {
  fetch('/MockData')
    .then(response => response.json())
    .then((restaurantVolumeData) => {
      restaurantVolumeData.forEach((restaurant) => {
        console.log(restaurant);
      })
    });
}