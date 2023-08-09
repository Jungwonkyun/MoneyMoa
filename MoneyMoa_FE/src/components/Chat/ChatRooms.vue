<template>
  <v-container>
    <v-row>
      <h3>전체 채팅방 목록</h3>
      <v-spacer></v-spacer>
      <v-btn
        >채팅방 만들기
        <v-dialog v-model="dialog" activator="parent" persistent width="auto">
          <v-card>
            <v-card-title class="text-center mt-4"> 채팅방 생성 </v-card-title>
            <v-card-text>
              <v-container>
                <v-row>
                  <v-col cols="12">
                    <v-textarea
                      clearable
                      label="채팅방 제목"
                      variant="underlined"
                      v-model="roomName"
                      rows="1"
                    ></v-textarea>
                  </v-col>
                  <v-col cols="12">
                    <v-textarea clearable label="채팅방 설명" v-model="roomDesc"></v-textarea>
                  </v-col>
                </v-row>
              </v-container>
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue-darken-1" variant="text" @click="dialog = false"> 닫기 </v-btn>
              <v-btn color="blue-darken-1" variant="text" @click=";(dialog = false), submitRoom()">
                생성하기
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-btn>
    </v-row>
  </v-container>
  <v-card v-for="(room, index) in roomList" :key="index" variant="outlined">
    <v-container>
      <v-row>
        <v-card-title>{{ room.name }}</v-card-title>
        <!-- <v-card-subtitle>{{ room.userCount }}명 참여중</v-card-subtitle> -->
        <v-btn @click="enter(room.roomId)">입장</v-btn>
      </v-row>
    </v-container>
  </v-card>
  <!-- <v-card class="overflow-y-auto" max-height="400">
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
  <v-card variant="tonal">fixed on bottom?</v-card> -->
</template>
<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { getRooms, enterRoom, createRoom } from '@/api/chat'

const router = useRouter()
const dialog = ref(false)
const roomName = ref('')
const roomDesc = ref('')

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

function submitRoom() {
  let roomInfo = {
    name: roomName.value,
    description: roomDesc.value
  }
  createRoom(roomInfo)
    .then((response) => {
      getRooms().then((response) => {
        console.log(response.data)
        roomList.value = response.data.roomList
      })
    })
    .catch((error) => {
      console.log(error)
    })
}
</script>
<style></style>
