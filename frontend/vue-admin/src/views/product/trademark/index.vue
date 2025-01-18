<script setup lang="ts">
import { Delete, Plus, ZoomIn } from '@element-plus/icons-vue'
import { reactive, ref, watch } from 'vue'
import {
  reqAddOrUpdateTrademark,
  reqDeleteTrademark,
  reqTrademark,
} from '@/api/product/trademark'
import { Records } from '@/api/product/trademark/type.ts'
import { ElMessage, UploadProps, UploadUserFile } from 'element-plus'
import type { UploadInstance } from 'element-plus'
import request from '@/utils/request.ts'
import { reqUpload } from '@/api/file'

const pageNum = ref(1)
const pageSize = ref(7)
const data = ref<Records[]>([])
const total = ref(0)

function refresh() {
  reqTrademark(pageNum.value, pageSize.value).then((res) => {
    data.value = res.records
    total.value = res.total
  })
}

// 监控pageNum和pageSize的变化, 当pageNum和pageSize发生变化时, 重新发送请求, 获取数据
// 并且立即执行一次, 用于初始化数据
watch(
  [pageNum, pageSize],
  ([newNum, newSize], [oldNum, oldSize]) => {
    // 页大小发生变化时, 页码重置为1, 因为页大小发生变化时, 页码可能会超出最大页码
    // 第一次进入页面时, oldSize为undefined, 此时不需要重置页码
    if (oldSize && oldSize !== newSize) {
      pageNum.value = 1
    }
    refresh()
  },
  { immediate: true },
)

const uploader = ref<UploadInstance>() // 获取上传组件的实例
const fileList = ref<UploadUserFile[]>([]) // 上传组件的文件列表

const dialogData = reactive({
  visible: false, // 对话框是否显示
  type: 0, // 对话框类型, 0表示添加, 1表示修改
  title() {
    return this.type === 0 ? '添加品牌' : '修改品牌'
  },
  form: {
    id: '', // 修改时需要id, 添加时不需要
    name: '',
    logoUrl: '',
  },
})
const rules = reactive({
  "name": [
    { required: true, message: '请输入品牌名称', trigger: 'blur' },
  ],
  "logoUrl": [
    { validator: (rule, value, callback) => {
      fileList.value.length >= 1 ? callback() : callback(new Error('请上传品牌图片'))
    }, trigger: 'blur' },
  ],
})
const formRef = ref()
async function handleAdd() {
  await formRef.value.validate() // 校验表单

  // 上传图片, 获取图片的url, 已经关闭了组件的自动上传功能, 需要手动上传
  uploadImage()
    .then((url) => {
      dialogData.form.logoUrl = url
      fileList.value[0].url = url
    })
    .then(() => {
      // 提交表单
      return reqAddOrUpdateTrademark(dialogData.form.name, dialogData.form.logoUrl)
    })
    .then(() => {
      // 关闭对话框
      dialogData.visible = false
      refresh() // 添加之后刷新数据
    })
}

async function handleEdit() {
  await formRef.value.validate() // 校验表单

  // 判断是否修改了图片, 如果修改了图片, 需要重新上传图片, 获取图片的url
  if (fileList.value[0].url?.startsWith('blob')) {
    await uploadImage().then((url) => {
      dialogData.form.logoUrl = url
      fileList.value[0].url = url
    })
  }
  await reqAddOrUpdateTrademark(dialogData.form.name, dialogData.form.logoUrl, Number(dialogData.form.id))
  dialogData.visible = false
  refresh() // 修改之后刷新数据
}

function handleDelete(id: number) {
  reqDeleteTrademark(id).then(() => {
    ElMessage.success('删除成功')
    refresh()
  })
}

// 打开添加对话框时清空数据
function openAddDialog() {
  dialogData.type = 0
  dialogData.visible = true
  dialogData.form.name = ''
  dialogData.form.logoUrl = ''
  dialogData.form.id = ''
  fileList.value = [] // 清空上传组件的文件列表
}

