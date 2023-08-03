import { createRouter, createWebHistory } from 'vue-router'

// HomeView
import HomeView from '../views/HomeView.vue'

// ChatView

// ProductsView
import ProductsView from '../views/ProductsView.vue'

// ChallengeView
import ChallengeView from '../views/ChallengeView.vue'

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
          path: 'cma',
          name: 'cmaList',
          component: () => import('../components/Products/CMAList.vue')
        }
      ]
    },
    {
      path: '/challenge',
      name: 'challenge',
      component: ChallengeView,
      props: true,
      redirect: '/challenge/feedList',
      children: [
        {
          path: 'feedList',
          name: 'challengeFeedList',
          component: () => import('../components/Challenge/ChallengeFeedList.vue'),
          props: true
        },
        {
          path: 'feed/:feedId',
          name: 'challengeFeedDetail',
          component: () => import('../components/Challenge/ChallengeFeedDetail.vue')
        },
        {
          path: 'feed/:feedId/:commentId',
          name: 'challengeFeedComment',
          component: () => import('../components/Challenge/ChallengeFeedComment.vue')
        },
        {
          // feed 생성 폼
          path: 'feed/post',
          name: 'challengeFeedPost',
          component: () => import('../components/Challenge/ChallengeFeedPost.vue')
        },
        {
          // feed 검색 결과 리스트
          path: 'feed/search',
          name: 'challengeFeedSearchList',
          component: () => import('../components/Challenge/ChallengeFeedSearchList.vue')
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
    {
      path: '/dictionary',
      name: 'dictionary',
      component: DictionaryView
    },
    {
      path: '/account',
      name: 'account',
      redirect: '/account/login',
      component: AccountView,
      children: [
        {
          path: 'login',
          name: 'loginform',
          component: LoginForm
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
        {
          path: 'profilechange',
          name: 'profilechange',
          component: ProfileChange,
          // 라우터 가드 함수
          beforeEnter: function (to, from, next) {
            const account = useAccountStore()
            // console.log(to, '라우터 가드 테스트')
            if (account.pwdChecked === false) {
              return next({ name: 'checkpassword' })
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

export default router
