<template>
  <v-container @click="setProduct(product)">
    <!-- to에 함수바인딩해서 detail페이지 각각 연결되도록 하자...? -->
    <router-link
      :to="{
        name: 'depositDetail',
        params: { productCode: product.product_code }
      }"
    >
      <v-card variant="tonal">
        <v-row>
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
          <v-col cols="2" align-self="center">기본 {{ getIntrRange(product).min }}% </v-col>
          <!-- <v-divider inset vertical /> -->
          <v-col cols="2" align-self="center">최고 {{ getIntrRange(product).max }}% </v-col>
        </v-row>
      </v-card>
    </router-link>
  </v-container>
</template>
<script setup>
import { useProductStore } from '@/stores/productStore'
import { getIntrRange } from '@/api/product'
defineProps({
  product: Object
})
const store = useProductStore()
function setProduct(product) {
  store.setProduct(product)
}
</script>
<style></style>
