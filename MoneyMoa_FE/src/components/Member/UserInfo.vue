<template>
  <v-container class="px-10">
    <v-card :elevation="24" justify="center" class="rounded-xl">
      <v-row>
        <v-col cols="12">
          <v-img
            width="250px"
            height="250px"
            class="profileImage rounded-circle mx-auto"
            :src="img"
            cover
          ></v-img>
        </v-col>
      </v-row>
      <v-card-title class="text-center ma-6"> 닉네임: {{ nickname }} </v-card-title>
      <v-card-subtitle class="text-center ma-6"> 이름: {{ name }} </v-card-subtitle>
      <v-card-subtitle class="text-center ma-6"> 이메일: {{ email }} </v-card-subtitle>

      <v-card-text class="text-center mb-6">
        {{ aboutMe }}
      </v-card-text>
      <v-row class="d-flex justify-space-evenly mt-6 mb-6">
        <v-btn cols="6" v-if="isMe !== true" @click="addFollow, followingDialog">팔로잉</v-btn>
        <v-btn cols="6" @click="doDM(memberId)">DM보내기</v-btn>
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
import memberApi from '@/api/member.js'
import { createDMRoom } from '@/api/chat'
import { useRoute, useRouter } from 'vue-router'
import img from '@/assets/img/beauty.png'
import { useCookies } from 'vue3-cookies'

const { cookies } = useCookies()
const route = useRoute()
const router = useRouter()
const memberId = computed(() => route.params.id)

// 화면에 표시할 유저 데이터
const nickname = ref('')
const introduce = ref('')
const imageUrl = ref('')
const name = ref('')
const email = ref('')

const isMe = ref(false)

// const props = defineProps(['memberId'])
// 굳이 멤버 뷰에서 상속 받을 필요 없음 -> 라우터에서 id 받아서 사용

// 마운트 시에 유저 정보 API 호출, memberId가 변경되면 다시 호출
// memberId가 변경되는 경우는 MemberView.vue에서 UserInfo를 호출할 때
// 받은 데이터 파싱해서 템프릿에 바인딩해야함, 아직 api 구현 안됨

// api 함수 지워서 임의로 1 박아놨어요 -종률-
onMounted(async () => {
  memberApi.getSombodyInfoApi(memberId.value).then((response) => {
    console.log(response)
    nickname.value = response.data.sombody.nickname
    introduce.value = response.data.sombody.introduce
    imageUrl.value = response.data.sombody.imageUrl
    name.value = response.data.sombody.name
    email.value = response.data.sombody.email
    if (response.data.sombody.id === parseInt(memberId.value)) {
      isMe.value = true
    }
  })
})

const addFollow = () => {
  functions.addFollow('팔로우 할 사람의 id')
}

// 이미지
const image = ref('@/assets/img/얼빡이.jpg')

//DM버튼 누를때 로직 - 신경희
function doDM(id) {
  id = Number(id)
  console.log(cookies.get('member').id + '와 ' + id + '의 DM방')
  if (id === cookies.get('member').id) return
  createDMRoom(id).then((response) => {
    const resRoom = response.data.subList[0]
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
