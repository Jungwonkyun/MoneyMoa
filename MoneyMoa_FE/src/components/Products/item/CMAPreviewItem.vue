<template>
  <v-container class="pa-2">
    <!-- CMA는 productCode 대신 id 쓸거임 -->
    <router-link
      :to="{
        name: 'cmaDetail',
        params: { id: product.id }
      }"
    >
      <v-card variant="flat" class="product-card product-preview">
        <v-row>
          <v-col>
            <v-card-item>
              <v-card-subtitle>
                <v-icon icon="mdi-face" />
                {{ product.stockName }}
              </v-card-subtitle>
              <v-card-title>
                {{ product.cmaName }}
              </v-card-title>
            </v-card-item>
          </v-col>
          <v-col cols="2" align-self="center"> 수익률 {{ product[RBJ] }}% </v-col>
        </v-row>
      </v-card>
    </router-link>
  </v-container>
</template>
<script setup>
import { useProductStore } from '@/stores/productStore'
import { storeToRefs } from 'pinia'
const store = useProductStore()
const { RBJ } = storeToRefs(store)
defineProps({
  product: Object
})
</script>
<style></style>
