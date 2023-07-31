<template>
  <div>
    <BankSelectItem v-if="productType != 'cma'" />
    <SecuritySelectItem v-else />
    <ProductConditionItem v-if="productType != 'cma'" />
    here is {{ productType }} list
    <hr />
    결과 {{ filteredProducts.length }} 건
    <ProductPreviewItem
      v-for="(product, index) in filteredProducts"
      :key="index"
      :product="product"
      :productType="productType"
    />
  </div>
</template>
<script setup>
import { reactive, computed } from 'vue'
import { useProductStore } from '@/stores/productStore'
import { storeToRefs } from 'pinia'
import BankSelectItem from './item/BankSelectItem.vue'
import SecuritySelectItem from './item/SecuritySelectItem.vue'
import ProductPreviewItem from './item/ProductPreviewItem.vue'
import ProductConditionItem from './item/ProductConditionItem.vue'

const store = useProductStore()
const { productType, bankList } = storeToRefs(store)
const products_dummy = reactive([
  {
    product_code: 'sdsds22',
    bank_code: 'D17',
    bank_name: '신한은행',
    product_name: 'OO예금',
    interest: '3.51',
    sqcl: '첫 가입',
    join_member: '소상공인',
    etc_note: ' ~~~ 가입시 추가 이자',
    max_limit: '10000000',
    img_path: 'ds222dss.jpg'
  },
  {
    product_code: 'sdsds23',
    bank_code: 'D17',
    bank_name: '우리은행',
    product_name: 'XX예금',
    interest: '3.51',
    sqcl: '첫 가입',
    join_member: '소상공인',
    etc_note: ' ~~~ 가입시 추가 이자',
    max_limit: '10000000',
    img_path: 'ds222ds3.jpg'
  }
])
const filteredProducts = computed(() =>
  products_dummy.filter(
    (product) => bankList.value.find((bank) => bank.name === product.bank_name)?.selected
  )
)
// const productComponent = computed(() => {
//   if (productType === 'deposit') {
//     return 'DepositPreviewItem'
//   } else if (productType === 'saving') {
//     return 'SavingPreviewItem'
//   } else if (productType === 'cma') {
//     return 'CmaPreviewItem'
//   }
// })
</script>
<style></style>
