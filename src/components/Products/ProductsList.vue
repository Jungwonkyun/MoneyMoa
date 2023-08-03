<template>
  <div>
    <BankSelectItem v-if="productType != 'cma'" />
    <SecuritySelectItem v-else />
    <ProductConditionItem v-if="productType != 'cma'" />
    <v-divider />
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
import { getDepositList, getSavingList, getPeriodRange, getDeposit } from '@/api/product'
import BankSelectItem from './item/BankSelectItem.vue'
import SecuritySelectItem from './item/SecuritySelectItem.vue'
import ProductPreviewItem from './item/ProductPreviewItem.vue'
import ProductConditionItem from './item/ProductConditionItem.vue'

const store = useProductStore()
const { productType, amount, period, bankList } = storeToRefs(store)
const products_dummy = reactive([
  {
    id: '1',
    product_code: 'sdsds22',
    bank_code: 'D17',
    bank_name: '신한은행',
    product_name: 'OO예금',
    interest: '3.7',
    sqcl: '첫 가입',
    join_member: '소상공인',
    etc_note: ' ~~~ 가입시 추가 이자',
    max_limit: '1000000',
    img_path: 'ds222dss.jpg',
    spclList: ['1. 최초거래 신규고객 우대 0.2%', '2. ㅇㅇ통장 출금하는 경우에 0.1%'],
    interestDetails: [
      {
        period: 6,
        interestType: '단리',
        rsrvType: '',
        basicRate: '3.7',
        maxRate: '3.9'
      },
      {
        period: 12,
        interestType: '단리',
        rsrvType: '',
        basicRate: '3.9',
        maxRate: '4.2'
      }
    ]
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
    img_path: 'ds222ds3.jpg',
    spclList: ['1. 무슨조건 0.2%', '2. 무슨무슨조건 0.1%', '3. 어쩌구조건 0.1%'],
    interestDetails: [
      {
        period: 6,
        interestType: '단리',
        rsrvType: '',
        basicRate: '3.51',
        maxRate: '3.7'
      },
      {
        period: 12,
        interestType: '단리',
        rsrvType: '',
        basicRate: '3.9',
        maxRate: '4.2'
      },
      {
        period: 24,
        interestType: '단리',
        rsrvType: '',
        basicRate: '4.2',
        maxRate: '4.5'
      }
    ]
  }
])
const products = getDepositList()
console.log(products)
const filteredProducts = computed(() =>
  products_dummy.filter(
    (product) =>
      bankList.value.find((bank) => bank.name === product.bank_name)?.selected &&
      checkPeriod(product) &&
      checkAmount(product)
  )
)
function checkPeriod(product) {
  //유저가 입력한 기간이 상품의 가입가능기간을 벗어나면 출력하지 않음
  if (!period.value) return true
  return getPeriodRange(product).min <= period.value && period.value <= getPeriodRange(product).max
}
function checkAmount(product) {
  //유저가 입력한 금액이 상품의 예금한도를 초과하면 출력하지 않음
  if (!amount.value) return true
  return amount.value <= Number(product.max_limit)
}
</script>
<style></style>
