<template>
  <v-container>
    <SearchBar v-if="shouldShowSearchBar" @custom-event="handleEvent" />
    <router-view :search-word="searchWord"></router-view>
  </v-container>
</template>
<script setup>
import { ref, computed } from 'vue'
import SearchBar from '@/components/Challenge/item/SearchBar.vue'

const searchWord = ref(null)

const handleEvent = (word) => {
  console.log(typeof word)
  // console.log(word)
  searchWord.value = word
}

// 특정 라우터 뷰에서만 SearchBar를 보여줄지 여부를 계산하는 computed 속성
const shouldShowSearchBar = computed(() => {
  // window.location.pathname가 '/challenge/feed/post'인 경우에는 false를 반환
  return window.location.pathname !== '/challenge/feed/post'
})
</script>
<style></style>
