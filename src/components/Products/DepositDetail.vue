<template>
  <v-card :text="product.bank_name" variant="tonal">
    {{ product.product_name }}의 상세페이지
  </v-card>
  <IntrCalcItem></IntrCalcItem>
</template>
<script setup>
import { useProductStore } from '../../stores/productStore'
import IntrCalcItem from './item/IntrCalcItem.vue'
const store = useProductStore()
// const product = storeToRefs(store)
const product = store.selectedProduct
</script>
<style></style>
