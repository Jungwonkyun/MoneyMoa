<template>
  <v-container>
    <v-row>
      <v-col cols="11" class="ma-auto">
        <SearchBar v-if="shouldShowSearchBar" @custom-event="handleEvent" />
      </v-col>
      <v-col cols="1" class="ma-auto">
        <PostFeed />
      </v-col>
    </v-row>
    <router-view :search-word="searchWord"></router-view>
  </v-container>
</template>
<script setup>
import { ref, computed } from 'vue'
import SearchBar from '@/components/ChallengeFeed/item/SearchBar.vue'
import PostFeed from '@/components/ChallengeFeed/item/PostFeed.vue'

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
<style>
.align-center {
  text-align: center;
}
.no-margin-padding {
  margin: 0;
  padding: 0;
}
</style>
