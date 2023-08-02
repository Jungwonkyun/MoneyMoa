<template>
    <v-row v-for="(member, index) in members" :key="index">
      <v-col>
        {{ member }}
      </v-col>
    </v-row>
</template>
<script setup>
  import functions from '@/api/member.js'
  import { computed, onMounted, ref } from 'vue'
  import { useCookies } from 'vue3-cookies'

  const {cookies} = useCookies()
  // members null값 주고
  const members = ref(null)

  // admin 토큰값 받기위해 쿠키에서 멤버정보 가져오고
  const member = computed(() => {
    return cookies.get('member')
  })

  // 멤버들 불러오는 함수짜고
  async function loadAlluser(){
    try{
      members.value = await functions.getLoadAlluser(member.accessToken)
    }catch(err){
      console.log(err)
    }
  }

  // 마운트 되자마자 불러오게 함
  // 제발 돼라
  onMounted(() => {
    loadAlluser()
  })
</script>
<style>
    
</style>