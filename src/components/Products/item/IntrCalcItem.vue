<template>
  <v-form>
    <v-container>
      <v-row v-if="productType === 'cma'">
        <v-radio-group inline v-model="cmaType">
          <v-radio label="예금 방식으로 계산" value="deposit"></v-radio>
          <v-radio label="적금 방식으로 계산" value="saving"></v-radio>
        </v-radio-group>
      </v-row>
      <v-row>
        <v-col cols="1" v-if="productType === 'saving' || cmaType === 'saving'">매달</v-col>
        <v-col>
          <v-text-field
            v-model="amount"
            hide-details
            single-line
            label="금액"
            type="number"
            min="0"
            @keypress="onKeyPress"
          />
        </v-col>
        <v-col cols="1" v-if="productType === 'saving' || cmaType === 'saving'">원씩</v-col>
        <v-col cols="1" v-else>원을</v-col>
        <v-col><v-select label="기간" :items="periods" v-model="period"></v-select></v-col>
        <v-col v-if="productType === 'saving' || cmaType === 'saving'">개월 동안 납입하면</v-col>
        <v-col v-else>개월 동안 예치하면</v-col>
      </v-row>
      <v-row v-if="!isNaN(result)">
        <v-col>{{ result }}</v-col>
        <v-col>원을 받을 수 있어요. (이자 {{ result - amount }}원)</v-col>
      </v-row>
    </v-container>
    <!-- 계산기록찜버튼 -->
  </v-form>
</template>
<script setup>
import { ref, reactive, computed } from 'vue'
import { useProductStore } from '@/stores/productStore'
import { storeToRefs } from 'pinia'

const cmaType = ref('deposit')
const store = useProductStore()
const { productType, amount, period, selectedProduct } = storeToRefs(store)
const periods = reactive([6, 12, 24, 36])

const result = computed(() => {
  let intr = Number(selectedProduct.value.interest) / 100
  return Math.floor(Number(amount.value) * (1 + (intr * Number(period.value)) / 12))
})
const onKeyPress = (event) => {
  if (event.key === '+' || event.key === '-') {
    event.preventDefault()
  }
}
</script>
<style></style>
