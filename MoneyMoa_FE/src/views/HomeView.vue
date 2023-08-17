<template>
  <div>
    <v-carousel
      cycle
      hide-delimiter-background
      show-arrows="hover"
      height="670"
      interval="5000"
      class="animate__animated animate__fadeIn"
    >
      <v-carousel-item v-for="(slide, i) in slides" :key="i">
        <div class="custom-slide">
          <v-img v-if="i === 0" :src="slide" height="720" class="main"></v-img>
          <v-img v-if="i === 1" :src="slide" height="720" class="caro1"></v-img>
          <v-img v-if="i === 2" :src="slide" height="720" class="caro2"></v-img>
          <v-img v-if="i === 3" :src="slide" height="720" class="caro3"></v-img>
          <v-img v-if="i === 4" :src="slide" height="720" cover class="text-white"
            ><v-container class="fill-height">
              <v-row>
                <v-spacer></v-spacer>
                <v-col align="right" class="splash">
                  <h1>돈 모으고 싶을 때,</h1>
                  <h1>머니모아</h1>
                </v-col>
              </v-row>
            </v-container></v-img
          >
        </div>
      </v-carousel-item>
    </v-carousel>
  </div>
</template>

<script setup>
// carousel item
import MomenyMoa_main from '@/assets/img/MoneyMoa_main.gif'
import rollmoney from '@/assets/img/01_carousel_rollmoney.gif'
import challenge from '@/assets/img/02_carousel_challenge.gif'
import dictionary from '@/assets/img/03_carousel_dictionary.gif'
import landing from '@/assets/img/micheile-henderson-f030K9IzpcM-unsplash.jpg'
import 'animate.css'
const slides = [MomenyMoa_main, rollmoney, challenge, dictionary, landing]

// const moamoa = './src/assets/img/money_moa_moa.gif'
</script>
<style scoped lang="scss">
h1 {
  font-size: 36px;
}

.main {
  background-color: #eff5ff;
}
.caro1 {
  background-color: #deffb3;
}
.caro2 {
  background-color: #ffdef1;
}
.caro3 {
  background-color: #e9f8ff;
}
.splash {
  h1 {
    font-size: 3rem;
  }
}
.animate__animated.animate__fadeIn {
  --animate-duration: 2s;
}
</style>
