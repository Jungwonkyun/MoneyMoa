<template>
  <v-text-field
    label="검색"
    placeholder="검색어는 공백으로 구분해주세요."
    variant="solo-filled"
    v-model="inputWord"
    type="text"
    @input="handleSearch"
    append-inner-icon="mdi-magnify"
    @click:append-inner="sendToStore(searchedWord)"
    @keyup.enter="sendToStore(searchedWord)"
    class="v-text-field"
  >
  </v-text-field>
</template>
<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useChallengeFeedStore } from '@/stores/challengeFeedStore'
import { storeToRefs } from 'pinia'

// useRouter 사용
const router = useRouter()

// 스토어 사용
const challengeFeedStore = useChallengeFeedStore()
const { searchWord } = storeToRefs(challengeFeedStore)

const inputWord = ref('')
const searchedWord = ref('')
const handleSearch = (e) => {
  inputWord.value = e.target.value

  //입력된 단어를 공백으로 분리하되 공백 자체는 제거
  const words = inputWord.value.split(' ').filter((word) => word !== '')
  const hashtags = []
  const normalWords = []

  // 단어들을 해시태그와 일반 검색어로 구분
  words.forEach((word) => {
    if (word.startsWith('#')) {
      hashtags.push(word)
    } else {
      normalWords.push(word)
    }
  })

  // console.log('해시태그', hashtags)
  // console.log('일반 단어', normalWords)

  // 해시태그의 길이가 0이 아니면 일반 검색어는 입력할 수 없다고 경고하고 새롭게 입력된 단어를 초기화 한다.
  if (hashtags.length !== 0 && normalWords.length !== 0) {
    alert('해시태그와 일반 검색어를 동시에 입력할 수 없습니다.')
    inputWord.value = ''
  }

  // 해시태그와 일반단어 중 길이가 0이 아닌 것을 검색어로 사용한다.
  searchedWord.value = hashtags.length !== 0 ? hashtags : normalWords

  // 검색어를 공백으로 구분된 문자열로 변환하여 출력
  const searchString = searchedWord.value.join(' ')
  searchedWord.value = searchString
}

const sendToStore = (searchedWord) => {
  searchWord.value = searchedWord
  router.push({ name: 'challengeFeedList' })
  inputWord.value = ''
}
</script>
<style scoped>
.v-text-field {
  width: 700px;
  margin: 0 auto;
}
</style>
