<template>
  <div style="text-align: center;margin:0 20px">
    <div style="text-align: center;margin-top: 100px">
      <div style="font-size: 25px;font-weight: bold">注册新用户</div>
      <div style="font-size: 14px;color: grey">欢迎注册我们的学习平台，请在下方填写相关信息</div>
    </div>
    <div style="margin-top: 50px">
      <el-form :model="form" :rules @validate="onValidate" ref="formRef">
        <el-form-item prop="username">
          <el-input v-model="form.username" :maxlength="8" type="text" placeholder="用户名">
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" :maxlength="16" type="password" placeholder="密码">
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password_repeat">
          <el-input v-model="form.password_repeat" :maxlength="16" type="password" placeholder="重复密码">
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input v-model="form.email" type="text" placeholder="电子邮箱">
            <template #prefix>
              <el-icon><Message /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="code">
          <el-row gutter="10">
            <el-col :span="18">
              <el-input v-model="form.code" maxlength="6" type="text" placeholder="请输入电子邮件验证码">
                <template #prefix>
                  <el-icon><EditPen /></el-icon>
                </template>
              </el-input>
            </el-col>
            <el-col :span="6">
              <el-button @click="validateEmail" style="width: 100%" type="success"
                         :disabled="!isEmailValid || codeTime > 0">
                {{codeTime > 0 ? "请稍后"+codeTime+"秒" : "获取验证码"}}
              </el-button>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>

    </div>
    <div style="margin-top: 50px">
      <el-button style="width: 270px" type="warning" @click="register" plain>立即注册</el-button>
    </div>
    <div style="margin-top: 20px">
      <span style="font-size: 14px;line-height: 15px;color: grey">已有账号？</span>
      <el-link type="primary" style="translate: 0 -2px" @click="router.push('/')">立即登录</el-link>
    </div>
  </div>
</template>

<script setup>
import {EditPen, Lock, Message, User} from "@element-plus/icons-vue";
import router from "@/router/index.js";
import {reactive, ref} from "vue";
import {ElMessage} from "element-plus";
import {post} from "@/net/index.js";

const codeTime = ref(0)

const form = reactive({
  username: '',
  password: '',
  password_repeat: '',
  email: '',
  code: '',

})

const validateEmail = () => {
  codeTime.value = 60
  post('api/auth/valid-register-email', {
    email: form.email
  }, (message) => {
    ElMessage.success(message)
    setInterval(() => codeTime.value--, 1000)
  },(message) => {
    ElMessage.warning(message)
    codeTime.value = 0
  })
}

const validateUsername = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入用户名'))
  } else if (!/^[\u4e00-\u9fa5a-zA-Z0-9]+$/.test(value)) {
    callback(new Error('用户名不能包含特殊字符，只能是中/英文'))
  } else {
    callback()
  }
}

const validatePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== form.password) {
    callback(new Error("两次输入的密码不一致"))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { validator: validateUsername, trigger: ['blur', 'change']},
    { min: 2, max: 8, message: '用户名长度必须在2-8个字符之间', trigger: ['blur','change'] },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 16, message: '密码长度必须在6-16个字符之间', trigger: ['blur', 'change'] }
  ],
  password_repeat: [
    { validator: validatePassword, trigger: ['blur', 'change']},
  ],
  email: [
    { required: true, message: '请输入电子邮件地址', trigger: 'blur' },
    { type: 'email', message: '请输入合法的电子邮件地址', trigger: ['blur', 'change'], }
  ],
  code: [
    { required: true, message: '请输入获取的验证码', trigger: 'blur' },
  ]
}
const formRef = ref()
const isEmailValid = ref(false)

const onValidate = (prop, isValid) => {
  if (prop === 'email')
    isEmailValid.value = isValid
}

const register = () => {
  formRef.value.validate((isValid) => {
    if (isValid) {
      post("/api/auth/register", {
        username: form.username,
        password: form.password,
        email: form.email,
        code: form.code
      }, (message) => {
        ElMessage.success(message)
        router.push("/")
      })
    } else {
      ElMessage.warning('请完整填写注册表单内容！')
    }
  })
}
</script>

<style scoped>

</style>