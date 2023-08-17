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
      <router-link to="/challenge" @click="deleteSearchWord"
        ><v-btn class="mx-4">챌린지</v-btn></router-link
      >
      <router-link to="/chat"><v-btn class="mx-4">오픈채팅</v-btn></router-link>
      <router-link to="/dictionary"><v-btn class="mx-4">금융사전</v-btn></router-link>
      <router-link to="/admin" v-if="isAdmin"
        ><v-btn class="mx-4">관리자 페이지</v-btn></router-link
      >
    </v-toolbar-items>
    <!-- <v-spacer /> -->
    <v-toolbar-items class="align-center">
      <router-link to="/chat/dmlist" v-if="isLogin"> <v-btn icon="mdi-send" /></router-link>
      <ProfileMenu v-if="isLogin" />
      <router-link to="/account" v-else><v-btn>로그인</v-btn></router-link>
    </v-toolbar-items>
  </v-toolbar>
</template>
<script setup>
import { useAccountStore } from '@/stores/accountStore.js'
import { useChallengeFeedStore } from '../../stores/challengeFeedStore'
import ProfileMenu from '../Accounts/ProfileMenu.vue'
import { storeToRefs } from 'pinia'

const account = useAccountStore()

// 챌린지 피드 스토어 사용
const challengeFeedStore = useChallengeFeedStore()
const { searchWord } = storeToRefs(challengeFeedStore)

const deleteSearchWord = () => {
  searchWord.value = ''
}

const { isLogin, isAdmin } = storeToRefs(account)

// const { isAdmin } = storeToRefs(account)
// 관리자인지 확인하기
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
.v-btn i {
  color: $logo-color;
}
</style>
