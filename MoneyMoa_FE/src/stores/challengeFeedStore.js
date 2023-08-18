import { defineStore } from 'pinia'
import { ref, watchEffect } from 'vue'
import { useRouter } from 'vue-router'
export const useChallengeFeedStore = defineStore('challengeFeed', () => {
  // 검색어
  const searchWord = ref('')

  //useRouter

  // 검색어 변경되면 챌린지 전체 목록 라우터로 이동

  return {
    searchWord
  }
})
