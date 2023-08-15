<template lang="">
  <v-container>
    <h1 align="center">팔로워</h1>
    <v-card
      v-for="(follower, index) in followerList"
      :key="index"
      class="follower-card"
      :elevation="10"
    >
      <router-link :to="`/member/${follower.fromMemberId}`" class="no-link-style text-center">
        <v-row align="center">
          <v-col cols="4">
            <v-img :width="100" cover :src="follower.profileImage"></v-img>
          </v-col>
          <v-col cols="4">
            <div>{{ follower.fromMemberNickname }}</div>
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
import memberApi from '@/api/member.js'

const followerList = ref([])

const route = useRoute()
const memberId = ref(route.params.id)

onMounted(async () => {
  await memberApi.fetchFollowerList(memberId.value).then((res) => {
    console.log(res.data['my follower list'])
    followerList.value = res.data['my follower list']
  })
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
