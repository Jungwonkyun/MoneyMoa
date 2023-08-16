<template>
  <v-container>
    <v-tabs v-model="type" color="blue-accent-4" align-tabs="center" :disabled="false">
      <v-tab value="deposit">예금</v-tab>
      <v-tab value="saving">적금</v-tab>
      <v-tab value="cma">CMA</v-tab>
    </v-tabs>
    <LikedProductItem
      v-for="(item, index) in likedList[type]"
      :key="index"
      :liked="item"
      :type="type"
      @like-updated="getContent()"
    />
  </v-container>
</template>
<script setup>
import { ref, reactive } from 'vue'
import { getLiked } from '@/api/product'
import LikedProductItem from './item/LikedProductItem.vue'

const type = ref('deposit')
const likedList = reactive({
  deposit: [],
  saving: [],
  cma: []
})

function getContent() {
  getLiked('deposit').then((response) => {
    // console.log(response.data)
    likedList.deposit = response.data.LikedDeposit
  })
  getLiked('saving').then((response) => {
    likedList.saving = response.data.LikedSaving
  })
  getLiked('cma').then((response) => {
    // console.log(response.data)
    likedList.cma = response.data.LikedCma
  })
}

getContent()
console.log(likedList)

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
</script>
<style scoped lang="scss"></style>
