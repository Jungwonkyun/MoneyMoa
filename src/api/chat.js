import { apiInstance } from './index.js'
import { useCookies } from 'vue3-cookies'
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'
const api = apiInstance('chat')
const { cookies } = useCookies()

//index.js로 옮기고 서버주소도 env.local에서 가져와야 할 것 같은데 일단 놔둠
// var sock = new SockJS('https://i9d210.p.ssafy.io/api/ws-stomp')
// var ws = Stomp.over(sock)
var reconnect = 0

async function getRooms() {
  try {
    const response = await api.get('roomlist')
    return response
  } catch (error) {
    console.log(error)
  }
}

async function getRoomDetail(roomId) {
  try {
    const response = await api.get(`room/${roomId}`)
    return response
  } catch (error) {
    console.log(error)
  }
}

async function enterRoom(roomId) {
  try {
    const token = cookies.get('accessToken')
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const response = await api.get(`room/enter/${roomId}`, { headers })
    return response
  } catch (error) {
    console.log(error)
  }
}

// function sendMessage(room, sender, message) {
//   ws.send(
//     '/pub/api/chat/message',
//     {},
//     JSON.stringify({
//       type: 'TALK',
//       roomId: room.roomId,
//       sender: sender,
//       message: message
//     })
//   )
//   //this.message = '' 이건 컴포넌트에서 비워주기
// }
// function recvMessage(recv) {
//   //이건 직접적으로 data영역의 messages배열을 업데이트시키는 코드임
//   //컴포넌트에서 호출하려면 리턴값 이용하면 되긴 함
//   //그런데 밑에 connect에서 subscribe직후 받아오는 recv는 컴포넌트에서 받아올 수가 있나??
//   this.messages.unshift({
//     type: recv.type,
//     sender: recv.type == 'ENTER' ? '[알림]' : recv.sender,
//     message: recv.message
//   })
// }
// function connect(room, sender) {
//   /*
//   room = {
//     roomId,
//     name,
//     userCnt,
//     chatMsg[]
//   }
//   */
//   ws.connect(
//     {},
//     function (frame) {
//       ws.subscribe(`/sub/api/chat/room/${room.roomId}`, function (message) {
//         var recv = JSON.parse(message.body)
//         recvMessage(recv)
//       })
//       ws.send(
//         '/pub/api/chat/message',
//         {},
//         JSON.stringify({ type: 'ENTER', roomId: room.roomId, sender: sender })
//       )
//     },
//     function (error) {
//       if (reconnect++ <= 5) {
//         setTimeout(function () {
//           console.log(`재연결 시도 (${reconnect})`)
//           sock = new SockJS('/api/ws-stomp')
//           ws = Stomp.over(sock)
//           connect()
//         }, 10 * 1000)
//       }
//     }
//   )
// }

// export { getRooms, getRoomDetail, sendMessage, recvMessage, connect }
export { getRooms, getRoomDetail, enterRoom }
