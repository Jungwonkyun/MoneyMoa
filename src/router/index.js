import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ProductsView from '../views/ProductsView.vue'
import ChallengeView from '../views/ChallengeView.vue'
import DictionaryView from '../views/DictionaryView.vue'
import AccountView from '../views/AccountView.vue'
import LogIn from '../components/Accounts/LogIn.vue'
import SignUp from '../components/Accounts/SignUp.vue'
import FindPassword from '../components/Accounts/FindPassword.vue'

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
      component: ProductsView
    },
    {
      path: '/challenge',
      name: 'challenge',
      component: ChallengeView
    },
    {
      path: '/dictionary',
      name: 'dictionary',
      component: DictionaryView
    },
    {
      path: '/account',
      name: 'account',
      redirect: '/account/logIn',
      component: AccountView,
      children: [
        {
          path: 'login',
          name: 'login',
          component: LogIn
        },
        {
          path: 'signup',
          name: 'signup',
          component: SignUp
        },
        {
          path: 'findpassword',
          name: 'findpassword',
          component: FindPassword
        }
      ]
    }
  ]
})

export default router
