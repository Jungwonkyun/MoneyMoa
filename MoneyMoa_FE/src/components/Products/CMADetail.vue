<template>
  <div>
    <v-card variant="tonal">
      <v-row>
        <v-col>
          <v-card-item>
            <v-card-subtitle>
              <v-icon icon="mdi-face" />
              {{ product.stockName }}
            </v-card-subtitle>
            <v-card-title>
              {{ product.cmaName }}
            </v-card-title>
          </v-card-item>
        </v-col>
      </v-row>
      <v-card-item>
        <v-radio-group v-model="RBJ" column class="info-by-cmatype" hide-details>
          <v-table>
            <tbody>
              <tr v-if="memo['RP형'].length">
                <td class="cmatype-col"><v-radio label="RP형" value="RP형"></v-radio></td>
                <v-table>
                  <tbody>
                    <tr v-for="(item, index) in memo['RP형']" :key="index">
                      <td>{{ item }}</td>
                    </tr>
                  </tbody>
                </v-table>
              </tr>
              <tr v-if="memo['발행어음형'].length">
                <td class="cmatype-col">
                  <v-radio label="발행어음형" value="발행어음형"></v-radio>
                </td>
                <v-table>
                  <tbody>
                    <tr v-for="(item, index) in memo['발행어음형']" :key="index">
                      <td>{{ item }}</td>
                    </tr>
                  </tbody>
                </v-table>
              </tr>
              <tr v-if="memo['종금형'].length">
                <td class="cmatype-col"><v-radio label="종금형" value="종금형"></v-radio></td>
                <v-table>
                  <tbody>
                    <tr v-for="(item, index) in memo['종금형']" :key="index">
                      <td>{{ item }}</td>
                    </tr>
                  </tbody>
                </v-table>
              </tr>
            </tbody>
          </v-table>
        </v-radio-group>
      </v-card-item>
    </v-card>
    <IntrCalcItem v-if="loaded" :product="product" :retRate="retByRBJ" />
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
import { ref, computed } from 'vue'
import { useProductStore } from '@/stores/productStore'
import { storeToRefs } from 'pinia'
import { useRoute } from 'vue-router'
import { getCMA } from '@/api/product'
import IntrCalcItem from './item/IntrCalcItem.vue'
import ProductCommentItem from './item/ProductCommentItem.vue'
const store = useProductStore()
const { RBJ } = storeToRefs(store)
console.log(RBJ.value)
const route = useRoute()
const product = ref({})
const memo = ref({
  RP형: [],
  종금형: [],
  발행어음형: []
})
const commentList = ref([])
const loaded = ref(false)
function getContent() {
  getCMA(route.params.id).then((response) => {
    product.value = response.data.product
    const lines = response.data.product.memo.split('\n')
    let currentKey = null
    for (const line of lines) {
      if (memo.value.hasOwnProperty(line)) {
        currentKey = line
      } else if (currentKey) {
        memo.value[currentKey].push(line)
      }
    }
    commentList.value = response.data.comments.map((comment) => ({
      ...comment,
      modifyState: false
    }))
    loaded.value = true
  })
}
getContent()
const retByRBJ = computed(() => {
  //라디오버튼에 없는 유형이 store에 있을 시 수익률 0으로 계산합니다
  return product.value.hasOwnProperty(RBJ.value) ? Number(product.value[RBJ.value]) : 0
})
</script>
<style scoped lang="scss">
.cmatype-col {
  width: 15%;
  border-right: 1px solid $grey-light;
}
</style>
