<template>
  <v-container>
    <v-card :elevation="24" justify="center" class="rounded-xl">
      <v-row>
        <v-col cols="12">
          <v-img
            class="profileImage rounded-xl"
            src="https://cdn.vuetifyjs.com/images/parallax/material.jpg"
            cover
          ></v-img>
        </v-col>
      </v-row>
      <v-card-title class="text-center ma-6">
        {{ memberId }}
      </v-card-title>
      <v-card-text class="text-center mb-6">
        {{ aboutMe }}
      </v-card-text>
      <v-row class="d-flex justify-space-evenly mt-6 mb-6">
        <v-btn cols="6" @click="addFollow, followingDialog">팔로잉</v-btn>
        <v-btn cols="6">DM보내기</v-btn>
      </v-row>

      <v-divider class="border-opacity-20"></v-divider>
      <router-link :to="`/member/${memberId}/challengelist`" class="no-link-style text-center">
        <v-card-title>챌린지</v-card-title>
      </router-link>
      <router-link :to="`/member/${memberId}/myfeed`" class="no-link-style text-center">
        <v-card-title>피드</v-card-title>
      </router-link>
      <router-link :to="`/member/${memberId}/followerlist`" class="no-link-style text-center">
        <v-card-title>팔로워</v-card-title>
      </router-link>
      <router-link :to="`/member/${memberId}/followinglist`" class="no-link-style text-center">
        <v-card-title>팔로잉</v-card-title>
      </router-link>
      <v-divider class="border-opacity-20"></v-divider>
      <v-row class="ma-2">
        <v-col class="text-center">
          <v-btn>
            <router-link :to="`/member/${memberId}/myproducts`" class="no-link-style text-center"
              >찜한 상품 목록
            </router-link>
          </v-btn>
        </v-col>
      </v-row>
      <v-row class="ma-2">
        <v-col class="text-center">
          <v-btn>
            <router-link :to="`/member/${memberId}/myproducts`" class="no-link-style text-center">
              정보 수정
            </router-link>
          </v-btn>
        </v-col>
      </v-row>
    </v-card>
  </v-container>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import functions from '@/api/member.js'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const memberId = computed(() => route.params.id)

// const props = defineProps(['memberId'])
// 굳이 멤버 뷰에서 상속 받을 필요 없음 -> 라우터에서 id 받아서 사용

// 마운트 시에 유저 정보 API 호출, memberId가 변경되면 다시 호출
// memberId가 변경되는 경우는 MemberView.vue에서 UserInfo를 호출할 때
// 받은 데이터 파싱해서 템프릿에 바인딩해야함, 아직 api 구현 안됨
onMounted(() => {
  const res = functions.callUserInfoApi(memberId.value)
})

const addFollow = () => {
  functions.addFollow('팔로우 할 사람의 id')
}

const nickname = ref('닉네임')
const aboutMe = ref('안녕하세요 ㅇㅇㅇㅇㅇㅇㅇㅁ래ㅓㅈㄷ랴ㅐ멎ㄷ랴')
</script>
<style>
.profileImage {
  margin: 20px;
}
.no-link-style {
  text-decoration: none; /* 밑줄 제거 */
  color: inherit; /* 색상 상속 */
  cursor: pointer; /* 커서를 손가락 모양으로 변경 (선택 사항) */
}
.border-radius {
  border-radius: 10px;
}
</style>
