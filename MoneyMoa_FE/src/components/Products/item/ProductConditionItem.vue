<template>
  <v-container>
    <v-row>
      <v-col v-if="productType == 'saving'" cols="1">매달</v-col>
      <v-col>
        <v-text-field
          class="amount-input"
          v-model.number="amount"
          hide-details
          single-line
          label="금액"
          min="0"
          @keypress="onKeyPress"
          @input="onInput"
        />
      </v-col>
      <v-col v-if="productType == 'saving'" cols="1">원씩</v-col>
      <v-col v-else cols="1">원을</v-col>
      <v-col cols="2"><v-select label="기간" :items="periods" v-model="period"></v-select></v-col>
      <v-col cols="3" v-if="productType == 'saving'">개월 동안 적금할거예요.</v-col>
      <v-col cols="3" v-else>개월 동안 예금할거예요.</v-col>
    </v-row>
  </v-container>
</template>
<script setup>
import { reactive } from 'vue'
import { useProductStore } from '@/stores/productStore'
import { storeToRefs } from 'pinia'
const store = useProductStore()
const { productType, amount, period } = storeToRefs(store)
const periods = reactive([6, 12, 24, 36])

const onInput = (event) => {
  const value = event.target.value.replace(/[^0-9]/g, '')
  amount.value = isNaN(parseInt(value)) ? '' : parseInt(value)
}
const onKeyPress = (event) => {
  if (event.key < '0' || event.key > '9') {
    event.preventDefault()
  }
}
</script>
<style scoped>
.amount-input {
  width: 100%;
}
</style>
