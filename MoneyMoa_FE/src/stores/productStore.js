import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useProductStore = defineStore('product', () => {
  const productType = ref('deposit')
  const selectedProduct = ref({})
  const bankList = ref([
    { name: '경남은행', selected: true },
    { name: '광주은행', selected: true },
    { name: '국민은행', selected: true },
    { name: '농협은행', selected: true, alias: '농협은행주식회사' },
    { name: '대구은행', selected: true },
    { name: '부산은행', selected: true },
    { name: '수협은행', selected: true },
    { name: '신한은행', selected: true },
    { name: '우리은행', selected: true },
    { name: '전북은행', selected: true },
    { name: '제주은행', selected: true },
    { name: '중소기업은행', selected: true },
    { name: '카카오뱅크', selected: true, alias: '주식회사 카카오뱅크' },
    { name: '케이뱅크', selected: true, alias: '주식회사 케이뱅크' },
    { name: '토스뱅크', selected: true, alias: '토스뱅크 주식회사' },
    { name: '하나은행', selected: true },
    { name: '한국산업은행', selected: true },
    { name: 'SC제일은행', selected: true, alias: '한국스탠다드차타드은행' }
  ])
  const securityList = ref([
    { name: '다올투자증권', selected: true },
    { name: '대신증권', selected: true },
    { name: '메리츠증권', selected: true },
    { name: '미래에셋증권', selected: true },
    { name: '삼성증권', selected: true },
    { name: '신영증권', selected: true },
    { name: '신한투자증권', selected: true },
    { name: '우리종합금융', selected: true },
    { name: '유안타증권', selected: true },
    { name: '유진투자증권', selected: true },
    { name: '케이프투자증권', selected: true },
    { name: '하나증권', selected: true },
    { name: '한국투자증권', selected: true },
    { name: '한화투자증권', selected: true },
    { name: '현대차증권', selected: true },
    { name: 'DB금융투자', selected: true },
    { name: 'IBK투자증권', selected: true },
    { name: 'KB증권', selected: true },
    { name: 'NH투자증권', selected: true },
    { name: 'SK증권', selected: true }
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
  function selectAllBank(isSecurity) {
    if (isSecurity) {
      securityList.value.forEach((sec) => {
        sec.selected = true
      })
    } else {
      bankList.value.forEach((bank) => {
        bank.selected = true
      })
    }
  }
  function cancelAllBank(isSecurity) {
    if (isSecurity) {
      securityList.value.forEach((sec) => {
        sec.selected = false
      })
    } else {
      bankList.value.forEach((bank) => {
        bank.selected = false
      })
    }
  }
  return {
    productType,
    amount,
    period,
    selectedProduct,
    bankList,
    securityList,
    setProductType,
    setAmount,
    setPeriod,
    setProduct,
    selectAllBank,
    cancelAllBank
  }
})
