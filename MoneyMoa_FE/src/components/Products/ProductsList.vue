<template>
  <v-container>
    <BankSelectItem />
    <ProductConditionItem />
    <v-row>
      <v-progress-linear indeterminate v-if="!loaded" />
      <span v-else> 결과 {{ filteredProducts.length }} 건 </span>
      <ProductPreviewItem
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
import { useRoute } from 'vue-router'
import { getDepositList, getSavingList, getPeriodRange } from '@/api/product'
import BankSelectItem from './item/BankSelectItem.vue'
import ProductPreviewItem from './item/ProductPreviewItem.vue'
import ProductConditionItem from './item/ProductConditionItem.vue'
import { load } from 'webfontloader'

const store = useProductStore()
const route = useRoute()
const { productType, amount, period, bankList } = storeToRefs(store)
const state = reactive({
  products: []
})
const loaded = ref(false)

watch(
  productType,
  (newValue) => {
    loaded.value = false
    if (newValue === 'saving') {
      getSavingList().then((response) => {
        state.products = response.data.products.map((product) => {
          const bank = bankList.value.find((bank) => bank.alias === product.bankName)
          if (bank) {
            return { ...product, bankName: bank.name }
          } else {
            return product
          }
        })
        loaded.value = true
      })
    } else if (newValue === 'deposit') {
      getDepositList().then((response) => {
        state.products = response.data.products.map((product) => {
          const bank = bankList.value.find((bank) => bank.alias === product.bankName)
          if (bank) {
            return { ...product, bankName: bank.name }
          } else {
            return product
          }
        })
        loaded.value = true
      })
    }
  },
  { immediate: true }
)

const filteredProducts = computed(() =>
  state.products.filter(
    (product) =>
      bankList.value.find((bank) => bank.name === product.bankName)?.selected &&
      checkPeriod(product) &&
      checkAmount(product)
  )
)
const { products } = toRefs(state)
function checkPeriod(product) {
  //유저가 입력한 기간이 상품의 가입가능기간을 벗어나면 출력하지 않음
  if (!period.value) return true
  return getPeriodRange(product).min <= period.value && period.value <= getPeriodRange(product).max
}
function checkAmount(product) {
  //유저가 입력한 금액이 상품의 예금한도를 초과하면 출력하지 않음
  if (!amount.value || !product.maxLimit) return true
  return amount.value <= Number(product.maxLimit)
}
</script>
<style scoped lang="scss">
.v-progress-linear {
  color: $logo-color;
}
</style>
