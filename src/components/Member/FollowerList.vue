<template lang="">
  <v-container>
    <h1 align="center">팔로워</h1>
    <v-card
      v-for="(follower, index) in followerList[0].to_member"
      :key="index"
      class="follower-card"
      :elevation="10"
    >
      <v-row align="center">
        <v-col cols="4">
          <v-img :width="100" cover :src="follower.profileImage"></v-img>
        </v-col>
        <v-col cols="4">
          <div>{{ follower.nickname }}</div>
        </v-col>
        <v-col cols="4">
          <div>{{ follower.email }}</div>
        </v-col>
      </v-row>
    </v-card>
  </v-container>
</template>
<script>
import { ref } from 'vue'
// 아래 주석 풀고 사용
// import functions from '@/api/member.js'

export default {
  setup() {
    const dummy = [
      {
        message: 'success',
        to_member: [
          {
            member_id: 1,
            nickname: 'test',
            profileImage: 'https://cdn.vuetifyjs.com/images/parallax/material.jpg',
            email: 'test@email.com'
          },
          {
            member_id: 2,
            nickname: 'test',
            profileImage: 'https://cdn.vuetifyjs.com/images/parallax/material.jpg',
            email: 'test@email.com'
          },
          {
            member_id: 3,
            nickname: 'test',
            profileImage: 'https://cdn.vuetifyjs.com/images/parallax/material.jpg',
            email: 'test@email.com'
          }
        ]
      }
    ]
    const followerList = ref(dummy)
    return {
      followerList
    }
  }
}
</script>
<style>
.follower-card {
  margin: 20px;
}
</style>
