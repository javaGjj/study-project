<script setup>

import {get} from "@/net/index.js";
import {ElMessage} from "element-plus";
import {useStore} from "@/stores/index.js";
import router from "@/router/index.js";

const store = useStore()

if (store.auth.user == null) {
  get('/api/user/me', (message) => {
    store.auth.user = message
    router.push('index')
  }, () => {
    store.auth.user = null
  })
}

</script>

<template>
  <router-view/>
</template>

<style scoped>

</style>
