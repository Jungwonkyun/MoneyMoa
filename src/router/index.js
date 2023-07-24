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
      component: ProductsView
    },
    {
      path: '/challenge',
      name: 'challenge',
      component: ChallengeView
    },
    {
      path: '/member',
      name: 'member',
      component: MemberView,
      redirect: '/member/challengelist',
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
    }
  ]
})

export default router
