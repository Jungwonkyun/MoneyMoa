<template>
  <v-btn id="menu-activator" color="primary">
    <v-avatar> <v-img src="https://cdn.vuetifyjs.com/images/john.jpg" alt="John"></v-img> </v-avatar
  ></v-btn>
  <v-menu activator="#menu-activator">
    <v-list>
      <v-list-item :value="0">
        <v-list-item-title> 마이페이지 </v-list-item-title>
      </v-list-item>
      <v-list-item :value="1" @click="onProfilechange">
        <v-list-item-title> 회원정보 수정 </v-list-item-title>
      </v-list-item>
      <v-list-item :value="2">
        <v-list-item-title> 찜한 상품 목록 </v-list-item-title>
      </v-list-item>
      <v-list-item :value="3" @click="logout">
        <v-list-item-title class="logout">
          로그아웃
          <v-icon icon="mdi-logout" />
        </v-list-item-title>
      </v-list-item>
    </v-list>
  </v-menu>
</template>
<script setup>
import { useRouter } from 'vue-router'
import { useAccountStore } from '@/stores/accountStore.js'

const account = useAccountStore()
const router = useRouter()

// 로그아웃 로직 (페이지 이동후 새로고침)
function logout() {
  account.onLogout()
  alert('로그아웃 되었습니다.')
  router.push({ name: 'home' }).then(() => {
    location.reload()
  })
}

function onProfilechange() {
  return router.push({ name: 'profilechange' })
}
</script>
<style>
.logout {
  color: red;
}
</style>
