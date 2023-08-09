<template>
  <v-container class="chat-container align-start justify-center">
    <v-card variant="outlined" class="chat-card">
      <v-card-title>
        <v-banner class="text-h5" sticky>
          <!-- <v-icon icon="mdi-arrow-left"></v-icon> -->
          {{ room.name }}
        </v-banner>
      </v-card-title>
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
import { ref, inject } from 'vue'
import { onMessage, onOpen, onClose, onError } from 'vue3-websocket'
import { getRoomDetail } from '@/api/chat'
import { useRoute } from 'vue-router'
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
const room = ref({})
const messages = ref([])
const inputMsg = ref('')
const nickName = cookies.get('member').nickname
console.log(nickName + ' is my nickname')

//room이 가진 것? roomId, name(방제), chatMsg배열
getRoomDetail(route.params.roomId).then((response) => {
  room.value = response.data['chatroomInfo']
  console.log(room.value)
  messages.value = response.data.chatMessages.filter((msg) => msg.message !== null)
  console.log('got room. try connect')
  connect(room.value, nickName)
  console.log('after connect')
})

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
      console.log('try subscribe')
      ws.subscribe(`/sub/api/chat/room/${room.roomId}`, function (message) {
        console.log('구독 후 받은 것:' + message.body)
        var recv = JSON.parse(message.body)
        // recvMessage 함수를 호출하고 반환된 값을 사용하여 messages 변수를 업데이트
        messages.value.push(...recvMessage(recv))
      })
      ws.send(
        '/pub/api/chat/message',
        JSON.stringify({ type: 'ENTER', roomId: room.roomId, sender: sender })
      )
    },
    function (error) {
      console.log('에러가 발생했어요.')
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
