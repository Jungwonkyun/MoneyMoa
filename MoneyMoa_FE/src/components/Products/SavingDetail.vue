<template>
  <div>
    <v-card variant="flat" class="product-card">
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
      </v-row>
      <v-card-item>
        <v-table>
          <tbody>
            <tr>
              <td class="product-table-left">가입대상</td>
              <td>{{ product.joinMember }}</td>
            </tr>
            <tr>
              <td>최고한도</td>
              <td v-if="product.maxLimit">{{ product.maxLimit }}원</td>
              <td v-else>없음</td>
            </tr>
            <tr>
              <td>유의사항</td>
              <td>{{ product.etcNote }}</td>
            </tr>
            <tr>
              <td>우대조건</td>
              <td v-if="spclConditionIntrs.length == 1 && spclConditionIntrs.at(0).intr === 0">
                없음
              </td>
              <v-table v-else>
                <tbody>
                  <tr v-for="(item, index) in spclConditionIntrs" :key="index">
                    <td>{{ item.condition }}</td>
                    <td v-if="item.intr !== 0">{{ item.intr }}%</td>
                    <td v-else></td>
                    <td v-if="item.intr !== 0">
                      <v-checkbox v-model="item.checked" hide-details />
                    </td>
                    <td v-else></td>
                  </tr>
                </tbody>
              </v-table>
            </tr>
          </tbody>
        </v-table>
      </v-card-item>
    </v-card>
    <IntrCalcItem v-if="loaded" :product="product" :spcls="spclConditionIntrs" />
    <v-container>
      <v-row class="my-1"
        ><v-icon icon="mdi-calendar-range" />
        <h3>기간별 금리</h3>
      </v-row>
      <v-table>
        <thead>
          <tr>
            <th class="product-table-left">가입기간</th>
            <th>금리</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in product.interestDetails" :key="index">
            <td>{{ item.period }}개월</td>
            <td>{{ item.basicRate }}% ~ {{ item.maxRate }}%</td>
          </tr>
          <tr>
            <td>만기 후 금리</td>
            <td>{{ product.interest }}</td>
          </tr>
        </tbody>
      </v-table>
    </v-container>
    <v-container>
      <ProductCommentItem
        v-if="loaded"
        :commentList="commentList"
        @comment-updated="getContent()"
      />
    </v-container>
  </div>
</template>
<script setup>
import { ref, reactive } from 'vue'
import { useProductStore } from '@/stores/productStore'
import { storeToRefs } from 'pinia'
import { useRoute } from 'vue-router'
import { getSaving, getPeriodRange, spclConditionIntrList } from '@/api/product'
import { loadBankIcons } from '@/api/icons'
import IntrCalcItem from './item/IntrCalcItem.vue'
import ProductCommentItem from './item/ProductCommentItem.vue'
const store = useProductStore()
const { period, bankList } = storeToRefs(store)
const route = useRoute()
const product = ref({})
const spclConditionIntrs = ref([])
const commentList = ref([])
const loaded = ref(false)

const icons = reactive({})
loadBankIcons().then((bankIcons) => {
  Object.assign(icons, bankIcons)
})

function getContent() {
  getSaving(route.params.productCode).then((response) => {
    // console.log(response.data.product)
    product.value = response.data.product

    //은행명 스토어기준으로 변경
    for (const bank of bankList.value) {
      if (product.value.bankName === bank.alias) {
        product.value.bankName = bank.name
      }
    }
    spclConditionIntrs.value = spclConditionIntrList(product.value)
    commentList.value = response.data.comments.map((comment) => ({
      ...comment,
      modifyState: false
    }))
    loaded.value = true
  })
}
getContent()
</script>
<style scoped lang="scss">
.product-table-left {
  width: 15%;
}
</style>
