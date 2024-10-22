<template>
  <div style="width: 100vw;height: 100vh;overflow: hidden;display: flex">
    <div style="flex: 1">
      <el-image style="width: 100%;height: 100%;" fit="cover"
                src="https://www.4kbizhi.com/d/file/2024/09/02/small092618Nlwz81725240379.jpg"/>
    </div>
    <div class="welcome-title">
      <div style="font-size: 30px;font-weight: bold">欢迎来到上班群官方网站</div>
      <div style="margin-top: 10px">兄弟们好！张哥，马哥，欢迎到来~~~</div>
      <div style="margin-top: 10px">我们专注于《火影忍者》、《炉石传说》、《英雄联盟》、《地下城与勇士》等各类游戏，非常的帅气！！！</div>
    </div>
    <div style="width: 400px;background-color: white;z-index: 1">
      <router-view v-slot="{ Component }">
        <transition name="el-fade-in-linear">
          <component :is="Component" style="height: 100%" />
        </transition>
      </router-view>
    </div>
  </div>
</template>

<script setup>

</script>

<style scoped>
.welcome-title {
  position: absolute;
  bottom: 30px;
  left: 30px;
  color: white;
  text-shadow: 0 0 10px black;
}
</style>