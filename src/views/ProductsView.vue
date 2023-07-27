<template>
  <div>
    <v-tabs v-model="productType" color="blue-accent-4" align-tabs="center" :disabled="false">
      <router-link :to="{ name: 'depositList' }"
        ><v-tab value="deposit" @click="setProductType('deposit')">예금</v-tab></router-link
      >
      <router-link :to="{ name: 'savingList' }"
        ><v-tab value="saving" @click="setProductType('saving')">적금</v-tab></router-link
      >
      <router-link :to="{ name: 'cmaList' }"
        ><v-tab value="cma" @click="setProductType('cma')">CMA</v-tab></router-link
      >
    </v-tabs>
    <!-- <ProductsList :productType="productType" /> -->
    <router-view />
  </div>
</template>
<script setup>
import { ref } from 'vue'
import { useProductStore } from '../stores/productStore'
const productType = ref('deposit')
const store = useProductStore()
const setProductType = (type) => {
  store.setProductType(type)
}
</script>
<style></style>
