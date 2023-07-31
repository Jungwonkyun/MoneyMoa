import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useProductStore = defineStore('product', () => {
  const productType = ref('deposit')
  const selectedProduct = ref({})
  const bankList = ref([
    { name: '경남은행', selected: true },
    { name: '광주은행', selected: true },
    { name: '국민은행', selected: true },
    { name: '농협은행', selected: true },
    { name: '대구은행', selected: true },
    { name: '부산은행', selected: true },
    { name: '수협은행', selected: true },
    { name: '신한은행', selected: true },
    { name: '우리은행', selected: true },
    { name: '전북은행', selected: true },
    { name: '제주은행', selected: true },
    { name: '중소기업은행', selected: true },
    { name: '카카오뱅크', selected: true },
    { name: '케이뱅크', selected: true },
    { name: '하나은행', selected: true },
    { name: '한국산업은행', selected: true },
    { name: 'SC제일은행', selected: true }
  ])
  const amount = ref()
  const period = ref()
  function setProductType(type) {
    productType.value = type
  }
  function setAmount(amo) {
    amount.value = amo
  }
  function setPeriod(per) {
    period.value = per
  }
  function setProduct(prod) {
    selectedProduct.value = prod
  }
  function selectAllBank() {
    bankList.value.forEach((bank) => {
      bank.selected = true
    })
  }
  function cancelAllBank() {
    bankList.value.forEach((bank) => {
      bank.selected = false
    })
  }
  return {
    productType,
    amount,
    period,
    selectedProduct,
    bankList,
    setProductType,
    setAmount,
    setPeriod,
    setProduct,
    selectAllBank,
    cancelAllBank
  }
})
