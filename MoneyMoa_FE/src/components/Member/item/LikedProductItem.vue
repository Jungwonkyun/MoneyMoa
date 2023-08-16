<template>
  <v-container>
    <v-card variant="flat" class="product-card product-preview">
      <v-row class="justify-space-around align-center">
        <v-col>
          <router-link :to="getDetailRoute(props.type)">
            <v-card-item class="justify-start">
              <v-row no-gutters>
                <v-col cols="auto">
                  <v-img
                    v-if="props.type != 'cma' && icons[liked.bankName]"
                    :src="icons[liked.bankName].default"
                    class="fin-icon"
                  ></v-img>
                  <v-img
                    v-if="props.type === 'cma' && icons[liked.stockName]"
                    :src="icons[liked.stockName].default"
                    class="fin-icon"
                  ></v-img>
                </v-col>
                <v-col cols="auto">
                  <v-card-subtitle>
                    {{ props.type === 'cma' ? liked.stockName : liked.bankName }}
                  </v-card-subtitle>
                </v-col>
              </v-row>
              <v-card-title>
                {{ props.type === 'cma' ? liked.cmaName : liked.productName }}
              </v-card-title>
            </v-card-item>
          </router-link>
        </v-col>
        <v-col cols="auto">
          <v-row v-if="liked.rsrvType == 'deposit'">
            {{ liked.amount }}원을 {{ liked.period }}개월 동안 예치하면
          </v-row>
          <v-row v-else>{{ liked.period }}개월 동안 {{ liked.amount }}원씩 납입하면</v-row>
          <v-row>
            <span class="highlighted-value">{{ liked.result }}</span> 원 수령
          </v-row>
        </v-col>
        <v-col cols="auto"
          ><v-btn icon="mdi-trash-can-outline" variant="plain" @click="del(liked.id)"
        /></v-col>
      </v-row>
    </v-card>
  </v-container>
</template>
<script setup>
import { reactive } from 'vue'
import { deleteLike } from '@/api/product'
import { loadBankIcons, loadSecuIcons } from '@/api/icons'
const emit = defineEmits(['like-updated'])
const props = defineProps({
  liked: Object,
  type: String
})
const icons = reactive({})
Promise.all([loadBankIcons(), loadSecuIcons()]).then(([bankIcons, secuIcons]) => {
  Object.assign(icons, bankIcons, secuIcons)
})

function del(id) {
  deleteLike(props.type, id).then((response) => {
    emit('like-updated')
  })
}

const getDetailRoute = (type) => {
  if (type === 'deposit') {
    return {
      name: 'depositDetail',
      params: { productCode: props.liked.productCode }
    }
  } else if (type === 'saving') {
    return {
      name: 'savingDetail',
      params: { productCode: props.liked.productCode }
    }
  } else {
    return {
      name: 'cmaDetail',
      params: { id: props.liked.cmaId }
    }
  }
}
</script>
<style scoped lang="scss"></style>
