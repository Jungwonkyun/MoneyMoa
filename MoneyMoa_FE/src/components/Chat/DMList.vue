<template>
  <v-container class="dm-container">
    <v-row class="fill-height">
      <v-col cols="5">
        <v-row class="ma-1">
          <h3>개인 메시지</h3>
        </v-row>
        <v-row>
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
        </v-row>
      </v-col>
      <v-col cols="7">
        <v-card
          v-if="!roomEmpty"
          variant="flat"
          class="empty-card d-flex justify-center align-center"
        >
          <v-row>
            <v-col>
              <v-img :src="writing_moa" :height="200"></v-img>
              <v-card-title>상대방 닉네임을 클릭하면 대화가 시작됩니다.</v-card-title>
            </v-col>
          </v-row>
        </v-card>
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
import writing_moa from '@/assets/img/writing_moa.png'

const { cookies } = useCookies()
const router = useRouter()
const dmRooms = ref([])
const selectedRoom = ref({})
const roomEmpty = ref(false)
const myNickName = cookies.get('member').nickname

getDMRooms().then((response) => {
  dmRooms.value = response.data.dmList.reverse()
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
  background-color: $secondary-color;
  background-color: #cedfff;
}
.dm-card {
  width: 100%;
}
</style>
