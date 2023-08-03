<template>
  <v-container>
    <v-row v-if="productType === 'cma'">
      <v-radio-group inline v-model="cmaType">
        <v-radio label="예금 방식으로 계산" value="deposit"></v-radio>
        <v-radio label="적금 방식으로 계산" value="saving"></v-radio>
      </v-radio-group>
    </v-row>
    <v-row>
      <v-col cols="1" v-if="calcType === 'saving'">매달</v-col>
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
      <v-col cols="1" v-if="calcType === 'saving'">원씩</v-col>
      <v-col cols="1" v-else>원을</v-col>
      <v-col><v-select label="기간" :items="periods" v-model="period"></v-select></v-col>
      <v-col v-if="calcType === 'saving'">개월 동안 납입하면</v-col>
      <v-col v-else>개월 동안 예치하면</v-col>
    </v-row>
    <v-row v-if="!isNaN(result) && result > 0">
      <v-col cols="2">{{ result }}</v-col>
      <v-col
        >원을 받을 수 있어요. (이자
        {{ result - amount * (calcType === 'saving' ? period : 1) }}원)</v-col
      >
      <!-- 계산기록찜버튼(todo: 로그인시에만 보여야함, 찜목록으로 바로가기 라우터) -->
      <v-btn @click="like">찜하기</v-btn>
      <v-snackbar v-model="likeSnackbar" timeout="2500" color="white">
        상품을 찜 목록에 담았어요.
        <template v-slot:actions>
          <v-btn color="blue" variant="text" @click="likeSnackbar = false"> 바로가기 </v-btn>
        </template>
      </v-snackbar>
    </v-row>
    <v-row v-else>(금액과 기간을 입력하고 결과를 확인해보세요)</v-row>
  </v-container>
</template>
<script setup>
import { ref, reactive, computed } from 'vue'
import { useProductStore } from '@/stores/productStore'
import { storeToRefs } from 'pinia'

const cmaType = ref('deposit')
const store = useProductStore()
const { productType, amount, period, selectedProduct } = storeToRefs(store)
const periods = reactive([6, 12, 24, 36])

const calcType = computed(() => {
  if (productType.value === 'cma') {
    return cmaType.value
  }
  return productType.value
})

const result = computed(() => {
  let intr = Number(selectedProduct.value.interest) / 100
  let month = Number(period.value)
  if (calcType.value === 'deposit') {
    //일단 단리계산
    console.log('예금식계산')
    return Math.floor(Number(amount.value) * (1 + (intr * month) / 12))
  } else if (calcType.value === 'saving') {
    //일단 단리계산
    console.log('적금식계산')
    return (
      Math.floor(((Number(amount.value) * intr) / 24) * month * (month + 1)) +
      Number(amount.value) * month
    )
  }
})
const onKeyPress = (event) => {
  if (event.key === '+' || event.key === '-') {
    event.preventDefault()
  }
}
const likeSnackbar = ref(false)
function like() {
  //todo: 찜하기axios호출
  console.log('찜할래용')
  likeSnackbar.value = true
}
</script>
<style></style>
