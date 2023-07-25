<template>
  <div @click="setProduct(product)">
    <router-link
      :to="{
        name: 'depositDetail',
        params: { productCode: product.product_code }
      }"
    >
      <v-card :text="product.bank_name" variant="tonal">
        {{ product.product_name }}
      </v-card>
    </router-link>
  </div>
</template>
<script setup>
import { useProductStore } from '@/stores/productStore'
defineProps({
  product: Object
})
const store = useProductStore()
function setProduct(product) {
  store.setProduct(product)
}
</script>
<style></style>
