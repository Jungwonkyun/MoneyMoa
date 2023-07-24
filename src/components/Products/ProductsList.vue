<template>
  <div>
    <BankSelectItem v-if="productType != 'cma'" />
    <!-- v-else 증권사선택아이템 -->
    <ProductConditionItem v-if="productType != 'cma'" :productType="productType" />
    <DepositPreviewItem
      v-for="(product, index) in products_dummy"
      :key="index"
      :product="product"
    />
  </div>
</template>
<script setup>
import { reactive } from 'vue'
import BankSelectItem from './item/BankSelectItem.vue'
import DepositPreviewItem from './item/DepositPreviewItem.vue'
import ProductConditionItem from './item/ProductConditionItem.vue'
defineProps({
  productType: String
})
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
    product_code: 'sdsds22',
    bank_code: 'D17',
    bank_name: '신한은행',
    product_name: 'XX예금',
    interest: '3.51',
    sqcl: '첫 가입',
    join_member: '소상공인',
    etc_note: ' ~~~ 가입시 추가 이자',
    max_limit: '10000000',
    img_path: 'ds222dss.jpg'
  }
])
</script>
<style></style>
