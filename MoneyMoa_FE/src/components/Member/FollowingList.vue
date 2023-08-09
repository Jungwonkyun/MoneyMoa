<template lang="">
  <v-container>
    <h1 align="center">팔로잉</h1>
    <v-card
      v-for="(follower, index) in followerList[0].to_member"
      :key="index"
      class="follower-card"
      :elevation="10"
    >
      <router-link :to="`/member/${follower.member_id}`" class="no-link-style text-center">
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
      </router-link>
    </v-card>
  </v-container>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import functions from '@/api/member.js'

const dummy = [
  {
    message: 'success',
    to_member: [
      {
        member_id: 23423,
        nickname: 'test',
        profileImage: 'https://cdn.vuetifyjs.com/images/parallax/material.jpg',
        email: 'test@email.com'
      },
      {
        member_id: 323,
        nickname: 'test',
        profileImage: 'https://cdn.vuetifyjs.com/images/parallax/material.jpg',
        email: 'test@email.com'
      },
      {
        member_id: 234,
        nickname: 'test',
        profileImage: 'https://cdn.vuetifyjs.com/images/parallax/material.jpg',
        email: 'test@email.com'
      }
    ]
  }
]
const followerList = ref(dummy)

const route = useRoute()
const memberId = ref(route.params.id)

onMounted(() => {
  const res = functions.fetchFollowingList(memberId.value)
})
</script>
<style>
.follower-card {
  margin: 20px;
}
.no-link-style {
  text-decoration: none;
  color: inherit;
  cursor: pointer;
}
</style>
