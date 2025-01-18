// 统一管理用户相关的接口

import request from '@/utils/request.ts'
import {
  LoginForm,
  LoginResponseData,
  UserInfoResponseData,
} from '@/api/user/type.ts'

export const enum API {
  LOGIN = '/user/login',
  USER_INFO = '/user/userInfo',
}

// 登录接口
export const reqLogin = (data: LoginForm) =>
  request.post<any, LoginResponseData>(API.LOGIN, data)

// 获取用户信息接口
export const reqUserInfo = () =>
  request.get<any, UserInfoResponseData>(API.USER_INFO)
