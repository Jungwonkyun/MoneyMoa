<template lang="">
  <div>
    <v-container align="center"><h1>챌린지</h1></v-container>

    <v-container>
      <v-card
        mt-10
        v-for="(challenge, index) in challenges"
        :key="challenge.id"
        :style="{ margin: '50px' }"
      >
        <v-row>
          <v-col cols="2">
            <v-img :width="100" aspect-ratio="1/1" cover :src="challenge.img"></v-img>
          </v-col>
          <v-col cols="2">{{ challenge.title }}</v-col>
          <v-col cols="2">{{ challenge.content }}</v-col>
          <v-col cols="2">{{ challenge.hashtag }}</v-col>
          <v-col cols="2">{{ challenge.period }}</v-col>
          <v-col cols="2">{{ challenge.total_amount }}</v-col>
        </v-row>
      </v-card>
    </v-container>
  </div>
</template>
<script>
import { ref } from 'vue'
// 아래 주석 풀고 사용
// import functions from '@/api/member.js'

export default {
  setup() {
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

    return {
      challenges
    }
  }
}
</script>
<style lang=""></style>
