<template>
  <div>
    here is {{ productType }} list
    <BankSelectItem v-if="productType != 'cma'" />
    <!-- v-else 증권사선택아이템 -->
    <ProductConditionItem v-if="productType != 'cma'" />
    <DepositPreviewItem
      v-for="(product, index) in products_dummy"
      :key="index"
      :product="product"
    />
  </div>
</template>
<script setup>
// import { computed } from 'vue'
import { reactive } from 'vue'
import { useProductStore } from '../../stores/productStore'
import { storeToRefs } from 'pinia'
import BankSelectItem from './item/BankSelectItem.vue'
import ProductConditionItem from './item/ProductConditionItem.vue'
import DepositPreviewItem from './item/DepositPreviewItem.vue'
// const props = defineProps({
//   productType: String
// })
const store = useProductStore()
const { productType } = storeToRefs(store)
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
    bank_name: '신한은행',
    product_name: 'XX예금',
    interest: '3.51',
    sqcl: '첫 가입',
    join_member: '소상공인',
    etc_note: ' ~~~ 가입시 추가 이자',
    max_limit: '10000000',
    img_path: 'ds222ds3.jpg'
  }
])
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
