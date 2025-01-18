export interface TradeMarkResponse {
  records: Records[]
  total: number
  size: number
  current: number
  orders: any[]
  optimizeCountSql: boolean
  hitCount: boolean
  countId?: any
  maxLimit?: any
  searchCount: boolean
  pages: number
}

export interface Records {
  id: number
  tmName: string
  logoUrl: string
}
