// 统一管理用户相关的接口

import request from '@/utils/request.ts'
import {
  LoginForm,
  LogoutResponseData,
  UserInfoResponseData,
  LoginResponseData,
} from '@/api/user/type.ts'

export const enum API {
  LOGIN = '/admin/acl/index/login',
  USER_INFO = '/admin/acl/index/info',
  LOGOUT = '/admin/acl/index/logout',
}

// 登录接口
export const reqLogin = (data: LoginForm) =>
  request.post<any, LoginResponseData, LoginForm>(API.LOGIN, data)

// 获取用户信息接口
export const reqUserInfo = () =>
  request.get<any, UserInfoResponseData>(API.USER_INFO)

// 退出登录接口
export const reqLogout = () => request.post<any, LogoutResponseData>(API.LOGOUT)
