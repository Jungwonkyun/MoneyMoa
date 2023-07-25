import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useProductStore = defineStore('product', () => {
  const productType = ref('deposit')
  const selectedProduct = ref({})
  const amount = ref(0)
  function setProductType(type) {
    productType.value = type
  }
  function setAmount(amo) {
    amount.value = amo
  }
  function setProduct(prod) {
    selectedProduct.value = prod
  }
  return { productType, amount, selectedProduct, setProductType, setAmount, setProduct }
})
