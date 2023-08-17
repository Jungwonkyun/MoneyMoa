<template>
  <v-container>
    <v-row class="d-flex justify-center">
      <v-col cols="lg-10 xl-8">
        <v-tabs v-model="productType" color="blue-accent-4" align-tabs="center" :disabled="false">
          <router-link :to="{ name: 'depositList' }"
            ><v-tab value="deposit">예금</v-tab></router-link
          >
          <router-link :to="{ name: 'savingList' }"><v-tab value="saving">적금</v-tab></router-link>
          <router-link :to="{ name: 'cmaList' }"><v-tab value="cma">CMA</v-tab></router-link>
        </v-tabs>
        <router-view />
      </v-col>
    </v-row>
    <v-sheet class="fixed">
      <DictionarySide />
    </v-sheet>
  </v-container>
</template>
<script setup>
// import { watch } from 'vue'
import { useProductStore } from '../stores/productStore'

import { storeToRefs } from 'pinia'
import DictionarySide from '@/components/Dictionary/DictionarySide.vue'
const store = useProductStore()
// const router = useRouter()
const { productType } = storeToRefs(store)
</script>
<style scoped lang="scss">
.fixed {
  position: fixed;
  bottom: 5vh;
  right: 5vw;
  border-radius: 100px;
}
</style>
