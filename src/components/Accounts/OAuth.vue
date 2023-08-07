<template>
  <div></div>
</template>
<script setup>
import functions from '../../api/member.js'
import { useRouter, useRoute } from 'vue-router'
import { useAccountStore } from '../../stores/accountStore.js'

const account = useAccountStore()
const router = useRouter()
const route = useRoute()
// 카카오 로그인
async function kakaoLogin() {
  try {
    const res = await functions.postKakaoLogin(route.query.code)
    console.log(res.data)
    if (res.data.message === 'success') {
      const token = res.data.authtokens.accessToken

      // 지금은 멤버정보를 서버에서 안보내줘서 일단은 로그인 어디서 했는지만 가져오기
      const member = {
        oauthProvider: 'KAKAO'
      }
      const data = {
        member: member,
        token: token
      }
      account.onLogin(data)
      alert('로그인 되었습니다.')
      router.push({ name: 'home' }).then(() => {
        location.reload()
      })
    } else if (res.data.message === 'fail') {
      alert('로그인에 실패하였습니다. 다시 시도해 주세요')
      router.push({ name: 'loginform' })
    }
  } catch (err) {
    alert('로그인에 실패하였습니다')
    router.push({ name: 'loginform' })
    console.log(err)
  }
}

// 네이버 로그인
async function naverLogin() {
  try {
    const res = await functions.postNaverLogin(route.query.code, route.query.state)
    console.log(res.data)
    if (res.data.message === 'success') {
      const token = res.data.authtokens.accessToken

      // 지금은 멤버정보를 서버에서 안보내줘서 일단은 로그인 어디서 했는지만 가져오기
      const member = {
        oauthProvider: 'NAVER'
      }
      const data = {
        member: member,
        token: token
      }
      account.onLogin(data)
      alert('로그인 되었습니다.')
      router.push({ name: 'home' }).then(() => {
        location.reload()
      })
    } else if (res.data.message === 'fail') {
      alert('로그인에 실패하였습니다. 다시 시도해 주세요')
      router.push({ name: 'loginform' })
    }
  } catch (err) {
    alert('로그인에 실패하였습니다')
    router.push({ name: 'loginform' })
    console.log(err)
  }
}

const path = route.path.split('/').slice(3, 5)
const platform = path[0]
const log = path[1]

if (platform === 'kakao') {
  if (log === 'login') {
    kakaoLogin()
  } else if (log === 'logout') {
    account.onLogout()
    alert('로그아웃 되었습니다.')
    router.push({ name: 'home' }).then(() => {
      location.reload()
    })
  }
} else if (platform === 'naver') {
  if (log === 'login') {
    naverLogin()
  } else if (log === 'logout') {
    account.onLogout()
    alert('로그아웃 되었습니다.')
    router.push({ name: 'home' }).then(() => {
      location.reload()
    })
  }
}
</script>
<style></style>