// 打开修改对话框时填充数据
function openEditDialog(id: number, name: string, logoUrl: string) {
  dialogData.type = 1 // 设置对话框类型和数据
  dialogData.visible = true
  dialogData.form.name = name
  dialogData.form.logoUrl = logoUrl
  dialogData.form.id = String(id)
  fileList.value = [
    {
      name: 'logo.png',
      url: logoUrl,
    },
  ]
}

// 上传图片之前校验文件类型和大小
const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
  console.log(rawFile.type)
  if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
    ElMessage.error('Avatar picture must be JPG/PNG format!')
    return false
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('Avatar picture size can not exceed 2MB!')
    return false
  }
  return true
}

// 图片删除时调用
function handleAvatarRemove(file: UploadUserFile) {
  // 过滤掉fileList中的file
  fileList.value = fileList.value.filter((item) => item !== file)
  // 清除掉dialog上的logoUrl
  dialogData.form.logoUrl = ''
}
// 图片超出限制时调用
function handleExceed(files: UploadUserFile[], fileList: UploadUserFile[]) {
  ElMessage.warning('只能选择一个图片, 请先删除已经选择的图片, 再选择新的图片')
}
// 图片上传, 通过el-upload的http-request属性指定上传图片的函数
// 在el-upload的实例ref上调用submit方法, 会触发http-request指定的函数
async function uploadImage() {
  let formData = new FormData()
  formData.append('file', fileList.value[0].raw as File)
  return await reqUpload(formData)
}
</script>

