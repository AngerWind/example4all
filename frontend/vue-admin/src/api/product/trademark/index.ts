import request from '@/utils/request.ts'
import { TradeMarkResponse } from '@/api/product/trademark/type.ts'

export const enum API {
  TRADEMARK = '/admin/product/baseTrademark/:pageNum/:pageSize',
  ADD_TRADEMARK = '/admin/product/baseTrademark/save',
  UPDATE_TRADEMARK = '/admin/product/baseTrademark/update',
  DELETE_TRADEMARK = '/admin/product/baseTrademark/remove/:id',
}

export const reqTrademark = (pageNum: number, pageSize: number) =>
  request.get<any, TradeMarkResponse>(
    API.TRADEMARK.replace(':pageSize', String(pageSize)).replace(
      ':pageNum',
      String(pageNum),
    ),
  )

export const reqAddOrUpdateTrademark = (
  tmName: string,
  logoUrl: string,
  id?: number,
) => {
  if (id) {
    return request.put<any, any>(API.UPDATE_TRADEMARK, {
      id,
      tmName,
      logoUrl,
    })
  } else {
    return request.post<any, any>(API.ADD_TRADEMARK, {
      tmName,
      logoUrl,
    })
  }
}

export const reqDeleteTrademark = (id: number) =>
  request.delete<any, any>(API.DELETE_TRADEMARK.replace(':id', String(id)))
