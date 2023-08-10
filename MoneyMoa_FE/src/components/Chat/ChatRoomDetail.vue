<template>
  <v-container class="chat-container align-start justify-center">
    <v-dialog v-model="dialog" width="auto">
      <v-card>
        <v-card-title class="text-h6">{{ room.name }} 정보</v-card-title>
        <v-card-text>{{ room.description }}</v-card-text>
        <v-table>
          <thead>
            <tr>
              참여자 목록
            </tr>
          </thead>
          <tbody>
            <tr v-for="(member, index) in roomMembers" :key="index">
              <td>{{ member.memberNickname }}</td>
            </tr>
          </tbody>
        </v-table>
      </v-card>
    </v-dialog>
    <v-card variant="outlined" class="chat-card">
      <v-toolbar>
        <template v-slot:prepend>
          <v-btn icon="mdi-arrow-left" @click="goBack()"></v-btn>
        </template>
        <v-toolbar-title class="text-h6"> {{ room.name }} </v-toolbar-title>
        <template v-slot:append>
          <v-btn id="chatmenu-activator" icon="mdi-dots-vertical" />
          <v-menu activator="#chatmenu-activator">
            <v-list>
              <v-list-item @click="showRoomInfo()">
                <v-list-item-title>채팅방 정보</v-list-item-title>
              </v-list-item>
              <v-list-item @click="quit(room.roomId)">
                <v-list-item-title>채팅방 나가기</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>
        </template>
      </v-toolbar>
      <v-card-text class="chatmessage-area overflow-auto">
        <template v-for="(msg, index) in messages">
          <v-sheet :class="{ 'd-flex flex-row-reverse': isMine(msg.sender) }" class="pa-1">
            <h4 v-if="!isMine(msg.sender)">
              {{ msg.sender }}
            </h4>
            <v-chip :color="isMine(msg.sender) ? 'primary' : ''">
              {{ msg.message }}
            </v-chip>
          </v-sheet>
        </template>
      </v-card-text>
      <v-card-actions class="align-center">
        <v-text-field v-model="inputMsg" @keyup.enter="sendMessage(room, nickName)" hide-details />
        <v-btn @click="sendMessage(room, nickName)" icon="mdi-send" />
      </v-card-actions>
    </v-card>
  </v-container>
</template>
<script setup>
import { ref, watch, nextTick } from 'vue'
import { getRoomDetail, getRoomMembers, quitRoom } from '@/api/chat'
import { useRoute, useRouter } from 'vue-router'
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'
import { useCookies } from 'vue3-cookies'
const { cookies } = useCookies()

var sock = new SockJS('https://i9d210.p.ssafy.io/api/ws-stomp')
var ws = Stomp.over(sock)
var reconnect = 0
console.log(cookies.get('member').nickname + ' 등장')
console.log(cookies.get('accessToken'))

const route = useRoute()
const router = useRouter()
const room = ref({})
const messages = ref([])
const inputMsg = ref('')
const nickName = cookies.get('member').nickname
const dialog = ref(false)
const roomMembers = ref([])

//room이 가진 것? roomId, name(방제), chatMsg배열
getRoomDetail(route.params.roomId).then((response) => {
  //방정보 가져오고
  room.value = response.data.chatroomInfo
  // console.log(room.value)
  //기존메시지 가져오고
  messages.value = response.data.chatMessages.filter((msg) => msg.message !== null)
  //참여자 가져오고
  getRoomMembers(room.value.roomId).then((response) => {
    roomMembers.value = response.data.MemberwhoSubThisChatroom
  })
  //소켓연결합니다
  connect(room.value, nickName)
})

function goBack() {
  if (window.history.length > 1) {
    console.log()
    router.go(-1)
  } else {
    router.push({
      name: 'chatrooms'
    })
  }
}

function showRoomInfo() {
  dialog.value = true
}

function quit(roomId) {
  quitRoom(roomId).then((response) => {
    router.push({
      name: 'chatrooms'
    })
  })
}

function recvMessage(recv) {
  // 배열을 반환합니다
  return [
    {
      type: recv.type,
      sender: recv.type == 'ENTER' ? '[알림]' : recv.sender,
      message: recv.message
    }
  ]
}

function connect(room, sender) {
  // const token = cookies.get('accessToken')
  // const headers = {
  //   login: 'mylogin',
  //   passcode: 'mypasscode',
  //   Authorization: `Bearer ${token}`
  // }
  ws.connect(
    {},
    function (frame) {
      ws.subscribe(`/sub/api/chat/room/${room.roomId}`, function (message) {
        var recv = JSON.parse(message.body)
        // recvMessage 함수를 호출하고 반환된 값을 사용하여 messages 변수를 업데이트
        messages.value.push(...recvMessage(recv))
        // messages 갱신되면 스크롤 최하단으로 이동
        nextTick(() => {
          const chatArea = document.querySelector('.chatmessage-area')
          console.log(chatArea.scrollHeight + ' ~ ' + chatArea.scrollTop)
          chatArea.scrollTop = chatArea.scrollHeight
        })
      })
      ws.send(
        '/pub/api/chat/message',
        JSON.stringify({ type: 'ENTER', roomId: room.roomId, sender: sender })
      )
    },
    function (error) {
      console.log(error)
      if (reconnect++ <= 5) {
        setTimeout(function () {
          console.log(`재연결 시도 (${reconnect})`)
          sock = new SockJS('https://i9d210.p.ssafy.io/api/ws-stomp')
          ws = Stomp.over(sock)
          connect()
        }, 10 * 1000)
      }
    }
  )
}

function sendMessage(room, sender) {
  if (!inputMsg.value.trim()) {
    inputMsg.value = ''
    return
  }
  ws.send(
    '/pub/api/chat/message',
    JSON.stringify({
      type: 'TALK',
      roomId: room.roomId,
      sender: sender,
      message: inputMsg.value
    })
  )
  inputMsg.value = ''
}

function isMine(sender) {
  return sender === nickName
}
</script>
<style scoped>
.chat-container {
  height: 90vh;
}
.chat-card {
  height: 100%;
}
.chatmessage-area {
  height: calc(100% - 144px);
  overflow: auto;
}
</style>
