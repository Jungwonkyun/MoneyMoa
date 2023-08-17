<template>
  <v-container class="pa-2">
    <router-link :to="getProductDetailRoute(product)">
      <v-card variant="flat" class="product-preview">
        <v-row>
          <v-col>
            <v-card-item>
              <v-row no-gutters>
                <v-col cols="auto">
                  <v-img
                    v-if="icons[product.bankName]"
                    :src="icons[product.bankName].default"
                    class="fin-icon"
                  ></v-img>
                </v-col>
                <v-col>
                  <v-card-subtitle>
                    {{ product.bankName }}
                  </v-card-subtitle>
                </v-col>
              </v-row>
              <v-card-title>
                {{ product.productName }}
              </v-card-title>
            </v-card-item>
          </v-col>
          <v-col cols="2" align-self="center">기본 {{ getIntrRange(product).min }}% </v-col>
          <!-- <v-divider inset vertical /> -->
          <v-col cols="2" align-self="center"
            >최고 <span class="highlighted-value">{{ getIntrRange(product).max }}%</span>
          </v-col>
        </v-row>
      </v-card>
    </router-link>
    <v-divider :thickness="2" />
  </v-container>
</template>
<script setup>
import { ref, reactive } from 'vue'
import { useProductStore } from '@/stores/productStore'
import { storeToRefs } from 'pinia'
import { getIntrRange } from '@/api/product'
import { loadBankIcons } from '@/api/icons'
const props = defineProps({
  product: Object
})
const store = useProductStore()
const { productType } = storeToRefs(store)

const icons = reactive({})
loadBankIcons().then((bankIcons) => {
  Object.assign(icons, bankIcons)
})

function getProductDetailRoute(product) {
  if (productType.value === 'deposit') {
    return {
      name: 'depositDetail',
      params: { productCode: product.productCode }
    }
  } else if (productType.value === 'saving') {
    return {
      name: 'savingDetail',
      params: { productCode: product.productCode }
    }
  }
  return { name: 'chat' }
}

// const getProductDetailRoute = (product) => {
//   if (productType.value === 'deposit') {
//     return {
//       name: 'depositDetail',
//       params: { productCode: product.productCode }
//     }
//   } else if (productType.value === 'saving') {
//     return {
//       name: 'savingDetail',
//       params: { productCode: product.productCode }
//     }
//   }
// }
</script>
<style scoped lang="scss">
.v-divider {
  color: darkblue;
}
</style>
