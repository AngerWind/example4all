let i = 0;
function wait(a) {
  return new Promise((resolve, reject) => {
    console.log(a);
    setTimeout(() => {
      i += 1;
      console.log(i);
      resolve(i);
    }, 1000);
  });
}

async function main() {
  let res1 = await wait("aaa");
  console.log(111);
  let res2 = await wait("bbb");
  console.log(222);
  let res3 = await wait("ccc");
  console.log(333);
}
main();
