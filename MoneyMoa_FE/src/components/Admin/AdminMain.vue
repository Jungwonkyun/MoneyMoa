<template>
  <v-container class="text-center">
    <v-row>
      <v-col align="center">
        <v-card title="유저 조회" class="text-center" color="#D3D3D3" width="300">
          <router-link :to="{ name: 'adminusersload' }">
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