<template>
  <el-card class="box-card">
    <el-button type="primary" :icon="Plus" @click="openAddDialog">
      添加品牌
    </el-button>
    <!--表格内容-->
    <!--
        data: 渲染表格的数据
        border: 是否显示边框
        height: 设置表格的高度, 设置该属性后, 在表格中数据上下滑动时, 表头固定
    -->
    <el-table :data="data" border height="75vh">
      <!--
        prop: 用于指定当前列显示的数据的属性
        label: 用于指定当前列的标题
        width: 用于指定当前列的宽度
        align: 用于指定当前列的对齐方式
        fixed: 用于指定表格左右滑动时, 当前列是否固定在左侧或者右侧
        type: 当前列的类型, index表示当前列是索引列, 如果不设置, 则不会显示索引列

        slot:
            default: 表示当前列的内容, 如果设置了该属性, 则当前列的内容会被替换为slot中的内容
            header: 表示当前列的表头, 如果设置了该属性, 则当前列的表头会被替换为slot中的内容
      -->
      <el-table-column type="index" label="序号" width="70px" fixed="left" />
      <el-table-column
        prop="tmName"
        label="品牌名称"
        align="center"
        width="120px"
      />
      <el-table-column label="品牌LOGO" align="center">
        <template #default="{ row, column, $index }">
          <img
            :src="row.logoUrl"
            style="height: 100px; width: 100px"
            alt="logo"
          />
        </template>
      </el-table-column>
      <el-table-column label="品牌操作" align="center" width="160px">
        <template #default="{ row, column, $index }">
          <!--
          row: 当前行的数据
          $index: 当前行的索引, 从0开始
          column: 当前列的一些属性
          -->
          <el-button
            type="warning"
            size="small"
            @click="openEditDialog(row.id, row.tmName, row.logoUrl)"
          >
            修改
          </el-button>
          <el-button type="danger" size="small" @click="handleDelete(row.id)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--分页器-->
    <!--
      current-page: 当前的页码
      page-size: 页大小
      page-sizes: 用于设置页大小的可选项
      background: 是否给页码按钮添加背景色
      layout: 用于设置分页器各个组件的顺序
                total: 共xxx页
                sizes: 选择当前也大小的下拉框
                prev: 上一页按钮
                pager: 分页按钮
                next: 下一页按钮
                jumper: 跳转到xxx页按钮
                ->: 向右对齐
       total: 总的数据量
       pager-count: 设置分页器页码按钮的数量, 大于5且小于等于21的奇数!!!!
       @current-change: 当前页码改变时触发, 不管是点击页码按钮, 还是点击上一页, 下一页按钮, 还是点击跳转到xxx页按钮, 都会触发该事件
       @size-change: 当页大小改变时触发
        -->
    <el-pagination
      v-model:current-page="pageNum"
      v-model:page-size="pageSize"
      :page-sizes="[7, 3, 5, 9]"
      :background="true"
      :pager-count="5"
      layout="prev, pager, next, jumper, ->, sizes, total"
      :total="total"
    />

    <!-- 修改或者添加对话框 -->
    <el-dialog
      v-model="dialogData.visible"
      :title="dialogData.title()"
      width="500px"
    >
      <el-form label-width="80px" :model="dialogData.form" :rules="rules" ref="formRef">
        <el-form-item label="品牌名称" prop="name">
          <el-input v-model="dialogData.form.name" />
        </el-form-item>
        <!--添加一个隐藏的输入框, 来做logoUrl的校验, 防止没有url的时候进行提交-->
        <el-form-item prop="logoUrl" v-show="false">
          <el-input v-model="dialogData.form.logoUrl" />
        </el-form-item>
        <el-form-item label="品牌LOGO">
          <!--
          这个upload一旦选择好图片后就会直接调用后端上传图片, 在取消掉图片后又会调用后端上传图片
          auto-upload: 是否在选择图片后立即自动上传
          action: 上传图片的地址 !!!!!!!!一定要记得加上baseApi, 如果不想自动上传, 可以设置为 #
          v-model:file-list: 选中的文件列表
          list-type: 上传文件的展示类型, text表示以文字的形式展示, picture表示以卡片的形式展示, picture-card表示以缩略图的形式
          show-file-list: 是否显示文件列表
          on-success: 上传成功后的回调, 参数是后端返回的数据, 一般用在自动上传的情况下
          before-upload: 上传前的回调, 如果返回false, 则不会上传图片, 可以在这个回调中做图片的格式和大小的校验
          on-remove: 删除图片后的回调
          method: 上传图片的方式, 默认是post
          drag: 是否支持拖拽上传
          accept: 选择文件时只能选择的类型 image/png, image/jpg, image/jpeg, image/gif
          http-request: 自定义的图片上传函数, 会覆盖掉默认的上传函数, 一般用在手动上传的情况下
          on-exceed: 文件超出个数限制时的回调
          -->
          <el-upload
            accept="image/png, image/jpg, image/jpeg, image/gif"
            list-type="picture-card"
            :auto-upload="false"
            v-model:file-list="fileList"
            :before-upload="beforeAvatarUpload"
            ref="uploader"
            :drag="true"
            :limit="1"
            :on-exceed="handleExceed"
          >
            <template #trigger>
              <el-icon><Plus /></el-icon>
            </template>
            <template #file="{ file }">
              <div>
                <img
                  class="el-upload-list__item-thumbnail avatar"
                  :src="file.url"
                  alt=""
                />
                <span class="el-upload-list__item-actions">
                  <span
                    class="el-upload-list__item-preview"
                    @click="handlePictureCardPreview(file)"
                  >
                    <el-icon><zoom-in /></el-icon>
                  </span>
                  <span
                    class="el-upload-list__item-delete"
                    @click="handleAvatarRemove(file)"
                  >
                    <el-icon><Delete /></el-icon>
                  </span>
                </span>
              </div>
            </template>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogData.visible = false">取消</el-button>
          <el-button
            type="primary"
            @click="dialogData.type === 0 ? handleAdd() : handleEdit()"
          >
            {{ dialogData.type === 0 ? '添加' : '修改' }}
          </el-button>
        </span>
      </template>
    </el-dialog>
  </el-card>
</template>

<style scoped lang="scss">
.el-table {
  margin-top: 15px;
  margin-bottom: 15px;
}

:deep(.el-upload-dragger) {
  width: 100%;
  height: 100%;
}
</style>
