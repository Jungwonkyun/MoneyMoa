<template>
  <v-container>
    <h1>ChallengeFeedList</h1>
    <SearchBar />
    <v-row class="card-container" justify="center">
      <v-card class="result ma-2" v-for="comment in comments" :key="comment.id" text-center>
        <v-col>
          <v-img src="https://cdn.vuetifyjs.com/images/parallax/material.jpg"></v-img>
        </v-col>
        <v-col>{{ comment.email }}</v-col>
        <v-col>{{ comment.id }}</v-col>
      </v-card>
      <InfiniteLoading @infinite="load" />
    </v-row>
  </v-container>
</template>

<script setup>
import { ref } from 'vue'
import SearchBar from '@/components/Challenge/item/SearchBar.vue'
import InfiniteLoading from 'v3-infinite-loading'
import 'v3-infinite-loading/lib/style.css'

let comments = ref([])
let page = 1
const load = async ($state) => {
  console.log('loading...')

  try {
    const response = await fetch(
      'https://jsonplaceholder.typicode.com/comments?_limit=10&_page=' + page
    )
    const json = await response.json()
    if (json.length < 10) $state.complete()
    else {
      comments.value.push(...json)
      $state.loaded()
    }
    page++
  } catch (error) {
    $state.error()
  }
}
</script>

<style>
.result {
  gap: 5px;
  font-weight: 300;
  width: 500px;
  text-align: center;
  background: #eceef0;
}

.card-container {
  max-height: 600px; /* 화면에 보여질 최대 높이 설정 */
  overflow-y: auto; /* 스크롤을 추가해야하는 영역을 정의 */
  width: 700px;
  margin: 0 auto;
}
</style>
