<template>
  <v-toolbar :elevation="3" id="header">
    <v-toolbar-title><router-link to="/">MoneyMoa</router-link></v-toolbar-title>
    <router-link to="/products"><v-btn>내 돈 굴리기</v-btn></router-link>
    <router-link to="/challenge"><v-btn>챌린지</v-btn></router-link>
    <router-link to="/feed"><v-btn>피드</v-btn></router-link>
    <router-link to="/dictionary"><v-btn>금융사전</v-btn></router-link>
    <v-spacer></v-spacer>
    <router-link to="/"
      ><v-btn><v-icon icon="mdi-bell-outline" /></v-btn
    ></router-link>
    <router-link to="/"
      ><v-btn><v-icon icon="mdi-send" /></v-btn
    ></router-link>
    <router-link :to="`/member/${memberId}`"><v-btn>프로필</v-btn></router-link>
    <router-link to="/account"><v-btn>로그인</v-btn></router-link>
  </v-toolbar>
</template>
<script setup>
import { useAccountStore } from '@/stores/accountStore.js'

const accountStore = useAccountStore()
const memberId = accountStore.memberId
</script>
<style lang="scss">
// #header {
//   // background-color: $primary-color;
// }
</style>
