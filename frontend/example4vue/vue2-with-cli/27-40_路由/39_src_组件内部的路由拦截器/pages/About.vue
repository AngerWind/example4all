<template>
  <h2>我是About的内容</h2>
</template>

<script>
export default {
  name: "About",

  // 在通过路由进入组件时被调用
  // 通过路由规则进入该组件时被调用,注意一定是通过路由规则进入组件，普通的组件装载是不会调用的
  beforeRouteEnter(to, from, next){
    console.log('App---beforeRouteEnter');
    console.log(from, to);
    const { isAuth } = to.meta;
    if(isAuth){
      //代表需要鉴权
      if(localStorage.getItem('school') === 'wust1')
        next();
      else
        alert('无权限');
    }else{
      next();
    }
  },
  //通过路由规则离开该组件时被调用
  beforeRouteLeave(to, from, next){
    console.log('App---beforeRouteLeave');
    console.log(from, to);
    next();
  }
}
</script>

<style scoped>

</style>
