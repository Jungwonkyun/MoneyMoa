<template>
  <v-container class="pa-2">
    <!-- CMA는 productCode 대신 id 쓸거임 -->
    <router-link
      :to="{
        name: 'cmaDetail',
        params: { id: product.id }
      }"
    >
      <v-card variant="flat" class="product-card product-preview">
        <v-row>
          <v-col>
            <v-card-item>
              <v-row no-gutters>
                <v-col cols="auto">
                  <v-img
                    v-if="icons[product.stockName]"
                    :src="icons[product.stockName].default"
                    class="fin-icon"
                  ></v-img>
                </v-col>
                <v-col>
                  <v-card-subtitle>
                    {{ product.stockName }}
                  </v-card-subtitle>
                </v-col>
              </v-row>
              <v-card-title>
                {{ product.cmaName }}
              </v-card-title>
            </v-card-item>
          </v-col>
          <v-col cols="2" align-self="center"> 수익률 {{ product[RBJ] }}% </v-col>
        </v-row>
      </v-card>
    </router-link>
  </v-container>
</template>
<script setup>
import { reactive } from 'vue'
import { useProductStore } from '@/stores/productStore'
import { storeToRefs } from 'pinia'
import { loadSecuIcons } from '@/api/icons'
const store = useProductStore()
const { RBJ } = storeToRefs(store)
defineProps({
  product: Object
})

const icons = reactive({})
loadSecuIcons().then((secuIcons) => {
  Object.assign(icons, secuIcons)
  // console.log(icons)
})
</script>
<style></style>
