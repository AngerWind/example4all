async function main() {
    const chai = require("chai");
    const assert = chai.assert;
    const expect = chai.expect;
    const should = chai.should();
}

main()
  .then(() => {
    return process.exit();
  })
  .catch((err) => {
    console.log(err);
    process.exit(1);
  });