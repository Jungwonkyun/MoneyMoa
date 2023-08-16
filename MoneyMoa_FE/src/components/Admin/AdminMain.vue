<template>
  <v-container class="text-center">
    <h1>메~인</h1>
    <v-row>
      <v-col cols="3">
        <v-card title="유저 조회" class="text-center" color="#D3D3D3">
          <router-link :to="{ name: 'adminusersload' }">
            <v-card-actions style="display: inline-block">
              <v-btn class="mx-0">이동하기</v-btn>
            </v-card-actions>
          </router-link>
        </v-card>
      </v-col>
      <v-col cols="3">
        <v-card title="피드 조회" class="text-center" color="#D3D3D3">
          <router-link :to="{ name: 'admin' }">
            <v-card-actions style="display: inline-block">
              <v-btn class="mx-0">이동하기</v-btn>
            </v-card-actions>
          </router-link>
        </v-card>
      </v-col>
      <v-col cols="3">
        <v-card title="채팅방 조회" class="text-center" color="#D3D3D3">
          <router-link :to="{ name: 'admin' }">
            <v-card-actions style="display: inline-block">
              <v-btn class="mx-0">이동하기</v-btn>
            </v-card-actions>
          </router-link>
        </v-card>
      </v-col>
      <v-col cols="3">
        <v-card title="상품 조회" class="text-center" color="#D3D3D3">
          <router-link :to="{ name: 'admin' }">
            <v-card-actions style="display: inline-block">
              <v-btn class="mx-0">이동하기</v-btn>
            </v-card-actions>
          </router-link>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>
<script setup></script>
<style></style>
