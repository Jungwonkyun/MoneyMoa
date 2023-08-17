<template>
  <v-container class="animate__animated animate__fadeInLeft">
    <v-card max-width="350" :elevation="5" class="rounded-xl">
      <v-row>
        <v-col>
          <v-img
            width="250px"
            height="250px"
            class="profileImage rounded-circle mx-auto"
            :src="imageUrl"
            cover
          ></v-img>
        </v-col>
      </v-row>
      <v-card-title class="text-center ma-6"> 닉네임: {{ nickname }} </v-card-title>
      <v-card-subtitle class="text-center ma-6"> 이름: {{ name }} </v-card-subtitle>
      <v-card-subtitle class="text-center ma-6"> 이메일: {{ email }} </v-card-subtitle>

      <v-card-text class="text-center mb-6">
        {{ introduce }}
      </v-card-text>
      <v-row v-if="isMe !== true" class="d-flex justify-space-evenly mt-6 mb-6">
        <v-btn cols="6" @click="addFollow, followingDialog">팔로잉</v-btn>
        <v-btn cols="6" @click="doDM(memberId)">DM보내기</v-btn>
      </v-row>
      <v-row v-else class="ma-2">
        <v-col class="text-center">
          <v-btn>
            <router-link :to="`/member/${memberId}/myproducts`" class="no-link-style text-center"
              >찜한 상품 목록
            </router-link>
          </v-btn>
        </v-col>
        <v-col class="text-center">
          <v-btn>
            <router-link :to="`/member/${memberId}/myproducts`" class="no-link-style text-center">
              정보 수정
            </router-link>
          </v-btn>
        </v-col>
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
    </v-card>
  </v-container>
</template>

<script setup>
import { ref, computed, watch, onUpdated, onMounted } from 'vue'
import memberApi from '@/api/member.js'
import { createDMRoom } from '@/api/chat'
import { useRoute, useRouter } from 'vue-router'
import img from '../../assets/img/default_image.png'
import { useCookies } from 'vue3-cookies'
import axios from 'axios'
import 'animate.css'
import { VSheet } from 'vuetify/lib/components/index.mjs'
const { cookies } = useCookies()
const route = useRoute()
const router = useRouter()

// 라우터로 타고 들어온 멤버 아이디
const memberId = ref(route.params.id)
console.log(memberId.value)
// 현재 로그인한 멤버 아이디
const memberData = cookies.get('member')
const loginMemberId = memberData.id

// 화면에 표시할 유저 데이터
const nickname = ref('')
const introduce = ref('')
const imageUrl = ref('')
const name = ref('')
const email = ref('')

// 내 유저 페이지인지 판단 하는 변수
const isMe = ref(false)

// 라우터 ID의 변경을 감지하여 정보를 업데이트하는 로직 추가
watch(memberId, async (newMemberId) => {
  const response = await memberApi.getSombodyInfoApi(newMemberId)
  console.log(response)
  const sombody = response.data.sombody
  nickname.value = sombody.nickname
  introduce.value = sombody.introduce
  imageUrl.value = sombody.imageUrl
  name.value = sombody.name
  email.value = sombody.email

  // 이미지 없으면 기본사진으로 대체 -  권종률
  if (!imageUrl.value) {
    imageUrl.value = img
  }
  if (loginMemberId === parseInt(newMemberId)) {
    isMe.value = true
  } else {
    isMe.value = false
  }
})

onMounted(async () => {
  const response = await memberApi.getSombodyInfoApi(memberId.value)
  console.log(response)
  const sombody = response.data.sombody
  if (!sombody) {
    router.push({ name: 'NotFound' })
  }
  nickname.value = sombody.nickname
  introduce.value = sombody.introduce
  imageUrl.value = sombody.imageUrl
  name.value = sombody.name
  email.value = sombody.email

  // 이미지 없으면 기본사진으로 대체 -  권종률
  if (!imageUrl.value) {
    imageUrl.value = img
  }
  // 만약 불러온 유저 정보의 id가 로그인한 맴버 아이디와 일치한다면
  if (loginMemberId === parseInt(memberId)) {
    isMe.value = true
  }
})

const addFollow = () => {
  functions.addFollow('팔로우 할 사람의 id')
}

//DM버튼 누를때 로직 - 신경희
function doDM(id) {
  id = Number(id)
  console.log(cookies.get('member').id + '와 ' + id + '의 DM방')
  if (id === cookies.get('member').id) return
  createDMRoom(id).then((response) => {
    // const resRoom = response.data.subList[0]
    router.push({
      name: 'dmlist'
    })
  })
}
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
.circle {
  border-radius: 50%;
}
</style>
