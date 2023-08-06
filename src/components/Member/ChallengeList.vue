<template>
  <v-container>
    <h1 align="center" class="mb-3">챌린지</h1>
    <v-card v-for="challenge in challenges" :key="challenge.id" class="mb-6" :elevation="10">
      <v-row class="d-flex justify-center align-center">
        <v-col cols="12" sm="4" class="text-center">
          <v-img cover :src="challenge.img" height="200"></v-img>
        </v-col>
        <v-col cols="12" sm="8">
          <v-row>
            <v-col cols="12">
              <v-card-title class="text-center">{{ challenge.title }}</v-card-title>
            </v-col>
            <v-col cols="12">
              <v-card-text class="text-center">{{ challenge.content }}{{ memberId }}</v-card-text>
            </v-col>
            <v-col cols="12">
              <v-card-text class="text-center">{{ challenge.hashtag }}</v-card-text>
            </v-col>
            <v-col cols="12">
              <v-card-text class="text-center">{{ challenge.period }}</v-card-text>
            </v-col>
            <v-col cols="12">
              <v-card-text class="text-center">{{ challenge.total_amount }}</v-card-text>
            </v-col>
          </v-row>
          <v-row class="d-flex justify-center">
            <v-col cols="6">
              <v-btn block>입장하기</v-btn>
            </v-col>
          </v-row>
          <div style="height: 20px"></div>
        </v-col>
      </v-row>
    </v-card>
    <v-row class="justify-end pa-3">
      <v-row justify="center">
        <v-dialog v-model="dialog" persistent width="auto">
          <template v-slot:activator="{ props }">
            <v-btn color="primary" v-bind="props"> Open Dialog </v-btn>
          </template>
          <v-card>
            <v-card-title class="text-center mt-4">
              <span class="text-h5">챌린지 생성하기</span>
            </v-card-title>
            <v-card-text>
              <v-container>
                <v-row>
                  <v-col cols="12" sm="6" md="4">
                    <v-text-field label="Legal first name*" required></v-text-field>
                  </v-col>
                  <v-col cols="12" sm="6" md="4">
                    <v-text-field
                      label="Legal middle name"
                      hint="example of helper text only on focus"
                    ></v-text-field>
                  </v-col>
                  <v-col cols="12" sm="6" md="4">
                    <v-text-field
                      label="Legal last name*"
                      hint="example of persistent helper text"
                      persistent-hint
                      required
                    ></v-text-field>
                  </v-col>
                  <v-col cols="12">
                    <v-text-field label="Email*" required></v-text-field>
                  </v-col>
                  <v-col cols="12">
                    <v-text-field label="Password*" type="password" required></v-text-field>
                  </v-col>
                  <v-col cols="12" sm="6">
                    <v-select
                      :items="['0-17', '18-29', '30-54', '54+']"
                      label="Age*"
                      required
                    ></v-select>
                  </v-col>
                  <v-col cols="12" sm="6">
                    <v-autocomplete
                      :items="[
                        'Skiing',
                        'Ice hockey',
                        'Soccer',
                        'Basketball',
                        'Hockey',
                        'Reading',
                        'Writing',
                        'Coding',
                        'Basejump'
                      ]"
                      label="Interests"
                      multiple
                    ></v-autocomplete>
                  </v-col>
                </v-row>
              </v-container>
              <small>*indicates required field</small>
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue-darken-1" variant="text" @click="dialog = false"> Close </v-btn>
              <v-btn color="blue-darken-1" variant="text" @click="dialog = false"> Save </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-row>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import functions from '@/api/member.js'

const dummy = [
  {
    id: 1,
    title: '운동 안 갈 때 마다 돈 넣기',
    content: '제곧내',
    img: 'https://cdn.vuetifyjs.com/images/cards/cooking.png',
    hashtag: '운동 좀 해라',
    period: 'nn일 남음',
    total_amount: '80 % 달성!'
  },
  {
    id: 1,
    title: '운동 안 갈 때 마다 돈 넣기',
    content: '제곧내',
    img: 'https://cdn.vuetifyjs.com/images/cards/cooking.png',
    hashtag: '운동 좀 해라',
    period: 'nn일 남음',
    total_amount: '80 % 달성!'
  },
  {
    id: 1,
    title: '운동 안 갈 때 마다 돈 넣기',
    content: '제곧내',
    img: 'https://cdn.vuetifyjs.com/images/cards/cooking.png',
    hashtag: '운동 좀 해라',
    period: 'nn일 남음',
    total_amount: '80 % 달성!'
  }
]
const challenges = ref(dummy)

const route = useRoute()
const memberId = ref(route.params.id)

const dialog = ref(false)

//// 마운트 시에 챌린지 리스트 API 호출, memberId가 변경되면 다시 호출
onMounted(() => {
  const res = functions.fetchChallengeList(memberId.value)
})
watch(dialog, (newVal, oldVal) => {
  console.log(newVal, oldVal)
})
</script>
<style>
.text-center {
  text-align: center;
}
</style>
