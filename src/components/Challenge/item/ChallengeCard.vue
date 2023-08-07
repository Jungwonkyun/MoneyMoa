<template>
  <v-card class="mx-auto" max-width="344">
    <v-img
      src="https://cdn.vuetifyjs.com/images/cards/sunshine.jpg"
      class="align-end text-white"
      height="200"
      cover
    >
      <v-card-title> {{ challenge.title }} </v-card-title>
    </v-img>

    <v-card-actions>
      <v-btn color="orange-lighten-2" variant="text" @click="show = !show"> 상세보기 </v-btn>

      <v-spacer></v-spacer>

      <v-btn icon="mdi-delete" @click="deleteChallenge()"></v-btn>
    </v-card-actions>

    <v-expand-transition>
      <div v-show="show">
        <v-divider></v-divider>

        <v-card-text> 챌린지 내용: {{ challenge.content }} </v-card-text>
        <v-divider></v-divider>

        <v-card-text> 챌린지 종료 날짜: {{ challenge.period }} </v-card-text>
        <v-card-text> 목표 금액: {{ challenge.goalAmount }} </v-card-text>
        <v-card-text> 챌린지 시작 날짜: {{ challenge.startDate }} </v-card-text>
      </div>
    </v-expand-transition>
  </v-card>
</template>
<script setup>
import { ref } from 'vue'
import functions from '@/api/challenge.js'

const props = defineProps({
  challenge: Object
})

// 챌린지ID 저장
const challengeId = ref('')

// 챌린지 상세보기 위한 변수
const show = ref(false)

// 챌린지 삭제 API 호출
const deleteChallenge = () => {
  challengeId.value = props.challenge.id
  const res = functions.deleteChallenge(challengeId.value)
}
</script>
<style></style>
