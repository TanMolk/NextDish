<!--
 @Name CuisineList
 @Description The list/detail window
 @author Wei Tan
 @createDate 2023/03/12
-->
<template>
  <div
      class="li-wrapper"
  >
    <div class="header">
      <span class="title">more details</span>
      <button
          class="exit"
          @click="clickX()"
      >X
      </button>
    </div>

    <div class="list-wrapper">
      <InfiniteList
          v-show="infoShowState"
          :width="'100%'"
          :height="'100%'"
          :data="items"
          :itemSize="100"
          v-slot="{item,index}"
      >
        <CuisineListItem
            :info="item"
            @click="changeInfoWindow('detail',item)"
        />
      </InfiniteList>
      <CuisineDetail
          :map-instance="mapInstance"
          :place-id="placeIdForDetail"
          class="cuisine-detail"
          v-show="!infoShowState"
          @direction-request="this.$emit('direction-request',$event);"
      />
    </div>
  </div>
</template>

<script>
import InfiniteList from 'vue3-infinite-list';
import CuisineListItem from "@/components/CuisineListItem.vue";
import CuisineDetail from "@/components/CuisineDetail.vue";

export default {
  name: "CuisineList",
  components: {CuisineDetail, CuisineListItem, InfiniteList},
  props: {
    //all data of list
    items: Array,
    //data of detail
    placeId: String,
    mapInstance: Object,
  },
  watch: {
    //if prop placeId change, update to placeIdForDetail
    placeId(newVar) {
      this.placeIdForDetail = newVar;
    },
    placeIdForDetail(newVar) {
      //send update event to outside
      this.$emit("place-id-change", newVar)
    }
  },
  data() {
    return {
      //true: show list, false: show detail
      infoShowState: true,
      //store placeId for change
      placeIdForDetail: null
    }
  },
  methods: {
    /**
     * Event when the X button get clicked
     */
    clickX() {
      //send event to outside
      this.$emit("click-X")
    },
    /**
     * Change showing window
     * @param type detail or list
     * @param item data of showing detail
     */
    changeInfoWindow(type, item) {
      if (type === "detail") {
        this.infoShowState = false;
        this.placeIdForDetail = item.place_id;
      } else if (type === "list") {
        this.infoShowState = true;
      }
    }
  }
}
</script>

<style scoped>
.cuisine-detail {
  max-width: 100%;
}

.li-wrapper {
  display: grid;
  grid-template-rows: 10% 90%;


  position: absolute;
  width: 100vw;
  height: 50vh;
  left: calc(50% - 50vw);
  bottom: 0;

  background-color: #9faab8;
  border-radius: 2em 2em 0 0;

  text-align: center;
}

.header {
  grid-row: 1;

  line-height: 5vh;
}

.list-wrapper {
  grid-row: 2;
}

.title {
  margin: 20px auto;

  font-size: 1.5em;
  font-weight: bolder;
}

.exit {
  position: absolute;
  right: 5%;
  top: 2.5%;

  border: none;

  background-color: transparent;
}

@media screen and (min-width: 480px) {
  .li-wrapper {
    width: 480px;

    left: calc(50% - 240px);
  }
}
</style>