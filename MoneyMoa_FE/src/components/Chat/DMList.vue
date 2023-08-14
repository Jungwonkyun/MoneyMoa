<template>
  <v-container class="dm-container">
    <h3>개인 메시지</h3>
    <v-row class="fill-height">
      <v-col cols="6">
        <v-card
          v-for="(item, index) in dmRooms"
          :key="index"
          class="dm-card"
          @click="enterDM(item)"
          variant="flat"
        >
          <v-card-title>{{ roomOppo(item) }}</v-card-title>
          <v-divider />
        </v-card>
      </v-col>
      <v-col cols="6">
        <v-card
          v-if="!roomEmpty"
          variant="tonal"
          class="empty-card d-flex justify-center align-center"
          >click list to start DM</v-card
        >
        <DMDetail v-else :roomId="selectedRoom.roomId" />
      </v-col>
    </v-row>
  </v-container>
</template>
<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { getDMRooms, enterDMRoom } from '@/api/chat'
import { useCookies } from 'vue3-cookies'
import DMDetail from './DMDetail.vue'

const { cookies } = useCookies()
const router = useRouter()
const dmRooms = ref([])
const selectedRoom = ref({})
const roomEmpty = ref(false)
const myNickName = cookies.get('member').nickname

getDMRooms().then((response) => {
  dmRooms.value = response.data.dmList
})

function enterDM(room) {
  enterDMRoom(room.roomId).then((response) => {
    console.log(response.data)
    // selRoom.value = response.data.chatroomInfo
    selectedRoom.value = room
    console.log('누른 방:')
    console.log(selectedRoom.value)
    roomEmpty.value = true
  })
}

function roomOppo(room) {
  //방이름으로 쓸 상대방아이디 (sender, receiver 중 내 닉이 아닌것 반환)
  return room.sender === myNickName ? room.receiver : room.sender
}
</script>
<style scoped lang="scss">
.dm-container {
  height: 88%;
}
.empty-card {
  height: 80vh;
}
</style>
