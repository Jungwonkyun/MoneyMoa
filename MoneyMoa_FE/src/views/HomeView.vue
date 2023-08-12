<template>
  <div>
    <v-carousel cycle hide-delimiter-background show-arrows="hover" height="720">
      <v-carousel-item v-for="(slide, i) in slides" :key="i">
        <div class="custom-slide">
          <v-img v-if="i === 0" :src="slide" height="720" class="main"></v-img>
          <v-img v-if="i === 1" :src="slide" height="720" cover class="text-white"
            ><v-container class="fill-height">
              <v-row>
                <v-spacer></v-spacer>
                <v-col align="right">
                  <h1>돈 모으고 싶을 때,</h1>
                  <h1>머니모아</h1>
                </v-col>
              </v-row>
            </v-container></v-img
          >
        </div>
      </v-carousel-item>
    </v-carousel>

    <v-container>
      <v-row justify="space-around">
        <v-col align="center">
          <v-img :src="moamoa"></v-img>
          <h3>내 돈 굴리기</h3>
          예적금, CMA상품을 비교하고 <br />
          최적의 금리를 저장할 수 있어요.
        </v-col>
        <v-col align="center">
          <v-img :src="moamoa"></v-img>
          <h3>챌린지</h3>
          목표 금액과 기간을 설정하고 피드를 통해 <br />진행상황을 공유할 수 있어요.
        </v-col>
        <v-col align="center">
          <v-img :src="moamoa"></v-img>
          <h3>금융사전</h3>
          어려운 금융용어를 바로바로 검색하고 <br />쉽게 풀어쓴 해설을 읽어볼 수 있어요.
        </v-col>
      </v-row>
      <v-row justify="center">
        <v-btn>시작하기</v-btn>
      </v-row>
    </v-container>
  </div>
</template>

<script setup>
// carousel item
import MomenyMoa_main from '@/assets/img/MoneyMoa_main.gif'
import landing from '@/assets/img/micheile-henderson-f030K9IzpcM-unsplash.jpg'

const slides = [MomenyMoa_main, landing]

// const moamoa = './src/assets/img/money_moa_moa.gif'
</script>
<style scoped lang="scss">
h1 {
  font-size: 36px;
}

.main {
  background-color: #eff5ff;
}
</style>
