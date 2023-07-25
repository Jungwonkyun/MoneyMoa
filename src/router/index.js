import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ProductsView from '../views/ProductsView.vue'
import ChallengeView from '../views/ChallengeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
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
    }
  ]
})

export default router
