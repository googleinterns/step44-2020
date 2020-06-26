
function loadPage(){

fetch('/MockData').then(response => response.json()).then((resaurants) => {

    const commentListElement = document.getElementById('comment-container');
    commentListElement.innerHTML = '';
    restaurantss.forEach((line) => {
      commentListElement.appendChild(createListElement(line));
   });
  });
}

function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}