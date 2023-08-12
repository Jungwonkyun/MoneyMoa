import challenge from './challenge'
import { apiInstance } from './index'
import { useCookies } from 'vue3-cookies'

const api = apiInstance()

// 토큰 받아오기
const { cookies } = useCookies()
const token = cookies.get('accessToken')

// 피드 전체 목록 조회 API
async function fetchAllFeedList() {
  try {
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
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const res = await api.get(`/feed/member/${memberId}`, { headers })
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
    const res = await api.get(`/feed/detail/${feedId}`, { headers })
    return res
  } catch (err) {
    console.log(err)
  }
}

// 피드 좋아요 API
async function addFeedLike(feedId) {
  try {
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const res = await api.put(`/feed/like`, { headers })
    return res
  } catch (err) {
    console.log(err)
  }
}

// 피드 검색 API
async function searchFeed(searchWord) {
  try {
    const res = await api.get(`/feed/search`, {
      usertoken: accessToken,
      searchWord: searchWord
    })
    return res
  } catch (err) {
    console.log(err)
  }
}

// 피드 생성 API
async function createFeed(feedData, challengeId) {
  try {
    const formData = new FormData()
    for (const file of feedData.files) {
      formData.append('files', file)
    }
    formData.append('content', feedData.content)
    formData.append('depositAmount', feedData.depositAmount)
    formData.append('hashtag', feedData.hashtag)

    const headers = {
      Authorization: `Bearer ${token}`,
      'Content-Type': 'multipart/form-data'
    }
    const res = await api.post(`/feed/create/${challengeId}`, formData, { headers })
    return res
  } catch (err) {
    console.log(err)
  }
}

// 피드 수정 API
async function updateFeed(feedData, feedId) {
  try {
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const res = await api.put(`/feed/update/${feedId}`, JSON.stringify(feedData), { headers })
    return res
  } catch (err) {
    console.log(err)
  }
}

// 피드 삭제 API
async function deleteFeed(feedId) {
  try {
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const res = await api.delete(`/feed/delete/${feedId}`, { headers })
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
async function updateComment() {
  try {
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
  createFeed,
  updateFeed,
  deleteFeed,
  postComment,
  deleteComment,
  updateComment,
  getUserFeedList
}
