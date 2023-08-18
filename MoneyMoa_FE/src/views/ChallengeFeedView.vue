<template>
  <v-container>
    <v-row>
      <v-col cols="12" class="fiexd ma-auto">
        <SearchBar v-if="shouldShowSearchBar" />
      </v-col>
      <v-col cols="12" class="fiexd ma-auto">
        <PostFeed />
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <router-view></router-view>
      </v-col>
    </v-row>
  </v-container>
</template>
<script setup>
import { ref, computed } from 'vue'
import SearchBar from '@/components/ChallengeFeed/item/SearchBar.vue'
import PostFeed from '@/components/ChallengeFeed/item/PostFeed.vue'

// 특정 라우터 뷰에서만 SearchBar를 보여줄지 여부를 계산하는 computed 속성
const shouldShowSearchBar = computed(() => {
  // window.location.pathname가 '/challenge/feed/post'인 경우에는 false를 반환
  return window.location.pathname !== '/challenge/feed/post'
})
</script>
<style>
.align-center {
  text-align: center;
}
.no-margin-padding {
  margin: 0;
  padding: 0;
}
</style>
