<template>
  <v-container>
    <v-row class="my-2 align-center">
      <h3>채팅방 목록</h3>
      <v-btn @click="allRoom" rounded class="moa-btn mx-2" variant="text">전체보기</v-btn>
      <v-spacer></v-spacer>
      <v-btn v-if="cookies.get('accessToken')" rounded class="mx-2" variant="text">
        <v-icon icon="mdi-chat-plus"></v-icon>
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
    <v-row>
      <v-text-field
        clearable
        class="chatroom-search"
        placeholder="방 제목으로 검색"
        variant="underlined"
        append-inner-icon="mdi-magnify"
        @keyup.enter="onSearch"
        v-model="keyword"
      >
        <template #append-inner-icon>
          <v-icon @click="onSearch" icon="mdi-magnify" />
        </template>
      </v-text-field>
    </v-row>
    <v-row no-gutters class="d-flex flex-row">
      <v-col v-for="(room, index) in roomList" :key="index" cols="lg-3 md-4">
        <v-card class="chatroom-card ma-4" @click="enter(room.roomId)">
          <v-img v-if="!room.imgUrl" :height="200" aspect-ratio="4/3" :src="pencil_moa"></v-img>
          <v-img v-else :src="room.imgUrl" :height="200" aspect-ratio="4/3"></v-img>
          <v-card-title>{{ room.name }}</v-card-title>
          <v-card-subtitle class="mb-2">{{ room.description }}</v-card-subtitle>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>
<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { getRooms, enterRoom, createRoom, searchRoom } from '@/api/chat'
import { useCookies } from 'vue3-cookies'
import pencil_moa from '@/assets/img/pencil_moa.png'

const { cookies } = useCookies()
const router = useRouter()
const dialog = ref(false)
const roomName = ref('')
const roomDesc = ref('')
const keyword = ref('')

const UploadImg = ref(null)
const previewURL = ref(null)

const roomList = ref([])

function allRoom() {
  getRooms().then((response) => {
    console.log(response.data)
    roomList.value = response.data.roomList.reverse()
    keyword.value = ''
  })
}
allRoom()

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
        roomList.value = response.data.roomList.reverse()
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

function defaultImage(e) {
  e.target.src = landing
}

function onSearch() {
  if (!keyword.value) return
  console.log('검색어는 ')
  console.log(keyword.value)
  searchRoom(keyword.value).then((response) => {
    console.log(response.data)
    roomList.value = response.data.chatroomInfo
  })
}
</script>
<style scoped lang="scss">
.chatroom-card {
  background-color: $secondary-color;
  // width: 50%;
}
</style>
