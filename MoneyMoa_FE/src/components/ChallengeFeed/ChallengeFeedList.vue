<template>
  <v-row class="card-container" justify="center">
    <v-card class="result ma-2" v-for="feed in feeds" :key="feed.id" text-center>
      <v-col>
        <router-link :to="'/challenge/feed/' + feed.id">
          <v-img src="https://cdn.vuetifyjs.com/images/parallax/material.jpg"></v-img>
        </router-link>
      </v-col>
      <v-col>{{ feed.id }}</v-col>
      <v-col>{{ feed.content }}</v-col>
      <v-col>{{ feed.createDateTime }}</v-col>
      <v-col>{{ feed.hashtag }}</v-col>
      <v-col>{{ feed.nickname }}</v-col>
    </v-card>
    <InfiniteLoading @infinite="load" />
  </v-row>
</template>

<script setup>
import { ref, defineProps } from 'vue'
import InfiniteLoading from 'v3-infinite-loading'
import 'v3-infinite-loading/lib/style.css'
import { apiInstance } from '@/api/index.js'
import { useCookies } from 'vue3-cookies'

const { cookies } = useCookies()

const api = apiInstance()

const props = defineProps({
  searchWord: null
})

let feeds = ref([])
const load = async ($state) => {
  console.log('loading...')

  try {
    const token = cookies.get('accessToken')
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const res = await api.get(`/feed/all`, { headers })
    console.log(res.data.feeds)
    const data = res.data.feeds.feed
    // 만약 데이터가 2개 이하라면
    // $state.complete()를 호출하여 더 이상 데이터를 로딩하지 않고 완료 상태로 변경
    if (data.length < 2) $state.complete()
    else {
      // feeds.value에 모든 data 배열의 모든 요소를 병합
      feeds.value.push(...data)
      // $state.loaded()를 호출하여 더 많은 데이터를 요청할 수 있도록 로딩 상태를 유지
      $state.loaded()
    }
  } catch (error) {
    $state.error()
  }
}
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
  width: 700px;
  margin: 0 auto;
}
</style>
