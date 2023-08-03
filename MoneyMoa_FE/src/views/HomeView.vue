<template>
  <div>
    <v-card :elevation="0">
      <v-img height="500" :src="landing" class="text-white" cover>
        <v-container class="fill-height">
          <v-row>
            <v-spacer></v-spacer>
            <v-col align="right">
              <h1>돈 모으고 싶을 때</h1>
              <h1>머니모아</h1>
            </v-col>
          </v-row>
        </v-container>
      </v-img>
    </v-card>
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
import landing from '../assets/img/micheile-henderson-f030K9IzpcM-unsplash.jpg'
// const landing = './src/assets/img/micheile-henderson-f030K9IzpcM-unsplash.jpg'
const moamoa = './src/assets/img/money_moa_moa.gif'
</script>
