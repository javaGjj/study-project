<template>
  <div style="width: 100vw;height: 100vh;overflow: hidden;display: flex">
    <div style="flex: 1">
      <el-image style="width: 100%;height: 100%;" fit="cover"
                src="https://www.4kbizhi.com/d/file/2024/09/02/small092618Nlwz81725240379.jpg"/>
    </div>
    <div class="welcome-title">
      <div style="font-size: 30px;font-weight: bold">欢迎来到我们的学习平台</div>
      <div style="margin-top: 10px">在这里你可以学习如何使用Java, 如何搭建网站，并与Java之父密切交流</div>
      <div style="margin-top: 10px">在这里你可以同性交友, 因为都是男的，没有学Java的女生</div>
    </div>
    <div style="width: 400px;background-color: white">
      <transition name="el-fade-in-linear">
        <router-view/>
      </transition>
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