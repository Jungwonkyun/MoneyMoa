<template>
  <v-container>
    <h1 align="center">챌린지</h1>
    <v-card v-for="challenge in challenges" :key="challenge.id" class="mb-6" :elevation="10">
      <v-row class="d-flex justify-center align-center">
        <v-col cols="12" sm="4" class="text-center">
          <v-img cover :src="challenge.img" height="200"></v-img>
        </v-col>
        <v-col cols="12" sm="8">
          <v-row>
            <v-col cols="12">
              <v-card-title class="text-center">{{ challenge.title }}</v-card-title>
            </v-col>
            <v-col cols="12">
              <v-card-text class="text-center">{{ challenge.content }}{{ memberId }}</v-card-text>
            </v-col>
            <v-col cols="12">
              <v-card-text class="text-center">{{ challenge.hashtag }}</v-card-text>
            </v-col>
            <v-col cols="12">
              <v-card-text class="text-center">{{ challenge.period }}</v-card-text>
            </v-col>
            <v-col cols="12">
              <v-card-text class="text-center">{{ challenge.total_amount }}</v-card-text>
            </v-col>
          </v-row>
          <v-row class="d-flex justify-center">
            <v-col cols="6">
              <v-btn block>입장하기</v-btn>
            </v-col>
          </v-row>
          <div style="height: 20px"></div>
        </v-col>
      </v-row>
    </v-card>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import functions from '@/api/member.js'

const dummy = [
  {
    id: 1,
    title: '운동 안 갈 때 마다 돈 넣기',
    content: '제곧내',
    img: 'https://cdn.vuetifyjs.com/images/cards/cooking.png',
    hashtag: '운동 좀 해라',
    period: 'nn일 남음',
    total_amount: '80 % 달성!'
  },
  {
    id: 1,
    title: '운동 안 갈 때 마다 돈 넣기',
    content: '제곧내',
    img: 'https://cdn.vuetifyjs.com/images/cards/cooking.png',
    hashtag: '운동 좀 해라',
    period: 'nn일 남음',
    total_amount: '80 % 달성!'
  },
  {
    id: 1,
    title: '운동 안 갈 때 마다 돈 넣기',
    content: '제곧내',
    img: 'https://cdn.vuetifyjs.com/images/cards/cooking.png',
    hashtag: '운동 좀 해라',
    period: 'nn일 남음',
    total_amount: '80 % 달성!'
  }
]
const challenges = ref(dummy)

const route = useRoute()
const memberId = ref(route.params.id)

//// 마운트 시에 챌린지 리스트 API 호출, memberId가 변경되면 다시 호출
onMounted(() => {
  const res = functions.fetchChallengeList(memberId.value)
})
</script>
<style lang=""></style>
