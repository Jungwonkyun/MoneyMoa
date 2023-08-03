<template>
  <v-container class="text-center">
    <h2>유저 조회</h2>
    <v-row v-for="(member, index) in members" :key="index" class="my-4">
      <v-col cols="3">
        {{ member.id + ' : ' + member.email
        }}<v-icon
          @click="userDelete(member)"
          icon="mdi-delete"
          color="red"
          style="cursor: pointer"
        />
      </v-col>
      <v-col cols="2"> 유저 {{ member.id }} <br />피드 바로가기 </v-col>
      <v-col cols="2"> 유저 {{ member.id }} <br />챌린지 바로가기 </v-col>
      <v-col cols="2"> 유저 {{ member.id }} <br />오픈 채팅방 바로가기 </v-col>
      <v-col cols="2"> 유저 {{ member.id }} <br />찜 상품 바로가기 </v-col>
    </v-row>
  </v-container>
</template>
<script setup>
import functions from '@/api/admin.js'
import { computed, onMounted, ref } from 'vue'
import { useCookies } from 'vue3-cookies'

const { cookies } = useCookies()
// members null값 주고
const members = ref(null)

// admin 토큰값 받기위해 쿠키에서 멤버정보 가져오고
const accessToken = computed(() => {
  return cookies.get('accessToken')
})

// 멤버들 불러오는 함수짜고
async function loadAlluser() {
  try {
    members.value = await functions.getLoadAlluser(accessToken.value)
    if (members.value) {
      members.value = members.value.data.memberList
    }
  } catch (err) {
    console.log(err)
  }
}

// 마운트 되자마자 불러오게 함
// 제발 돼라
onMounted(() => {
  loadAlluser()
})

async function userDelete(e) {
  console.log(e)
  if (confirm('정말로 삭제하시겠습니까?')) {
    try {
      const message = functions.deleteUser(e.id, accessToken.value)
      console.log(message)
      if (message.data.message === 'success') {
        alert('삭제되었습니다.')
      }
    } catch (err) {
      console.log(err)
    }
  }
}
</script>
<style></style>
