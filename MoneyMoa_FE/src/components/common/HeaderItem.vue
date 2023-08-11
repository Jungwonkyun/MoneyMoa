<template>
  <v-toolbar :elevation="3" id="header" fixed>
    <v-toolbar-title class="header-title"
      ><router-link to="/">MoneyMoa</router-link></v-toolbar-title
    >
    <router-link to="/products"><v-btn>내 돈 굴리기</v-btn></router-link>
    <router-link to="/challenge"><v-btn>챌린지</v-btn></router-link>
    <router-link to="/chat"><v-btn>오픈채팅</v-btn></router-link>
    <router-link to="/dictionary"><v-btn>금융사전</v-btn></router-link>
    <router-link to="/admin" v-if="isAdmin"><v-btn>관리자 페이지</v-btn></router-link>
    <v-spacer></v-spacer>
    <router-link to="/"
      ><v-btn><v-icon icon="mdi-bell-outline" /></v-btn
    ></router-link>
    <router-link to="/"
      ><v-btn><v-icon icon="mdi-send" /></v-btn
    ></router-link>
    <ProfileMenu v-if="isLogin" />
    <router-link to="/account" v-if="!isLogin"><v-btn>로그인</v-btn></router-link>
  </v-toolbar>
</template>
<script setup>
import { useAccountStore } from '@/stores/accountStore.js'
import ProfileMenu from '../Accounts/ProfileMenu.vue'
import { storeToRefs } from 'pinia'
import { useCookies } from 'vue3-cookies'
import { computed } from 'vue'

const { cookies } = useCookies()
const account = useAccountStore()
const { isLogin } = storeToRefs(account)

// 관리자인지 확인하기
const isAdmin = computed(() => {
  if (!cookies.get('member')) {
    return false
  }
  return 'ADMIN' === cookies.get('member').role
})
</script>
<style lang="scss">
// #header {
//   // background-color: $primary-color;
// }
</style>
