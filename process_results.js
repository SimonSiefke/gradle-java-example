const results = require('./results.json')
const path = require('path')
const fs = require('fs')

const algorithms = ['lloyd', 'elkan']
const algorithmsResults = algorithms.map(resultsByName)
// const elkanResults = resultsByName('elkan')
// const lloydResults = resultsByName('lloyd')

let mergedResults = []
for (let i = 0; i < algorithmsResults[0].length; i++) {
  const algResults = algorithms.reduce(
    (total, current, index) => ({
      ...total,
      [current]: algorithmsResults[index][i].score,
    }),
    {}
  ) //?
  mergedResults.push({
    dataset: algorithmsResults[0][i].dataset,
    numberofclusters: algorithmsResults[0][i].numberofclusters,
    ...algResults,
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
