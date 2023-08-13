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

async function getDMRooms() {
  try {
    const token = cookies.get('accessToken')
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const response = await api.get('dmlist', { headers })
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

async function getRoomMembers(roomId) {
  try {
    const token = cookies.get('accessToken')
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const response = await api.get(`room/members/${roomId}`, { headers })
    return response
  } catch (error) {
    console.log(error)
  }
}

async function createRoom(roomInfo) {
  try {
    const response = await api.post('room/create', JSON.stringify(roomInfo))
    return response
  } catch (error) {
    console.log(error)
  }
}

async function quitRoom(roomId) {
  try {
    const token = cookies.get('accessToken')
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const response = await api.delete('room/out', roomId, { headers })
    return response
  } catch (error) {
    console.log(error)
  }
}
export { getRooms, getRoomDetail, enterRoom, getRoomMembers, createRoom, quitRoom, getDMRooms }
