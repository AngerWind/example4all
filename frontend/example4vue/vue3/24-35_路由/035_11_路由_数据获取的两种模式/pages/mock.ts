// 模拟一个异步的数据请求(假装是axios发请求), 500毫秒后返回数据
export function fetchData(data: string): Promise<string> {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve(data)
    }, 500)
  })
}
