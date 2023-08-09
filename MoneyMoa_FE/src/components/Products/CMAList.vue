<template>
  <div>
    <SecuritySelectItem />
    <v-radio-group inline v-model="cmaType">
      <h5>유형 선택</h5>
      <v-col class="d-flex justify-space-around">
        <v-radio label="RP형" value="RP형" selected="true"></v-radio>
        <v-radio label="발행어음형" value="발행어음형"></v-radio>
        <v-radio label="종금형" value="종금형"></v-radio>
      </v-col>
    </v-radio-group>
    <v-divider />
    결과 {{ filteredProducts.length }} 건
    <CMAPreviewItem v-for="(product, index) in filteredProducts" :key="index" :product="product" />
  </div>
</template>
<script setup>
import { ref, reactive, computed } from 'vue'
import { useProductStore } from '@/stores/productStore'
import { storeToRefs } from 'pinia'
import { getCMAList } from '@/api/product'
import SecuritySelectItem from './item/SecuritySelectItem.vue'
import CMAPreviewItem from './item/CMAPreviewItem.vue'

const cmaType = ref('RP형')
const store = useProductStore()
const { productType, securityList } = storeToRefs(store)
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
// const products = getCMAList()
// console.log(products)

const products = ref([])
getCMAList().then((response) => {
  products.value = response.data.products
})

const filteredProducts = computed(() =>
  products_dummy.filter(
    (product) => securityList.value.find((bank) => bank.name === product.bank_name)?.selected
  )
)
</script>
<style></style>
