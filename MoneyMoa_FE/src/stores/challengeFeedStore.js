import { defineStore } from 'pinia'
import { ref } from 'vue'
export const useChallengeFeedStore = defineStore('challengeFeed', () => {
  // 검색어
  const searchWord = ref([1, 2, 3])
  return {
    searchWord
  }
})
