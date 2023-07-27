<template>
  <div>
    <v-tabs v-model="productType" color="blue-accent-4" align-tabs="center" :disabled="false">
      <router-link :to="{ name: 'depositList' }"><v-tab value="deposit">예금</v-tab></router-link>
      <router-link :to="{ name: 'savingList' }"><v-tab value="saving">적금</v-tab></router-link>
      <router-link :to="{ name: 'cmaList' }"><v-tab value="cma">CMA</v-tab></router-link>
    </v-tabs>
    <!-- <ProductsList :productType="productType" /> -->
    <router-view />
  </div>
</template>
<script setup>
import { useProductStore } from '../stores/productStore'
import { storeToRefs } from 'pinia'
const store = useProductStore()
const { productType } = storeToRefs(store)
</script>
<style></style>
