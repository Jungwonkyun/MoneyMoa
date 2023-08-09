<template>
  <h3>채팅방 목록</h3>
  <v-card v-for="(room, index) in roomList" :key="index" variant="outlined">
    <v-container>
      <v-row>
        <v-card-title>{{ room.name }}</v-card-title>
        <!-- <v-card-subtitle>{{ room.userCount }}명 참여중</v-card-subtitle> -->
        <v-btn @click="enter(room.roomId)">입장</v-btn>
      </v-row>
    </v-container>
  </v-card>
  <v-card class="overflow-y-auto" max-height="400">
    <v-banner class="justify-center text-h5 font-weight-light" sticky> Scroll Me </v-banner>

    <v-card-text>
      <div v-for="n in 12" :key="n" class="mb-4">
        Lorem ipsum dolor sit amet consectetur adipisicing elit. Modi commodi earum tenetur.
        Asperiores dolorem placeat ab nobis iusto culpa, autem molestias molestiae quidem pariatur.
        Debitis beatae expedita nam facere perspiciatis. Lorem ipsum dolor sit amet consectetur
        adipisicing elit. Repellendus ducimus cupiditate rerum officiis consequuntur laborum
        doloremque quaerat ipsa voluptates, nobis nam quis nulla ullam at corporis, similique
        ratione quasi illo!
      </div>
    </v-card-text>
    <v-banner class="justify-center text-h5 font-weight-light" sticky> Scroll You </v-banner>
  </v-card>
  <v-card variant="tonal">fixed on bottom?</v-card>
</template>
<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { getRooms, enterRoom } from '@/api/chat'

const router = useRouter()

const roomList = ref([])
getRooms().then((response) => {
  console.log(response.data)
  roomList.value = response.data.roomList
})
function enter(roomId) {
  enterRoom(roomId)
    .then((response) => {
      router.push({
        name: 'chatroomdetail',
        params: { roomId: roomId }
      })
      console.log('드가자~')
    })
    .catch((error) => {
      console.log(error)
      alert('로그인 후 이용해주세요.')
    })
}
</script>
<style></style>
