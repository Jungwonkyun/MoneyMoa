import { createRouter, createWebHistory } from 'vue-router'
import { useCookies } from 'vue3-cookies'
// HomeView
import HomeView from '../views/HomeView.vue'

// ChatView

// ProductsView
import ProductsView from '../views/ProductsView.vue'
import { useProductStore } from '../stores/productStore.js'

// ChallengeFeedView
import ChallengeFeedView from '../views/ChallengeFeedView.vue'

// MemberView
import MemberView from '../views/MemberView.vue'
import ChallengeList from '@/components/Member/ChallengeList.vue'
import FollowerList from '@/components/Member/FollowerList.vue'
import FollowingList from '@/components/Member/FollowingList.vue'
import MyFeed from '@/components/Member/MyFeed.vue'
import MyProducts from '@/components/Member/MyProducts.vue'

// DictionaryView
import DictionaryView from '../views/DictionaryView.vue'

// AccountView
import AccountView from '../views/AccountView.vue'
import LoginForm from '../components/Accounts/LoginForm.vue'
import SignupForm from '../components/Accounts/SignupForm.vue'
import FindPassword from '../components/Accounts/FindPassword.vue'
import CheckPassword from '../components/Accounts/CheckPassword.vue'
import ProfileChange from '../components/Accounts/ProfileChange.vue'
import { useAccountStore } from '../stores/accountStore.js'
import OAuth from '../components/Accounts/OAuth.vue'
// AdminView
import AdminView from '../views/AdminView.vue'
import { storeToRefs } from 'pinia'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/test',
      name: 'test',
      component: () => import('../components/Accounts/test.vue')
    },
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/products',
      name: 'products',
      component: ProductsView,
      redirect: '/products/deposit',
      children: [
        {
          path: 'deposit',
          name: 'depositList',
          component: () => import('../components/Products/ProductsList.vue')
        },
        {
          path: 'deposit/:productCode',
          name: 'depositDetail',
          component: () => import('../components/Products/DepositDetail.vue')
        },
        {
          path: 'saving',
          name: 'savingList',
          component: () => import('../components/Products/ProductsList.vue')
        },
        {
          path: 'saving/:productCode',
          name: 'savingDetail',
          component: () => import('../components/Products/SavingDetail.vue')
        },
        {
          path: 'cma',
          name: 'cmaList',
          component: () => import('../components/Products/CMAList.vue')
        },
        {
          path: 'cma/:id',
          name: 'cmaDetail',
          component: () => import('../components/Products/CMADetail.vue')
        }
      ]
    },
    {
      path: '/challenge',
      name: 'challenge',
      component: ChallengeFeedView,
      props: true,
      redirect: '/challenge/feedList',
      children: [
        {
          path: 'feedList',
          name: 'challengeFeedList',
          component: () => import('../components/ChallengeFeed/ChallengeFeedList.vue'),
          props: true
        },
        {
          path: 'feed/:feedId',
          name: 'challengeFeedDetail',
          component: () => import('../components/ChallengeFeed/ChallengeFeedDetail.vue')
        },
        {
          path: 'feed/:feedId/:commentId',
          name: 'challengeFeedComment',
          component: () => import('../components/ChallengeFeed/ChallengeFeedComment.vue')
        },
        {
          // feed 검색 결과 리스트
          path: 'feed/search',
          name: 'challengeFeedSearchList',
          component: () => import('../components/ChallengeFeed/ChallengeFeedSearchList.vue')
        }
      ]
    },
    {
      path: '/member/:id',
      name: 'member',
      component: MemberView,
      // to는 현재 접근한 경로의 라우팅 정보를 가지고 있음
      redirect: (to) => {
        return `/member/${to.params.id}/challengelist`
      },
      props: true,
      children: [
        {
          path: 'followerlist',
          name: 'followerlist',
          component: FollowerList
        },
        {
          path: 'followinglist',
          name: 'followinglist',
          component: FollowingList
        },
        {
          path: 'myfeed',
          name: 'myfeed',
          component: MyFeed
        },
        {
          path: 'myproducts',
          name: 'myproducts',
          component: MyProducts
        },
        {
          path: 'challengelist',
          name: 'challengelist',
          component: ChallengeList
        }
      ]
    },
    // 사전 라우터
    {
      path: '/dictionary',
      name: 'dictionary',
      component: DictionaryView
    },
    // 계정 라우터
    {
      path: '/account',
      name: 'account',
      redirect: '/account/login',
      component: AccountView,
      children: [
        // 일반로그인
        {
          path: 'login',
          name: 'loginform',
          component: LoginForm,

          // 로그인 한사람은 로그인폼 못가게
          beforeEnter: function (to, from, next) {
            const account = useAccountStore()
            // 소셜로그인은 비밀번호 검사 X
            if (account.isLogin) {
              return next({ name: 'home' })
            } else {
              return next()
            }
          }
        },
        // 소셜로그인
        {
          path: 'oauth/kakao/login',
          name: 'oauthkakaologin',
          component: OAuth
        },
        {
          path: 'oauth/kakao/logout',
          name: 'oauthkakaologout',
          component: OAuth
        },
        {
          path: 'oauth/naver/login',
          name: 'oauthnaverlogin',
          component: OAuth
        },
        {
          path: 'signup',
          name: 'signupform',
          component: SignupForm
        },
        {
          path: 'findpassword',
          name: 'findpassword',
          component: FindPassword
        },
        {
          path: 'checkpassword',
          name: 'checkpassword',
          component: CheckPassword
        },
        // 개인정보 수정
        {
          path: 'profilechange',
          name: 'profilechange',
          component: ProfileChange,
          // 라우터 가드 함수
          beforeEnter: (to, from, next) => {
            const account = useAccountStore()
            const { cookies } = useCookies()
            // 소셜로그인은 비밀번호 검사 X
            if (cookies.get('member') && cookies.get('member').oauthProvider === 'GENERAL') {
              if (!account.pwdChecked) {
                return next({ name: 'checkpassword' })
              } else {
                return next()
              }
            } else {
              return next()
            }
          }
        }
      ]
    },
    {
      path: '/chat',
      name: 'chat',
      component: () => import('../views/ChatView.vue'),
      redirect: '/chat/roomlist',
      children: [
        {
          path: 'roomlist',
          name: 'chatrooms',
          component: () => import('../components/Chat/ChatRooms.vue')
        },
        {
          path: 'room/:roomId',
          name: 'chatroomdetail',
          component: () => import('../components/Chat/ChatRoomDetail.vue'),
          beforeEnter: (to, from, next) => {
            const { cookies } = useCookies()
            if (!cookies.get('accessToken')) {
              alert('로그인이 필요합니다.')
              next({ name: 'loginform' })
            } else {
              next()
            }
          }
        },
        {
          path: 'dmlist',
          name: 'dmlist',
          component: () => import('../components/Chat/DMList.vue')
        }
      ]
    },
    {
      path: '/admin',
      name: 'admin',
      component: AdminView,
      redirect: '/admin/adminmain',
      children: [
        {
          path: 'adminmain',
          name: 'adminmain',
          component: () => import('../components/Admin/AdminMain.vue')
        },
        {
          path: 'adminusersload',
          name: 'adminusersload',
          component: () => import('../components/Admin/AdminUsersLoad.vue')
        }
      ]
    }
  ]
})

router.beforeEach((to) => {
  const store = useProductStore()
  const productTypeValue = to.path.split('/')[2]
  if (['deposit', 'saving', 'cma'].includes(productTypeValue)) {
    store.productType = productTypeValue
  }
})

// 로그인 필요한 페이지
router.beforeEach(async (to, from, next) => {
  // 전역에서 이동할때마다 토큰 갱신하기
  // 항상 토큰검사하고 나서 이동하기

  const account = useAccountStore()
  await account.getNewToken()
  const urlPath = to.path.split('/').splice(1)
  const needLogin = ['profilechange', 'checkpassword', 'challenge', 'admin']
  const { isLogin } = storeToRefs(account)
  for (let i in urlPath) {
    if (needLogin.includes(urlPath[i]) && !isLogin.value) {
      return next({ name: 'loginform' })
    }
  }
  next()
})

export default router
