<template>
  <v-container>
    <v-row class="align-center my-2">
      <v-textarea
        v-model="commentContent"
        variant="outlined"
        prepend-icon="mdi-comment-processing-outline"
        rows="2"
        no-resize
        @keyup.enter="write($event)"
        hide-details
      ></v-textarea>
      <v-btn @click="write" class="ml-4">댓글 작성</v-btn>
    </v-row>
    <!-- 댓글목록 -->
    <v-sheet v-if="commentList.length">
      <template v-for="(item, index) in commentList" :key="index">
        <v-card variant="flat">
          <v-row>
            <v-col cols="3">
              <v-card-title class="comment-nickname">{{ item.nickname }}</v-card-title>
              <v-card-subtitle>{{ formatDate(item.createdAt) }}</v-card-subtitle>
            </v-col>
            <v-col cols="7">
              <v-card-text v-if="!item.modifyState">{{ item.content }}</v-card-text>
              <v-textarea
                v-else
                v-model="item.content"
                variant="outlined"
                rows="2"
                no-resize
                class="modiarea"
              ></v-textarea>
            </v-col>
            <v-col cols="2" class="pa-1" v-if="checkId(item.memberId)">
              <v-btn variant="text" @click="modifyCmt(item)">수정</v-btn>
              <v-btn variant="text" @click="deleteCmt(item.id)">삭제</v-btn>
            </v-col>
          </v-row>
          <v-divider />
        </v-card>
      </template>
    </v-sheet>
    <v-sheet v-else class="d-flex justify-center my-2"> 댓글이 없습니다. </v-sheet>
  </v-container>
</template>
<script setup>
import { ref } from 'vue'
import { useProductStore } from '@/stores/productStore'
import { useRoute } from 'vue-router'
import { storeToRefs } from 'pinia'
import { useCookies } from 'vue3-cookies'
import { writeComment, deleteComment, modifyComment } from '@/api/product'
const props = defineProps({
  commentList: Object
})
const emit = defineEmits(['comment-updated'])
const route = useRoute()
const store = useProductStore()
const { productType } = storeToRefs(store)
const { cookies } = useCookies()
const commentContent = ref('')

console.log('댓글:')
console.log(props.commentList)

function write(event) {
  if (event && event.shiftKey) {
    // Shift + Enter 키가 눌렸을 때 이벤트 중지
    event.preventDefault()
    return
  }
  let comment = {
    content: commentContent.value
  }
  let codeORid = productType.value === 'cma' ? route.params.id : route.params.productCode
  writeComment(productType.value, codeORid, comment).then((response) => {
    commentContent.value = ''
    emit('comment-updated')
  })
}
function deleteCmt(cmtId) {
  deleteComment(productType.value, cmtId).then((response) => {
    emit('comment-updated')
  })
}
function modifyCmt(cmt) {
  if (!cmt.modifyState) {
    cmt.modifyState = true
  } else {
    cmt.modifyState = false
    let comment = {
      content: cmt.content
    }
    modifyComment(productType.value, cmt.id, comment).then((response) => {
      emit('comment-updated')
    })
  }
}
function formatDate(input) {
  const date = new Date(input)
  const year = date.getFullYear().toString().slice(-2)
  const month = (date.getMonth() + 1).toString().padStart(2, '0')
  const day = date.getDate().toString().padStart(2, '0')
  return `${year}/${month}/${day}`
}
function checkId(cmtWriterId) {
  if (!cookies.get('member')) return false
  let loginId = cookies.get('member').id
  return loginId === cmtWriterId
}
</script>
<style>
.modiarea {
  width: 100%;
}
.comment-nickname {
  font-size: 16px;
  font-weight: bold;
}
</style>
