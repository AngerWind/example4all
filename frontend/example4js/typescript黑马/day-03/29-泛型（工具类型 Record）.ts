type RecordObj = Record<'a' | 'b' | 'c', string[]>

// type RecordObj = {
//   a.js: string[]
//   b: string[]
//   c: string[]
// }

let obj: RecordObj = {
  a: ['a'],
  b: ['b'],
  c: ['c']
}
