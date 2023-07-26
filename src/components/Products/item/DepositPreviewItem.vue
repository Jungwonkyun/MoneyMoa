<template>
  <div @click="setProduct(product)">
    <router-link
      :to="{
        name: 'depositDetail',
        params: { productCode: product.product_code }
      }"
    >
      <v-card variant="tonal">
        <v-row class="d-flex justify-space-between">
          <v-col>
            <v-card-item>
              <v-card-subtitle>
                <v-icon icon="mdi-face" />
                {{ product.bank_name }}
              </v-card-subtitle>
              <v-card-title>
                {{ product.product_name }}
              </v-card-title>
            </v-card-item>
          </v-col>
          <v-col cols="2"> {{ product.interest }}% </v-col>
        </v-row>
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
