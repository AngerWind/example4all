<template>
  <div class="talk">
    <button @click="talkStore.getATalk" :disabled="talkStore.loading">
      {{ talkStore.loading ? '获取中...' : '获取一句土味情话' }}
    </button>
    <ul>
      <li v-for="talk in talkStore.talkList" :key="talk.id">{{ talk.title }}</li>
    </ul>
  </div>
</template>

<script setup lang="ts" name="LoveTalk">
  import {useTalkStore} from '../store/loveTalk'

  const talkStore = useTalkStore()

</script>

<style scoped>
</style>
