<template>
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
      <v-col cols="2" align-self="center">최고 {{ getIntrRange(product).max }}% </v-col>
    </v-row>
    <v-card-item>
      <v-table>
        <tbody>
          <tr>
            <td>가입대상</td>
            <td>{{ product.join_member }}</td>
          </tr>
          <tr>
            <td>최고한도</td>
            <td>{{ product.max_limit }}</td>
          </tr>
          <tr>
            <td>유의사항</td>
            <td>{{ product.etc_note }}</td>
          </tr>
        </tbody>
      </v-table>
    </v-card-item>
  </v-card>
  <IntrCalcItem />
  <v-table>
    <thead>
      <tr>
        <th>가입기간</th>
        <th>금리</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(item, index) in product.interestDetails" :key="index">
        <td>{{ item.period }}개월</td>
        <td>{{ item.basicRate }}% ~ {{ item.maxRate }}%</td>
      </tr>
    </tbody>
  </v-table>
</template>
<script setup>
import { ref } from 'vue'
import { useProductStore } from '@/stores/productStore'
import { storeToRefs } from 'pinia'
import {
  getDeposit,
  getSaving,
  getIntrRange,
  getPeriodRange,
  spclConditionIntrList
} from '@/api/product'
import IntrCalcItem from './item/IntrCalcItem.vue'
const store = useProductStore()
const { selectedProduct, period } = storeToRefs(store)
const product = ref({})
if (Object.keys(selectedProduct).length === 0) {
  product.value = getDeposit().data
} else {
  product.value = selectedProduct.value
}
// console.log(product.value.interestDetails)
// const calcDetail = product.interestDetails.reduce((prev, curr) => {
//   if (curr.period <= period.value && (!prev || curr.period > prev.period)) {
//     return curr
//   } else {
//     return prev
//   }
// }, null)
</script>
<style></style>
