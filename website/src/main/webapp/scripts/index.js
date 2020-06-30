async function getRestaurants(query) {
  fetch('/searchRequest?query=tacos+in+new+york')
    .then(response => response.json())
    .then((restaurants) => {
      restaurants["results"].forEach((restaurant) => {
        console.log(restaurant["name"]);
      })
    });
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