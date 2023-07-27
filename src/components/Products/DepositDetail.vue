<template>
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
      <v-col cols="2">금리 {{ product.interest }}% </v-col>
    </v-row>
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
