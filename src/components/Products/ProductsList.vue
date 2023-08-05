<template>
  <v-container>
    <BankSelectItem />
    <ProductConditionItem />
    <v-divider />
    <v-row>
      결과 {{ filteredProducts.length }} 건
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
import { getDepositList, getSavingList, getPeriodRange, getDeposit, getTest } from '@/api/product'
import BankSelectItem from './item/BankSelectItem.vue'
import ProductPreviewItem from './item/ProductPreviewItem.vue'
import ProductConditionItem from './item/ProductConditionItem.vue'

const store = useProductStore()
const route = useRoute()
const { productType, amount, period, bankList } = storeToRefs(store)
const state = reactive({
  products: []
})

watch(
  productType,
  (newValue) => {
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
  if (!amount.value) return true
  return amount.value <= Number(product.maxLimit)
}
</script>
<style></style>
