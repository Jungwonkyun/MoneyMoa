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
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import memberApi from '@/api/member.js'

const followerList = ref([])

// 라우터로 타고 들어온 멤버 아이디
const route = useRoute()
const memberId = ref(route.params.id)

// 라우터 ID의 변경을 감지하여 정보를 업데이트하는 로직 추가
watch(memberId, async (newMemberId) => {
  try {
    const res = await memberApi.fetchSpecificFollowerList(newMemberId)
    console.log(res.data.membersFollowlist)
    followingList.value = res.data.membersFollowlist
  } catch (error) {
    console.error('팔로잉 목록을 불러오는 중 에러가 발생했습니다:', error)
  }
})

// 화면에 표시할 유저 데이터
onMounted(async () => {
  try {
    console.log(memberId.value)
    const res = await memberApi.fetchSpecificFollowerList(memberId.value)
    console.log(res.data.membersFollowlist)
    followerList.value = res.data.membersFollowlist
  } catch (error) {
    console.error('팔로잉 목록을 불러오는 중 에러가 발생했습니다:', error)
  }
})
</script>
<style scoped>
.follower-card {
  margin: 20px;
}
.no-link-style {
  text-decoration: none;
  color: inherit;
  cursor: pointer;
}
</style>
