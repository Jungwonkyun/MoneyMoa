<template lang="">
  <v-container>
    <h1 align="center">팔로잉</h1>
    <v-card
      v-for="(following, index) in followingList"
      :key="index"
      class="follower-card"
      :elevation="10"
    >
      <v-row align="center">
        <v-col cols="10">
          <router-link :to="`/member/${following.toMemberId}`" class="no-link-style text-center">
            <div>{{ following.toMemberNickname }}</div>
          </router-link>
        </v-col>
        <v-col cols="2">
          <v-btn
            icon="mdi-delete"
            size="large"
            variant="text"
            @click="deleteFollow(following.toMemberId)"
          ></v-btn>
        </v-col>
      </v-row>
    </v-card>
  </v-container>
</template>
<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { useRoute } from 'vue-router'
import memberApi from '@/api/member.js'

// 라우터로 타고 들어온 멤버 아이디
const route = useRoute()
const memberId = computed(() => route.params.id)

const followingList = ref([])

// 라우터 ID의 변경을 감지하여 정보를 업데이트하는 로직 추가
watch(memberId, async (newMemberId) => {
  try {
    const res = await memberApi.fetchFollowingList(newMemberId)
    console.log(res)
    followingList.value = res.data['my following list']
  } catch (error) {
    console.error('팔로잉 목록을 불러오는 중 에러가 발생했습니다:', error)
  }
})

// 화면에 표시할 유저 데이터
onMounted(async () => {
  try {
    const res = await memberApi.fetchFollowingList()
    console.log(res)
    followingList.value = res.data['my following list']
  } catch (error) {
    console.error('팔로잉 목록을 불러오는 중 에러가 발생했습니다:', error)
  }
})

// 언팔
const deleteFollow = async (toMemberId) => {
  try {
    console.log(toMemberId)
    const res = await memberApi.deleteFollow(toMemberId)
    console.log(res)
  } catch (error) {
    console.error('언팔 처리 중 에러가 발생했습니다:', error)
  }
}
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
