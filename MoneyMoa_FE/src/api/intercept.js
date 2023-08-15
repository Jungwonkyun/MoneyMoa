import { useCookies } from 'vue3-cookies'
import functions from '@/api/member.js'

import { useAccountStore } from '@/stores/accountStore.js'
import { storeToRefs } from 'pinia'
const { cookies } = useCookies()

export function setInterceptors(instance) {
  instance.interceptors.request.use(
    function (config) {
      config.headers.Authorization = `Bearer ${cookies.get('accessToken')}`
      return config
    },
    function (error) {
      console.log(error)
      return Promise.reject(error)
    }
  )

  instance.interceptors.response.use(
    function (response) {
      return response
    },
    async function (error) {
      const { config } = error
      console.log(error)
      if (error.response.status === 403) {
        const account = useAccountStore()
        const { isLogin } = storeToRefs(account)
        try {
          const originalRequest = config

          const res = await functions.postGetAccessid()
          if (res.message === 'success') {
            cookies.set('accessToken', res.RefreshedAccessToken, '30MIN')
            const expireTimes = cookies.get('expireTimes')
            // 리프레시 토큰 수명만큼 새로 저장
            cookies.set('accessTokenRef', res.RefreshedAccessToken, expireTimes)
            // isLogin값도 갱신해주자
            isLogin.value = !!cookies.get('accessToken')
          }
          console.log(res)
          originalRequest.headers.Authorization = `Bearer ${res.RefreshedAccessToken}`
          return instance(originalRequest)
        } catch (err) {
          alert('다시 로그인 해주세요.')
          throw err
        }
      }
      return Promise.reject(error)
    }
  )

  return instance
}
