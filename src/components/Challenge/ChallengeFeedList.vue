<template>
  {{ searchWord }}
  <v-row class="card-container" justify="center">
    <v-card class="result ma-2" v-for="comment in comments" :key="comment.id" text-center>
      <v-col>
        <router-link :to="'/challenge/feed/' + comment.id">
          <v-img src="https://cdn.vuetifyjs.com/images/parallax/material.jpg"></v-img>
        </router-link>
      </v-col>
      <v-col>{{ comment.email }}</v-col>
      <v-col>{{ comment.id }}</v-col>
    </v-card>
    <InfiniteLoading @infinite="load" />
  </v-row>
</template>

<script setup>
import { ref, defineProps, computed, watch } from 'vue'
import InfiniteLoading from 'v3-infinite-loading'
import 'v3-infinite-loading/lib/style.css'
import axios from 'axios'
import funtions from '@/api/member.js'
import { apiInstance } from '@/api/index.js'

const api = apiInstance()

const props = defineProps({
  searchWord: null
})

// const test = () => {
//   axios.get('https://feed/' + props.searchWord).then((res) => {
//     console.log(res)
//   })
// }

// watch(props.searchWord, () => {
//   test()
// })

// let comments = ref([])
// let page = 1
// const load = async ($state) => {
//   console.log('loading...')

//   try {
//     const response = await fetch(
//       'https://jsonplaceholder.typicode.com/comments?_limit=10&_page=' + page
//     )
//     const json = await response.json()
//     if (json.length < 3) $state.complete()
//     else {
//       comments.value.push(...json)
//       $state.loaded()
//     }
//     page++
//   } catch (error) {
//     $state.error()
//   }
// }

// let comments = ref([])
// const load = async ($state) => {
//   console.log('loading...')

//   try {
//     const headers = {
//       Authorization: `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMCIsImV4cCI6MTY5MTA0NzExOX0.dG2x-6yl9PRgVzxiHU33l0Ayx1VwwsJqkSJKeKwABx78rv5ejtKqNno6HVDZ_aSPEw95SKzmJZMH7D2CspfC0Q`
//     }
//     const res = await api.get(`/feed/all`, { headers })
//     const json = await res.json()
//     console.log(json)
//     if (json.length < 3) $state.complete()
//     else {
//       comments.value.push(...json)
//       $state.loaded()
//     }
//   } catch (error) {
//     $state.error()
//   }
// }

async function test() {
  try {
    const headers = {
      Authorization: `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMCIsImV4cCI6MTY5MTA0NzExOX0.dG2x-6yl9PRgVzxiHU33l0Ayx1VwwsJqkSJKeKwABx78rv5ejtKqNno6HVDZ_aSPEw95SKzmJZMH7D2CspfC0Q`
    }
    const res = await api.get(`/feed/all`, { headers })
    return res
  } catch (err) {
    console.log(err)
  }
}

test().then((res) => {
  console.log(res)
})
</script>

<style>
.result {
  gap: 5px;
  font-weight: 300;
  width: 100%;
  height: 550px;
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
