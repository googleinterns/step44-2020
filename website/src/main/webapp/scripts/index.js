async function getVolumeData() {
  fetch('/MockData')
    .then(response => response.json())
    .then((restaurantVolumeData) => {
      restaurantVolumeData.forEach((restaurant) => {
        console.log(restaurant);
      })
    });
}