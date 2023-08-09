<template>
  <v-container>
    <v-tabs v-model="productType" color="blue-accent-4" align-tabs="center" :disabled="false">
      <router-link :to="{ name: 'depositList' }"><v-tab value="deposit">예금</v-tab></router-link>
      <router-link :to="{ name: 'savingList' }"><v-tab value="saving">적금</v-tab></router-link>
      <router-link :to="{ name: 'cmaList' }"><v-tab value="cma">CMA</v-tab></router-link>
    </v-tabs>
    <router-view />
    <DictionarySide />
  </v-container>
</template>
<script setup>
// import { watch } from 'vue'
import { useProductStore } from '../stores/productStore'
import { useRouter } from 'vue-router'
import { storeToRefs } from 'pinia'
import DictionarySide from '@/components/Dictionary/DictionarySide.vue'
const store = useProductStore()
// const router = useRouter()
const { productType } = storeToRefs(store)

// watch(
//   () => router.currentRoute.value,
//   (to) => {
//     const productTypeValue = to.path.split('/')[2]
//     if (['deposit', 'saving', 'cma'].includes(productTypeValue)) {
//       productType.value = productTypeValue
//       console.log('예금?적금?cma?')
//       console.log(productType.value)
//     }
//   }
// )
</script>
<style></style>
