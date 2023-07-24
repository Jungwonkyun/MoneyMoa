<template>
  <div class="container">
    <div class="left">
      <UserInfo />
    </div>
    <div class="right">
      <router-view />
    </div>
  </div>
</template>

<script>
import UserInfo from '@/components/Member/UserInfo.vue'

export default {
  components: {
    UserInfo
  }
}
</script>

<style>
.container {
  display: flex;
}

.left {
  flex: 2;
  padding: 20px;
}

.right {
  flex: 3;
  padding: 20px;
}
</style>
