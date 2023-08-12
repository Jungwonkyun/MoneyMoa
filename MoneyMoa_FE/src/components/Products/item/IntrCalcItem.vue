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
      <!-- 계산기록찜버튼(todo: 찜목록으로 바로가기 링크) -->
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
import { likeProduct } from '@/api/product'
import { storeToRefs } from 'pinia'
import { useCookies } from 'vue3-cookies'
import {
  getIntrRange,
  getPeriodRange,
  getMatchingDetail,
  spclConditionIntrList
} from '@/api/product'
const props = defineProps({
  product: Object,
  spcls: Array,
  retRate: Number
})
const cmaType = ref('deposit')
const store = useProductStore()
const { productType, amount, period, selectedProduct } = storeToRefs(store)
const { cookies } = useCookies()
const periods = reactive([6, 12, 24, 36])

const calcType = computed(() => {
  if (productType.value === 'cma') {
    return cmaType.value
  }
  return productType.value
})
const calcDetail = computed(() => {
  //props.product의 intrDetails중 선택한 기간에 맞는 것 반환
  if (props.product?.interestDetails) {
    // console.log('intrs:')
    // console.log(props.product.interestDetails)
    return props.product.interestDetails.reduce((prev, curr) => {
      if (curr.period <= period.value && (!prev || curr.period > prev.period)) {
        return curr
      } else {
        return prev
      }
    })
  } else {
    console.log('product미전달')
    return null
  }
})

//우대이율 총합(maxRate 고려 전)
const spSum = computed(() => {
  if (productType.value === 'cma') return 0
  let sum = 0
  props.spcls.forEach((item) => {
    if (item.checked) sum = sum + item.intr
  })
  console.log('우대합계: ' + sum)
  return sum
})
//최종 계산에 적용되는 이율(maxRate를 넘지 않게)
const calcIntr = computed(() => {
  if (productType.value === 'cma') {
    return props.retRate
  }
  let finIntr = Math.min(
    Number(calcDetail.value.basicRate) + spSum.value,
    Number(calcDetail.value.maxRate)
  )
  return finIntr
})

const result = computed(() => {
  // console.log(calcIntr.value)
  let intr = calcIntr.value / 100
  console.log('적용 금리: ' + intr)
  let month = Number(period.value)
  //현재 단리계산만 구현
  if (calcType.value === 'deposit') {
    return Math.floor(Number(amount.value) * (1 + (intr * month) / 12))
  } else if (calcType.value === 'saving') {
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
  if (!cookies.get('accessToken')) {
    alert('찜하기는 회원만 이용할 수 있습니다.')
    return
  }

  let likeInfo = {
    memberId: cookies.get('member').id,
    productCode: props.product.productCode || '',
    cmaId: props.product.id || '',
    amount: Number(amount.value),
    interest: calcIntr.value,
    period: period.value,
    result: result.value,
    rsrvType: calcType.value
  }
  console.log(likeInfo)
  likeProduct(productType.value, likeInfo).then((response) => {
    likeSnackbar.value = true
  })
}
</script>
<style scoped>
.v-text-field {
  width: 200px;
}
</style>
