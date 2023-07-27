import { ref } from 'vue'
import axios from 'axios'

const usertoken = ref('유저 토큰')
const userId = ref('상대유저 아이디')

// 팔로잉 API
const followingAPI = ref('팔로잉 API')
const callFollowingApi = async () => {
  try {
    const res = await axios.post(`${followingAPI.value}`, {
      usertoken: usertoken.value,
      userId: userId.value
    })
  } catch (err) {
    console.log(err)
  }
}

// 챌린지 리스트 API
const challengeAPI = ref('챌린지 리스트 API')
const fetchChallengeList = async () => {
  try {
    const res = await axios.get(`${challengeAPI}`, {
      usertoken: usertoken.value
    })
  } catch (err) {
    console.log(err)
  }
}

// 팔로워 유저 목록 API
const followerListAPI = ref('팔로워 리스트 API')
const fetchFollowerList = async () => {
  try {
    const res = await axios.get(`${followerListAPI}`, {
      usertoken: usertoken.value
    })
  } catch (err) {
    console.log(err)
  }
}

// 팔로잉 유저 목록 API
const followingListAPI = ref('팔로워 리스트 API')
const fetchFollowingList = async () => {
  try {
    const res = await axios.get(`${followerListAPI}`, {
      usertoken: usertoken.value
    })
  } catch (err) {
    console.log(err)
  }
}

export default {
  callFollowingApi,
  fetchChallengeList,
  fetchFollowerList,
  fetchFollowingList
}
