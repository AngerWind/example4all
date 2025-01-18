import request from '@/utils/request.ts'

export const enum API {
  UPLOAD = '/admin/product/fileUpload',
}

// 上传图片接口
export const reqUpload = (data: FormData) =>
  request.post<any, string, FormData>(API.UPLOAD, data, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  })
