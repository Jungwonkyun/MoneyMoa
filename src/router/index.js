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

const router = createRouter({
  history: createWebHistory(),
  routes: [
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
          component: () => import('../components/Products/savingDetail.vue')
        },
        {
          path: 'cma',
          name: 'cmaList',
          component: () => import('../components/Products/CMAList.vue')
        },
        {
          path: 'cma/:id',
          name: 'cmaDetail',
          component: () => import('../components/Products/DepositDetail.vue')
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
          beforeEnter: function (to, from, next) {
            const account = useAccountStore()
            const { cookies } = useCookies()
            // 소셜로그인은 비밀번호 검사 X
            if (cookies.get('member') && cookies.get('member').authtokens === 'GENERAL')
              if (account.pwdChecked === false) {
                return next({ name: 'checkpassword' })
              } else {
                return next()
              }
            else {
              return next()
            }
          }
        }
      ]
    },
    {
      path: '/chat',
      name: 'chat',
      component: () => import('../views/ChatView.vue')
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

export default router
