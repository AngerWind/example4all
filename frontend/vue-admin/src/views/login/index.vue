<script setup lang="ts">
import { reactive, ref } from 'vue'
import { User, Lock } from '@element-plus/icons-vue'

// form表单属性
const form = reactive({
  username: '',
  password: '', // 账号密码的默认值, 省的每次都要输入
})
// form表单验证规则
const rules = reactive({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }, // 在失去焦点的时候触发
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'change' }, // 输入框文本改变时触发
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 10, message: '长度在 6 到 10 个字符', trigger: 'blur' },
    {
      // 自定义验证规则
      validator: (rule, value, callback) => {
        // value接收到的是prop传过来的值
        // callback是一个回调函数, 用来返回错误信息, 如果没有错误信息, 就直接调用callback()
        // rule一个包含当前校验规则的对象
        let pattern = /^[a-zA-Z0-9_]+$/ // 字母数字下划线
        if (!pattern.test(value)) {
          return callback(new Error('密码必须是字母数字下划线')) // 返回错误信息
        }
        callback() // 成功
      },
      trigger: 'blur',
    },
  ],
})

// 登录按钮是否loading
const loading = ref(false)

import { useUserStore } from '@/store/modules/user.ts'
import { reqLogin } from '@/api/user'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const $router = useRouter()
const userStore = useUserStore()
const $route = useRoute()
// 获取表单的ref
const formRef = ref()

// 提交表单
async function submit(form) {
  // 在提交表单之前, 先校验表单, 返回一个Promise, 如果校验失败, 连请求都不要发送
  await formRef.value.validate() // 校验表单

  // 登录按钮loading
  loading.value = true
  const token = await reqLogin(form)
  // 登录成功
  userStore.updateToken(token)
  if ($route.query.redirect) {
    // 如果有重定向的地址, 就跳转到重定向的地址
    await $router.push($route.query.redirect as string)
  } else {
    await $router.push('/')
  }
  // 登录按钮loading
  loading.value = false
}

function reset() {
  // 重置表单
  formRef.value.resetFields()
}
</script>

<template>
  <div class="login-container">
    <el-row>
      <el-col :span="12"></el-col>
      <el-col :span="12">
        <!-- ref用于获取表单实例 -->
        <!--:model表示表单需要验证的数据-->
        <!--rules表示表单的交易规则-->
        <!--hide-required-asterisk, 如果添加了校验规则, 并且字段是必选的, 那么会在item之前出现一个*号, 表示必选, 使用该属性可以消除*号-->
        <el-form
          ref="formRef"
          :model="form"
          label-position="left"
          class="login-form"
          :rules="rules"
          hide-required-asterisk
        >
          <h1>Hello</h1>
          <h2>欢迎来到硅谷甄选</h2>
          <!--prop表示当前item需要验证的属性, 他是el-form的 model的属性名 -->
          <el-form-item label="账号" prop="username" class="label">
            <!--prefix-icon表示前缀图标-->
            <el-input
              placeholder="请输入用户名"
              v-model="form.username"
              :prefix-icon="User"
            ></el-input>
          </el-form-item>
          <!--prop表示当前item需要验证的属性, 他是el-form的 model的属性名 -->
          <el-form-item label="密码" prop="password">
            <!-- show-password表示密码框, 并且会在suffix-icon上添加一个小眼睛, 表示密码是否可见 -->
            <!-- 原先的使用type="password"表示密码框的方式已经过期了-->
            <el-input
              autocomplete="off"
              v-model="form.password"
              :prefix-icon="Lock"
              placeholder="请输入密码"
              show-password
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-row justify="space-evenly" class="btn-row">
              <el-col :span="9">
                <el-button
                  type="primary"
                  @click="submit(form)"
                  class="login_btn"
                  :loading="loading"
                  :disabled="loading"
                >
                  登录
                </el-button>
              </el-col>
              <el-col :span="9">
                <!--todo 很奇怪, 如果在这里调用reset()方法, 然后在reset()中调用resetFields就可以清除内容,
                但是如果在这里直接调用formRef.value.resetFields()却不行, 报错说resetFields为undefined -->
                <el-button type="primary" @click="reset()">重置</el-button>
              </el-col>
            </el-row>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped lang="scss">
.login-container {
  width: 100%;
  height: 100vh; // 1vh= 1%视口高度的长度, 1vw= 1%视口宽度的长度
  background: url(@/assets/images/background.jpg) no-repeat; // 如果背景图像的宽度或高度小于容器的宽度或高度, 不重复图片
  background-size: cover; // 自动伸缩填充容器, 并保存图像的纵横比, 如果背景图像的宽度或高度大于容器的宽度或高度, 则背景图像的某些部分将被裁剪以适应容器
}

.login-form {
  position: relative;
  padding: 40px;
  width: 50%;
  top: 30vh;
  background: url('@/assets/images/login_form.png');
  background-size: cover;

  h1 {
    color: white;
    font-size: 40px;
  }

  h2 {
    margin: 20px 0;
    font-size: 20px;
    color: white;
  }

  :deep(.el-form-item__label) {
    color: white;
  }

  .btn-row {
    width: 100%;

    .el-button {
      width: 100%;
    }
  }
}
</style>
