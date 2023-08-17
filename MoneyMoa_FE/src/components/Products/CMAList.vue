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
    <v-row>
      <v-progress-linear indeterminate v-if="!loaded" />
      <span v-else>결과 {{ filteredProducts.length }} 건</span>
      <CMAPreviewItem v-for="(product, index) in filteredProducts" :key="index" :cma="product" />
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
const loaded = ref(false)

getCMAList().then((response) => {
  state.products = response.data.products
  loaded.value = true
})

const filteredProducts = computed(() =>
  state.products.filter(
    (product) =>
      securityList.value.find((secu) => secu.name === product.stockName)?.selected &&
      product[RBJ.value]
  )
)
</script>
<style scoped lang="scss">
.v-progress-linear {
  color: $logo-color;
}
</style>
