<template>
  <router-view />
  <Toast />
  <ConfirmDialog />
</template>

<script>
import Toast from './components/Toast.vue'
import ConfirmDialog from './components/ConfirmDialog.vue'

export default {
  name: 'App',
  components: {
    Toast,
    ConfirmDialog
  }
}
</script>
