<template>
  <div>
    <v-tabs v-model="productType" color="deep-purple-accent-4" align-tabs="center">
      <v-tab value="deposit">예금</v-tab>
      <v-tab value="saving">적금</v-tab>
      <v-tab value="cma">CMA</v-tab>
    </v-tabs>
    <v-window v-model="productType"> {{ productType }}선택됨</v-window>
    <ProductsList :productType="productType" />
  </div>
</template>
<script setup>
import { ref } from 'vue'
import ProductsList from '../components/Products/ProductsList.vue'
const productType = ref('deposit')
</script>
<style></style>
