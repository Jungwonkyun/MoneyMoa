import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ProductsView from '../views/ProductsView.vue'
import ChallengeView from '../views/ChallengeView.vue'
// MemberView
import MemberView from '../views/MemberView.vue'
// Member폴더의 컴포넌트 전체 불러오기
import ChallengeList from '@/components/Member/ChallengeList.vue'
import FollowerList from '@/components/Member/FollowerList.vue'
import FollowingList from '@/components/Member/FollowingList.vue'
import MyFeed from '@/components/Member/MyFeed.vue'
import MyProducts from '@/components/Member/MyProducts.vue'
import DictionaryView from '../views/DictionaryView.vue'
import AccountView from '../views/AccountView.vue'
import LoginForm from '../components/Accounts/LoginForm.vue'
import SignupForm from '../components/Accounts/SignupForm.vue'
import FindPassword from '../components/Accounts/FindPassword.vue'

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
          component: () => import('../components/Products/ProductsList.vue')
        }
      ]
    },
    {
      path: '/challenge',
      name: 'challenge',
      component: ChallengeView
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
        }
      ]
    },
    {
      path: '/feed',
      name: 'feed',
      component: () => import('../views/FeedView.vue')
    }
  ]
})

export default router
