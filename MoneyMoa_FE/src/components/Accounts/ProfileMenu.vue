<template>
  <v-btn id="menu-activator" color="primary">
    <v-avatar> <v-img :src="member.imageUrl" alt="John"></v-img> </v-avatar
  ></v-btn>
  <v-menu activator="#menu-activator">
    <v-list>
      <v-list-item :value="0" @click="onMypage">
        <v-list-item-title> 마이페이지 </v-list-item-title>
      </v-list-item>
      <v-list-item :value="1" @click="onMyproducts">
        <v-list-item-title> 찜한 상품 목록 </v-list-item-title>
      </v-list-item>
      <v-list-item :value="2" @click="onProfilechange">
        <v-list-item-title> 회원정보 수정 </v-list-item-title>
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
import { useCookies } from 'vue3-cookies'
import functions from '../../api/member.js'
const { cookies } = useCookies()
const account = useAccountStore()
const router = useRouter()
const member = cookies.get('member')
console.log(member)
function onMypage() {
  console.log(member.id)
  router.push({ name: 'member', params: { id: member.id } })
}

// 로그아웃 로직 (페이지 이동후 새로고침)
function logout() {
  if (member.oauthProvider === 'GENERAL') {
    account.onLogout()
    alert('로그아웃 되었습니다.')
    router.push({ name: 'home' }).then(() => {
      location.reload()
    })
  } else if (member.oauthProvider === 'KAKAO') {
    functions.openkakaoLogout()
  } else if (member.oauthProvider === 'NAVER') {
    account.onLogout()
    alert('로그아웃 되었습니다.')
    router.push({ name: 'home' }).then(() => {
      location.reload()
    })
  }
}

// 회원정보 변경페이지로 이동
function onProfilechange() {
  return router.push({ name: 'profilechange' })
}
// 찜목록 페이지로 이동
function onMyproducts() {
  return router.push({ name: 'myproducts', params: { id: member.id } })
}
</script>
<style scoped>
.logout {
  color: red;
}
</style>
