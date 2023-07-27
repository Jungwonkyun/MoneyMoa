import { ref } from 'vue'
import axios from 'axios'
import { apiInstance } from './index'
import { useAccountStore } from '@/stores/accountStore.js'
import { storeToRefs } from 'pinia'

const store = useAccountStore()
const { accessToken } = storeToRefs(store)

const api = apiInstance()

const usertoken = ref('유저 토큰')
const userId = ref('상대유저 아이디')

// 유저 정보 API
async function callUserInfoApi(member_id) {
  try {
    const res = await api.get(`/member/info/${member_id}`, {
      usertoken: accessToken
    })
  } catch (err) {
    console.log(err)
  }
}

// 팔로잉 API
async function callFollowingApi() {
  try {
    const res = await api.post('/member/follow', {
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
    const res = await axios.get(`${followingListAPI}`, {
      usertoken: usertoken.value
    })
  } catch (err) {
    console.log(err)
  }
}

export default {
  callUserInfoApi,
  callFollowingApi,
  fetchChallengeList,
  fetchFollowerList,
  fetchFollowingList
}
