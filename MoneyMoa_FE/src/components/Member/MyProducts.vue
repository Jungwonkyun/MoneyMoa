<template lang="">
  <v-container>
    <LikedProductItem v-for="(item, index) in tmpLike" :key="index" :liked="item" />
    <v-card
      v-for="(product, index) in myProducts[0].LikeProduct"
      :key="index"
      :elevation="10"
      class="mb-8 pa-4"
    >
      <v-row align="center">
        <v-col cols="3" class="text-center">
          <v-row>
            <v-col>
              <v-img class="mx-auto" :src="kbLogo" style="background-color: black"></v-img>
            </v-col>
          </v-row>
          <v-row>
            <v-col>
              {{ product.bank }}
            </v-col>
          </v-row>
          <v-row>
            <v-col>
              {{ product.product_name }}
            </v-col>
          </v-row>
        </v-col>
        <v-col cols="8">
          <v-row>
            <v-col v-if="product.product_type === '적금'"
              >매달 {{ product.amount }}을 입금하면</v-col
            >
            <v-col v-if="product.product_type === '예금'"
              >한번에 {{ product.amount }}을 입금하면</v-col
            >
            <v-col v-if="product.product_type === 'CMA적금식'">
              매월 {{ product.amount }}을 입금하면</v-col
            >
            <v-col v-if="product.product_type === 'CMA예금식'">
              한번에 {{ product.amount }}을 예치하면</v-col
            >
          </v-row>
          <v-row>
            <v-col> {{ product.period }}개월 후에 {{ product.result }}원 받을 수 있습니다. </v-col>
          </v-row>
        </v-col>
        <v-col cols="1">
          <v-row>
            <v-btn>삭제</v-btn>
          </v-row>
        </v-col>
      </v-row>
    </v-card>
  </v-container>
</template>
<script setup>
import { ref } from 'vue'
import LikedProductItem from './item/LikedProductItem.vue'
const tmpLike = ref([
  {
    productCode: 'WR0001B',
    productName: '우리예금',
    period: 12,
    interest: '3.5',
    amount: '1000000',
    productType: 'deposit',
    result: '1035000',
    bankName: '우리은행'
  },
  {
    // productCode: 'WR0001B',
    id: 15,
    productName: 'myCMA',
    period: 12,
    interest: '3.5',
    amount: '10000',
    productType: 'cma2',
    result: '123000',
    bankName: '한국투자증권'
  }
])
const dummy = [
  {
    message: 'success',
    LikeProduct: [
      {
        id: 1,
        member_id: 2,
        product_code: '1223dde',
        product_name: '꿈노아 적금',
        period: '12',
        interest: '3.5',
        amount: '100000',
        product_type: '적금',
        result: '1035000',
        bank: '우리은행'
      },
      {
        id: 2,
        member_id: 2,
        product_code: '1223dde',
        product_name: '꿈노아 예금',
        period: '24',
        interest: '4',
        amount: '1500000',
        product_type: '예금',
        result: '1560000',
        bank: '우리은행'
      },
      {
        id: 3,
        member_id: 2,
        product_code: '1223dde',
        product_name: "노's CMA",
        period: '6',
        interest: '3',
        amount: '2000000',
        product_type: 'CMA예금식',
        result: '2060000',
        bank: '우리은행'
      },
      {
        id: 3,
        member_id: 2,
        product_code: '1223dde',
        product_name: "노's CMA",
        period: '6',
        interest: '3',
        amount: '100000',
        product_type: 'CMA적금식',
        result: '1035000',
        bank: '우리은행'
      }
    ]
  }
]

const myProducts = ref(dummy)

const wooriLogo = '/src/assets/bankLogo/woori.png'
const kbLogo = '/src/assets/bankLogo/kb.svg'
</script>
<style lang=""></style>
