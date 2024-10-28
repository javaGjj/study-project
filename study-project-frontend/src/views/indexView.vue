<template>
  <el-container>
    <el-header height="60px" class="header">
      <div class="header-content">
        <span>上班群官方网站</span>
        <el-button @click="logout" type="danger" plain class="logout-btn">退出登录</el-button>
      </div>
    </el-header>

    <el-main class="main">
      <el-card class="welcome-card" shadow="hover">
        <div class="welcome-message">
          欢迎 <span class="username">{{store.auth.user.username}}</span> 进入上班群官方网站
        </div>
      </el-card>
    </el-main>
  </el-container>
</template>

<style scoped>
.header {
  background-color: #333;
  color: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}
.header-content span {
  font-size: 20px;
  font-weight: bold;
}
.logout-btn {
  transition: background-color 0.3s;
}
.logout-btn:hover {
  background-color: #ff4d4f;
  color: #fff;
}
.main {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 80vh;
  background-color: #f5f5f5;
}
.welcome-card {
  width: 400px;
  padding: 20px;
  text-align: center;
}
.welcome-message {
  font-size: 18px;
  font-weight: 500;
  color: #333;
}
.username {
  color: #409eff;
  font-weight: bold;
}
</style>

<script setup>
import { get } from "@/net/index.js";
import { ElMessage } from "element-plus";
import router from "@/router/index.js";
import { useStore } from "@/stores/index.js";

const store = useStore();

const logout = () => {
  get("/api/auth/logout", (message) => {
    ElMessage.success(message);
    store.auth.user = null;
    router.push("/");
  });
};
</script>
