import challenge from './challenge'
import { apiInstance } from './index'
import { useCookies } from 'vue3-cookies'
import { instanceWithAuth } from './interceptorIndex.js'

const api = apiInstance()
const apiWithAuth = instanceWithAuth()

// 토큰 받아오기
const { cookies } = useCookies()
const token = cookies.get('accessToken')

// 피드 전체 목록 조회 API
async function fetchAllFeedList() {
  try {
    // 토큰 받아오기
    const token = cookies.get('accessToken')
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const res = await api.get(`/feed/all`, { headers })
    return res
  } catch (err) {
    console.log(err)
  }
}

// 해당 유저의 피드 목록 조회 API
async function getUserFeedList(memberId) {
  try {
    const res = await apiWithAuth.get(`/feed/member/${memberId}`)
    console.log(res)
    return res
  } catch (err) {
    console.log(err)
  }
}

// 피드 상세 조회 API
async function fetchFeedDetail(feedId) {
  try {
    const res = await apiWithAuth.get(`/feed/detail/${feedId}`)
    return res
  } catch (err) {
    console.log(err)
  }
}

// 피드 좋아요 API
async function addFeedLike(feedId) {
  try {
    const res = await apiWithAuth.post(`/feed/like/${feedId}`)
    console.log(res)
    return res
  } catch (err) {
    console.log(err)
  }
}

// 피드 좋아요 취소 API
async function deleteFeedLike(feedId) {
  try {
    const res = await apiWithAuth.delete(`/feed/unlike/${feedId}`)
    console.log(res)
    return res
  } catch (err) {
    console.log(err)
  }
}

// 피드 검색 API
async function searchFeed(searchWord) {
  try {
    const res = await api.get(`/feed/search?keyword=${searchWord}`)
    return res
  } catch (err) {
    console.log(err)
  }
}

// 피드 생성 API
async function createFeed(feedData, challengeId) {
  try {
    // 토큰 받아오기
    const token = cookies.get('accessToken')
    const headers = {
      Authorization: `Bearer ${token}`,
      'Content-Type': 'multipart/form-data'
    }
    const res = await api.post(`/feed/create/${challengeId}`, feedData, { headers })
    return res
  } catch (err) {
    console.log(err)
  }
}

// 피드 수정 API
async function updateFeed(feedData, feedId) {
  try {
    // 토큰 받아오기
    const token = cookies.get('accessToken')
    const headers = {
      Authorization: `Bearer ${token}`,
      'Content-Type': 'multipart/form-data'
    }
    const res = await api.post(`/feed/update/${feedId}`, feedData, { headers })
    return res
  } catch (err) {
    console.log(err)
  }
}

// 피드 삭제 API
async function deleteFeed(feedId) {
  try {
    const res = await apiWithAuth.delete(`/feed/delete/${feedId}`)
    return res
  } catch (err) {
    console.log(err)
  }
}

// 댓글 작성 API
async function postComment(feedId, comment) {
  try {
    // 토큰 받아오기
    const { cookies } = useCookies()
    const token = cookies.get('accessToken')
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const res = await api.post(`/feed/comment/${feedId}`, JSON.stringify(comment), { headers })
    return res
  } catch (err) {
    console.log(err)
  }
}

// 댓글 삭제 API
async function deleteComment(commentId) {
  try {
    // 토큰 받아오기
    const { cookies } = useCookies()
    const token = cookies.get('accessToken')
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const res = await api.delete(`/feed/comment/delete/${commentId}`, { headers })
    return res
  } catch (err) {
    console.log(err)
  }
}

// 댓글 수정 API
async function updateComment(commentId, comment) {
  try {
    // 토큰 받아오기
    const { cookies } = useCookies()
    const token = cookies.get('accessToken')
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const res = await api.put(`/feed/comment/${commentId}`, JSON.stringify(comment), { headers })
    return res
  } catch (err) {
    console.log(err)
  }
}

export default {
  fetchAllFeedList,
  fetchFeedDetail,
  addFeedLike,
  deleteFeedLike,
  searchFeed,
  createFeed,
  updateFeed,
  deleteFeed,
  postComment,
  deleteComment,
  updateComment,
  getUserFeedList
}
