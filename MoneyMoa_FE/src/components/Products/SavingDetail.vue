<template>
  <v-card variant="tonal">
    <v-row>
      <v-col>
        <v-card-item>
          <v-card-subtitle>
            <v-icon icon="mdi-face" />
            {{ product.bankName }}
          </v-card-subtitle>
          <v-card-title>
            {{ product.productName }}
          </v-card-title>
        </v-card-item>
      </v-col>
    </v-row>
    <v-card-item>
      <v-table>
        <tbody>
          <tr>
            <td>가입대상</td>
            <td>{{ product.joinMember }}</td>
          </tr>
          <tr>
            <td>최고한도</td>
            <td v-if="product.maxLimit">{{ product.maxLimit }}원</td>
            <td v-else>없음</td>
          </tr>
          <tr>
            <td>유의사항</td>
            <td>{{ product.etcNote }}</td>
          </tr>
          <tr>
            <td>우대조건</td>
            <v-table>
              <tbody>
                <tr v-for="(item, index) in spclConditionIntrs" :key="index">
                  <td>{{ item.condition }}</td>
                  <td>{{ item.intr }}%</td>
                  <td><v-checkbox v-model="item.checked" /></td>
                </tr>
              </tbody>
            </v-table>
          </tr>
        </tbody>
      </v-table>
    </v-card-item>
  </v-card>
  <IntrCalcItem v-if="loaded" :product="product" :spcls="spclConditionIntrs" />
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
import { useRoute } from 'vue-router'
import { getSaving, getPeriodRange, spclConditionIntrList } from '@/api/product'
import IntrCalcItem from './item/IntrCalcItem.vue'
const store = useProductStore()
const { selectedProduct, period } = storeToRefs(store)
const route = useRoute()
const product = ref({})
const spclConditionIntrs = ref([])
const loaded = ref(false)

getSaving(route.params.productCode).then((response) => {
  // console.log(response.data.product)
  product.value = response.data.product
  store.setProduct(product.value)
  spclConditionIntrs.value = spclConditionIntrList(product.value)
  loaded.value = true
})
</script>
<style></style>
