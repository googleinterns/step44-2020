
async function getVolumeData() {
  fetch('/MockData')
    .then(response => response.json())
    .then((restaurantVolumeData) => {
      restaurantVolumeData.forEach((restaurant) => {
        console.log(restaurant);
      })
    });
}

async function testing() {
  console.log("starting test");

  const response = await fetch('/searchRequest');
  const testResults = await response.text();
  console.log(testResults);
  document.getElementById('testResults').innerText = testResults;

  console.log("test ran");
}