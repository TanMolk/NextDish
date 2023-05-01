<script setup>
import UserModal from "@/components/UserModal.vue";
</script>

<template>
  <RouterView></RouterView>
  <UserModal
      ref="userDialog"
  />
</template>

<script>


import UserUtil from "@/utils/UserUtil";

export default {
  mounted() {
    UserUtil.setUserDialog(this.$refs.userDialog);
  }
}
</script>

<style scoped>
</style>