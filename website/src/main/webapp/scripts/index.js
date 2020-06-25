async function testing(){
  console.log("starting test");

  const response = await fetch('/searchRequest');
  const testResults = await response.text();
  console.log(testResults);
  document.getElementById('testResults').innerText = testResults;

  console.log("test ran");
}