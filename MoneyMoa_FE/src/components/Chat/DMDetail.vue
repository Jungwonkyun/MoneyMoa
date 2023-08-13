<template>
  <v-container class="chat-container align-start justify-center">
    <v-card variant="outlined" class="chat-card">
      <v-toolbar>
        <v-toolbar-title class="text-h6"> {{ props.room.receiver }} </v-toolbar-title>
      </v-toolbar>
      <v-card-text class="chatmessage-area overflow-auto">
        <template v-for="(msg, index) in messages">
          <v-sheet
            :class="{ 'd-flex flex-row-reverse': isMine(msg.sender) }"
            class="pa-1 align-end"
          >
            <h4 v-if="!isMine(msg.sender)">
              {{ msg.sender }}
            </h4>
            <v-chip
              class="chatmessage-chip white-space-normal"
              :color="isMine(msg.sender) ? 'primary' : ''"
            >
              {{ msg.message }}
            </v-chip>
            <span class="pa-1">
              {{ formatDate(msg.createdTime) }}
            </span>
          </v-sheet>
        </template>
      </v-card-text>
      <v-card-actions class="align-center">
        <v-text-field
          v-model="inputMsg"
          @keyup.enter="sendMessage(room, nickName)"
          :maxlength="100"
          :counter="100"
          hide-details
        />
        <v-btn @click="sendMessage(room, nickName)" icon="mdi-send" />
      </v-card-actions>
    </v-card>
  </v-container>
</template>
<script setup>
import { ref, watch, nextTick } from 'vue'
import { getRoomDetail, quitRoom } from '@/api/chat'
import { useRouter } from 'vue-router'
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'
import dayjs from 'dayjs'
import { useCookies } from 'vue3-cookies'

const { cookies } = useCookies()
const props = defineProps({
  room: Object
})
var sock = new SockJS('https://i9d210.p.ssafy.io/api/ws-stomp')
var ws = Stomp.over(sock)
var reconnect = 0
console.log(cookies.get('accessToken'))

const router = useRouter()
const room = ref({})
const messages = ref([])
const inputMsg = ref('')
const nickName = cookies.get('member').nickname
const myId = cookies.get('member').id
const dialog = ref(false)

getRoomDetail(props.room.roomId).then((response) => {
  //방정보는 prop으로 받음
  console.log(props.room.sender + '와 ' + props.room.receiver + '가 대화합니다')
  //기존메시지 가져오고
  messages.value = response.data.chatMessages.filter((msg) => msg.message !== null)
  //소켓연결합니다
  connect(props.room, nickName)
})

const formatDate = (dateString) => {
  const date = dayjs(dateString)
  const now = dayjs()
  if (date.isSame(now, 'day')) {
    return date.format('HH:mm')
  } else {
    return date.format('MM/DD HH:mm')
  }
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
      sender: recv.type == 'JOIN' ? '[알림]' : recv.sender,
      message: recv.message
    }
  ]
}

function connect(room, sender) {
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
        JSON.stringify({ type: 'ENTER', roomId: room.roomId, sender: sender, memberId: myId })
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
      message: inputMsg.value,
      memberId: myId
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
.chatmessage-chip {
  max-width: 70%;
  /* height: auto !important; */
}
</style>
