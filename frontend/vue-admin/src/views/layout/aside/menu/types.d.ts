export declare type MenuItem = {
  title: string
  icon: string
  path: string // index用于菜单项的唯一表示, 最好使用路由的path, 这样router=true, 点击菜单项就可以跳转到对应的路由
  subMenu?: MenuItem[]
}
