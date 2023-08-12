<template>
  <v-container>
    <SecuritySelectItem />
    <v-radio-group inline v-model="RBJ">
      <h5>유형 선택</h5>
      <v-col class="d-flex justify-space-around">
        <v-radio label="RP형" value="RP형"></v-radio>
        <v-radio label="발행어음형" value="발행어음형"></v-radio>
        <v-radio label="종금형" value="종금형"></v-radio>
      </v-col>
    </v-radio-group>
    <v-divider />
    <v-row>
      결과 {{ filteredProducts.length }} 건
      <CMAPreviewItem
        v-for="(product, index) in filteredProducts"
        :key="index"
        :product="product"
      />
    </v-row>
  </v-container>
</template>
<script setup>
import { ref, reactive, toRefs, computed, watch } from 'vue'
import { useProductStore } from '@/stores/productStore'
import { storeToRefs } from 'pinia'
import { getCMAList } from '@/api/product'
import SecuritySelectItem from './item/SecuritySelectItem.vue'
import CMAPreviewItem from './item/CMAPreviewItem.vue'

const store = useProductStore()
const { productType, securityList, RBJ } = storeToRefs(store)
const state = reactive({
  products: []
})

getCMAList().then((response) => {
  state.products = response.data.products
})

const products = ref([])
getCMAList().then((response) => {
  products.value = response.data.products
})

const filteredProducts = computed(() =>
  state.products.filter(
    (product) =>
      securityList.value.find((secu) => secu.name === product.stockName)?.selected &&
      product[RBJ.value]
  )
)
</script>
<style></style>
