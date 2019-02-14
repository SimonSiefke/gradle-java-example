const results = require('./results.json')
const path = require('path')
const fs = require('fs')

const elkanResults = resultsByName('elkan')
const lloydResults = resultsByName('lloyd')

let mergedResults = []
for (let i = 0; i < elkanResults.length; i++) {
  mergedResults.push({
    dataset: elkanResults[i].dataset,
    numberofclusters: elkanResults[i].numberofclusters,
    lloyd: lloydResults[i].score,
    elkan: elkanResults[i].score,
  })
}
fs.writeFileSync(
  path.join(__dirname, 'processedresults.json'),
  JSON.stringify(mergedResults, null, 2)
)
function resultsByName(name) {
  return results
    .filter(result => result.benchmark.endsWith(name))
    .map(result => ({
      score: score(result),
      dataset: dataset(result),
      numberofclusters: numberofclusters(result),
    }))
}

function score(result) {
  return result.primaryMetric.score
}
function dataset(result) {
  return result.params.dataSet
}

function numberofclusters(result) {
  return result.params.numberOfClusters
}
