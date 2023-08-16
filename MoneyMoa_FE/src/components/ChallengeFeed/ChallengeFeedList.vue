<template>
  <v-row class="card-container" justify="center">
    <v-card class="result ma-2" v-for="feed in feeds" :key="feed.id" text-center>
      <v-col>
        <v-carousel hide-delimiter-background show-arrows="hover">
          <v-carousel-item v-for="(fileUrl, index) in feed.fileUrls" :key="index">
            <router-link :to="'/challenge/feed/' + feed.id">
              <v-img :src="fileUrl" height="100%"></v-img>
            </router-link>
          </v-carousel-item>
        </v-carousel>
      </v-col>
      <v-col>{{ feed.id }}</v-col>
      <v-col>
        <v-card-text>{{ feed.content }}</v-card-text>
      </v-col>
      <v-col>{{ feed.hashtag }}</v-col>
      <v-col>{{ feed.nickname }}</v-col>
    </v-card>
    <InfiniteLoading @infinite="load" />
  </v-row>
</template>

<script setup>
import { ref, watchEffect, onMounted } from 'vue'
import InfiniteLoading from 'v3-infinite-loading'
import 'v3-infinite-loading/lib/style.css'
import { useChallengeFeedStore } from '../../stores/challengeFeedStore'
import { storeToRefs } from 'pinia'
import challengeFeedApi from '@/api/challengeFeed.js'

// 스토어 사용
const challengeFeedStore = useChallengeFeedStore()
const { searchWord } = storeToRefs(challengeFeedStore)

let feeds = ref([])

const load = async ($state) => {
  console.log('loading...')

  try {
    // 만약 searchWord가 존재한다면 검색어를 통해 피드를 불러온다.
    if (searchWord.value) {
      challengeFeedApi.searchFeed(searchWord.value).then((response) => {
        console.log(response.data)
        const data = response.data.reverse()
        // 만약 데이터가 2개 이하라면
        // $state.complete()를 호출하여 더 이상 데이터를 로딩하지 않고 완료 상태로 변경
        if (data.length < 2) $state.complete()
        else {
          // feeds.value에 모든 data 배열의 모든 요소를 병합
          feeds.value.push(...data)
          // $state.loaded()를 호출하여 더 많은 데이터를 요청할 수 있도록 로딩 상태를 유지
          $state.loaded()
        }
      })
    } else {
      // searchWord가 존재하지 않는다면 모든 피드를 불러온다.
      challengeFeedApi.fetchAllFeedList().then((response) => {
        console.log(response.data.feedList)
        const data = response.data.feedList.reverse()
        // 만약 데이터가 2개 이하라면
        // $state.complete()를 호출하여 더 이상 데이터를 로딩하지 않고 완료 상태로 변경
        if (data.length < 2) $state.complete()
        else {
          // feeds.value에 모든 data 배열의 모든 요소를 병합
          feeds.value.push(...data)
          // $state.loaded()를 호출하여 더 많은 데이터를 요청할 수 있도록 로딩 상태를 유지
          $state.loaded()
        }
      })
    }
  } catch (error) {
    $state.error()
  }
}
// `searchWord` 변경 시 `load` 함수 호출
watchEffect(() => {
  console.log('searchWord', searchWord.value)
  feeds.value = []
  // `load` 함수 호출
  load({
    complete() {},
    loaded() {},
    error() {}
  })
})

onMounted(() => {
  searchWord.value = ''
})

console.log(feeds.value)
</script>

<style>
.result {
  gap: 5px;
  font-weight: 300;
  width: 100%;
  height: 100%;
  text-align: center;
  background: #f4f4f4;
}

.card-container {
  max-height: 600px; /* 화면에 보여질 최대 높이 설정 */
  overflow-y: auto; /* 스크롤을 추가해야하는 영역을 정의 */
  width: 1000px;
  margin: 0 auto;
}
</style>
