// 保存用到的各种dto类型信息

// 登录接口需要携带的参数类型
export interface LoginForm {
  username: string
  password: string
}

// 登录接口返回的数据类型
export type LoginResponseData = string

// 用户信息接口返回的数据类型
export interface UserInfoResponseData {
  routes: string[]
  buttons: string[]
  roles: string[]
  name: string
  avatar: string
}
export type LogoutResponseData = null | object | string
