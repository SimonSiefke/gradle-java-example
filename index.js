const fs = require('fs')
const path = require('path')

const data = fs.readFileSync(
  path.join(__dirname, './benchmark/data/A1.txt'),
  'utf-8'
)
const lines = data.split('\n').map(l => l.trim()) //?
const numbers = lines.map(line =>
  line
    .split(' ')
    .filter(x => x)
    .map(Number.parseFloat)
) //?

// fs.writeFileSync('./l.json', JSON.stringify(numbers))

const as = numbers.map(([x, y]) => `${x}, ${y}`).join('\n')
fs.writeFileSync('./as2.txt', as)

numbers
  .reduce(([xsum, ysum], [x, y]) => [xsum + x, ysum + y], [0, 0])
  .map(x => x / numbers.length) //?
