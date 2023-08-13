<template>
  <v-card-text>
    {{ comment.nickname }}: {{ comment.content }}
    <v-btn icon="mdi-delete" variant="text" @click="deleteComment(comment.id)"></v-btn>
    <v-btn icon="mdi-pencil" variant="text" @click="updateCondition = !updateCondition"></v-btn>
    <v-text-field
      v-if="updateCondition"
      class="px-5"
      v-model="editedCommentContent"
      label="댓글 수정..."
      variant="underlined"
      append-inner-icon="mdi-comment-check-outline"
      @click:append-inner="updateComment(comment.id)"
      @keyup.enter="updateComment(comment.id)"
    ></v-text-field>
  </v-card-text>
</template>
<script setup>
import { ref } from 'vue'
import { useRoute } from 'vue-router'
import challengeFeed from '@/api/challengeFeed.js'
import { useCookies } from 'vue3-cookies'

// comment 상속받음
const props = defineProps({
  comment: Object
})

// emit 선언
const emit = defineEmits()

// 상속 받은 comment 변수화
const comment = ref(props.comment)

// 수정할 댓글 모델
const editedCommentContent = ref('')

// 댓글 수정 필드 조건
const updateCondition = ref(false)

// 쿠키 사용
const { cookies } = useCookies()

// 멤버 정보 호출
const memberInfo = ref(cookies.get('member'))
const memberId = ref(memberInfo.value.id)

const route = useRoute()
const feedId = ref(route.params.feedId)

// 댓글 삭제
const deleteComment = async (commentId) => {
  try {
    await challengeFeed.deleteComment(commentId)
    // 댓글 목록 업데이트
    const response = await challengeFeed.fetchFeedDetail(feedId.value)
    emit('after-delete', response.data.comments)
  } catch (error) {
    console.error('에러 발생:', error)
  }
}

// 댓글 수정 및 댓글 목록 조회
const updateComment = async (commentId) => {
  try {
    const commentData = {
      content: editedCommentContent.value
    }

    // 댓글 수정
    const res = await challengeFeed.updateComment(commentId, commentData)

    // 수정 내용 업데이트
    comment.value.content = res.data.updateFeedComment.content

    // 입력 필드 초기화
    editedCommentContent.value = ''
  } catch (error) {
    console.error('에러 발생:', error)
  }
}
</script>
<style></style>
