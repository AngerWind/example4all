// 保存用到的各种dto类型信息

// 登录接口需要携带的参数类型
export interface LoginForm {
  username: string
  password: string
}

// 登录接口返回的数据类型
export interface LoginResponseData {
  code: number
  data: {
    token: string
  }
}

// 用户信息接口返回的数据类型
export interface UserInfoResponseData {
  code: number
  data: {
    checkUser: {
      userId: number
      avatar: string
      username: string
      password: string
      description: string
      roles: string[]
      buttons: string[]
      routes: string[]
      token: string
    }
  }
}
