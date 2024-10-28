import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

import 'element-plus/dist/index.css'
import axios from "axios";

const app = createApp(App)
// 本地
// axios.defaults.baseURL = 'http://localhost:8080'
// 服務器
axios.defaults.baseURL = 'http://47.96.132.68:8080'

app.use(createPinia())
app.use(router)

app.mount('#app')
