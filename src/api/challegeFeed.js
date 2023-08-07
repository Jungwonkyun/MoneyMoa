import { apiInstance } from './index'
import { useAccountStore } from '@/stores/accountStore.js'
import { useCookies } from 'vue3-cookies'

const api = apiInstance()
const { cookies } = useCookies()
const token = cookies.get('accessToken')

// 피니아 스토어에서 유저 토큰을 가져오기
function getAccessToken() {
  const accountStore = useAccountStore()
  const accessToken = accountStore.accessToken
  return accessToken
}

// 피드 전체 목록 조회 API
async function fetchAllFeedList() {
  try {
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const res = await api.get(`api/feed/all`, { headers })
    return res
  } catch (err) {
    console.log(err)
  }
}

// 피드 상세 조회 API
async function fetchFeedDetail(feedId) {
  try {
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const res = await api.get(`/feed/${feedId}`, { headers })
    return res
  } catch (err) {
    console.log(err)
  }
}

// 피드 좋아요 API
async function addFeedLike(feedId) {
  try {
    const accessToken = getAccessToken()
    const res = await api.put(`/feed/like`, {
      usertoken: accessToken,
      feedlike: {
        feed_id: feedId
      }
    })
    return res
  } catch (err) {
    console.log(err)
  }
}

// 피드 검색 API
async function searchFeed(searchWord) {
  try {
    const accessToken = getAccessToken()
    const res = await api.get(`/feed/search`, {
      usertoken: accessToken,
      searchWord: searchWord
    })
    return res
  } catch (err) {
    console.log(err)
  }
}

// 피드 작성 API
async function postFeed() {
  try {
    const accessToken = getAccessToken()
    const res = await api.post(`/feed/writeboard`, {
      usertoken: accessToken
    })
    return res
  } catch (err) {
    console.log(err)
  }
}

// 피드 수정 API
async function updateFeed() {
  try {
    const accessToken = getAccessToken()
    const res = await api.put(`/feed/modifyboard`, {
      usertoken: accessToken
    })
    return res
  } catch (err) {
    console.log(err)
  }
}

// 피드 삭제 API
async function deleteFeed() {
  try {
    const accessToken = getAccessToken()
    const res = await api.delete(`/feed/deleteboard`, {
      usertoken: accessToken
    })
    return res
  } catch (err) {
    console.log(err)
  }
}

// 댓글 작성 API
async function postComment() {
  try {
    const accessToken = getAccessToken()
    const res = await api.get(`/feed/list`, {
      usertoken: accessToken
    })
    return res
  } catch (err) {
    console.log(err)
  }
}

// 댓글 삭제 API
async function deleteComment() {
  try {
    const accessToken = getAccessToken()
    const res = await api.get(`/feed/list`, {
      usertoken: accessToken
    })
    return res
  } catch (err) {
    console.log(err)
  }
}

// 댓글 수정 API
async function updateComment() {
  try {
    const accessToken = getAccessToken()
    const res = await api.get(`/feed/list`, {
      usertoken: accessToken
    })
    return res
  } catch (err) {
    console.log(err)
  }
}

// 전체 유저 피드 리스트 API
async function fetchFeedList() {
  try {
    const accessToken = getAccessToken()
    const res = await api.get(`/feed/list`, {
      usertoken: accessToken
    })
    return res
  } catch (err) {
    console.log(err)
  }
}

export default {
  fetchAllFeedList,
  fetchFeedDetail,
  addFeedLike,
  searchFeed,
  postFeed,
  updateFeed,
  deleteFeed,
  postComment,
  deleteComment,
  updateComment
}
