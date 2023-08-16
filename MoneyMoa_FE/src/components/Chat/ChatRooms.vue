<template>
  <v-container>
    <v-row class="my-2">
      <h3>전체 채팅방 목록</h3>
      <v-spacer></v-spacer>
      <v-btn v-if="cookies.get('accessToken')"
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
            <v-file-input
              multiple
              @change="previewChangeImg"
              label="이미지를 선택해 주세요."
              accept="image/*"
              v-model="UploadImg"
              class="my-10"
            ></v-file-input>
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
    <v-card v-for="(room, index) in roomList" :key="index" class="chatroom-card ma-4">
      <v-container>
        <v-row class="align-end">
          <v-col cols="3">
            <v-img v-if="!room.imgUrl" :height="200" aspect-ratio="4/3" :src="landing"></v-img>
            <v-img v-else :src="room.imgUrl" :height="200" aspect-ratio="4/3"></v-img>
          </v-col>
          <v-col align-self="start">
            <v-card-title>{{ room.name }}</v-card-title>
            <v-card-subtitle>{{ room.description }}</v-card-subtitle>
          </v-col>
          <v-btn @click="enter(room.roomId)">입장</v-btn>
        </v-row>
      </v-container>
    </v-card>
  </v-container>
</template>
<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { getRooms, enterRoom, createRoom } from '@/api/chat'
import { useCookies } from 'vue3-cookies'
import landing from '@/assets/img/micheile-henderson-f030K9IzpcM-unsplash.jpg'

const { cookies } = useCookies()
const router = useRouter()
const dialog = ref(false)
const roomName = ref('')
const roomDesc = ref('')

const UploadImg = ref(null)
const previewURL = ref(null)

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
  const jsonBlob = new Blob([JSON.stringify(roomInfo)], { type: 'application/json' })
  const data = new FormData()
  if (UploadImg.value && UploadImg.value.length > 0) {
    data.append('file', UploadImg.value[0])
  }
  data.append('chatRoom', jsonBlob)
  createRoom(data)
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

function previewChangeImg() {
  const reader = new FileReader()
  reader.onload = (e) => {
    previewURL.value = e.target.result
  }
  reader.readAsDataURL(UploadImg.value[0])
}

// const defaultImage = (e) => {
//   e.target.src = landing
// }
function defaultImage(e) {
  e.target.src = landing
}
</script>
<style scoped lang="scss">
.chatroom-card {
  background-color: $secondary-color;
  // width: 50%;
}
</style>
