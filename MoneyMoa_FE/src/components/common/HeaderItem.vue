<template>
  <v-toolbar :elevation="3" id="header" fixed class="d-flex flex-row align-center">
    <v-toolbar-title>
      <router-link to="/">
        <span class="header-title no-wrap">MoneyMoa</span>
      </router-link>
    </v-toolbar-title>
    <!-- <v-spacer /> -->
    <v-toolbar-items class="align-center">
      <router-link to="/products"><v-btn class="mx-4">내 돈 굴리기</v-btn></router-link>
      <router-link to="/challenge"><v-btn class="mx-4">챌린지</v-btn></router-link>
      <router-link to="/chat"><v-btn class="mx-4">오픈채팅</v-btn></router-link>
      <router-link to="/dictionary"><v-btn class="mx-4">금융사전</v-btn></router-link>
      <router-link to="/admin" v-if="isAdmin"><v-btn>관리자 페이지</v-btn></router-link>
    </v-toolbar-items>
    <!-- <v-spacer /> -->
    <v-toolbar-items class="align-center">
      <router-link to="/chat/dmlist"> <v-btn icon="mdi-send" /></router-link>
      <ProfileMenu v-if="isLogin" />
      <router-link to="/account" v-else><v-btn>로그인</v-btn></router-link>
    </v-toolbar-items>
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
.v-toolbar__content {
  justify-content: space-around;
}
.v-toolbar-title {
  max-width: fit-content;
}
.header-title {
  text-overflow: clip;
}
.no-wrap {
  white-space: nowrap;
}
</style>
