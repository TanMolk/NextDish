<template>
  <div
      style="width: var(--restaurants-list-detail-window-width);margin: 0 auto;"
  >
    <div
        class="title"
    >
      <p>Newcastle Feed Your Stomach</p>
      <p
          style="overflow:hidden;text-overflow:ellipsis; white-space:nowrap; "
      >
        {{ placeName ? placeName : "" }}
      </p>
    </div>
    <CuisineDetail
        ref="cuisineDetail"
        :map-instance="null"
        :place-id="placeId"
        :share-model="true"
        @place-change="this.placeName=$event"
    />
  </div>
</template>

<script>
import CuisineDetail from "@/components/CuisineDetail.vue";

export default {
  name: "SharePage",
  components: {CuisineDetail},
  data() {
    return {
      placeId: this.$route.query.placeId + "",
      placeName: null
    }
  }
}
</script>

<style scoped>
.title {
  text-align: center;

  font-size: 1.5em;
  font-weight: bolder;
  font-style: italic;
}
</style>