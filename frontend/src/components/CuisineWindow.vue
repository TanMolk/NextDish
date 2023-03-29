<!--
 @Name CuisineWindow
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
      <ul
          style="padding: 0;"
          v-loading="listLoadingState"
          v-infinite-scroll="loadMoreData"
          :infinite-scroll-disabled="listNoMoreState"
      >
        <li
            style="list-style: none;"
            v-for="item in items"
            v-show="infoShowState"
        >
          <CuisineListItem
              style="margin-top: 10px"
              :info="item"
              @click="changeInfoWindow('detail',item)"
          />
        </li>
        <li
            style="margin-top: 10px"
            v-show="listNoMoreState"
        >
          <span style="font-weight: bolder;">No more</span>
        </li>
      </ul>
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
import CuisineListItem from "@/components/CuisineListItem.vue";
import CuisineDetail from "@/components/CuisineDetail.vue";

export default {
  name: "CuisineWindow",
  components: {CuisineDetail, CuisineListItem},
  props: {
    //list loading state
    listLoadingState: Boolean,
    listNoMoreState: Boolean,
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
    },
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
    },
    loadMoreData() {
      this.$emit("load-more-data");
    }
  }
}
</script>

<style scoped>
.cuisine-detail {
  width: var(--restaurants-list-detail-window-width);
}

.li-wrapper {
  overflow: auto;

  position: absolute;
  height: var(--restaurants-list-detail-window-height);
  width: var(--restaurants-list-detail-window-width);
  left: var(--restaurants-list-detail-window-left);
  bottom: 0;

  background-color: #9faab8;
  border-radius: 2em 2em 0 0;

  text-align: center;
}

.header {
  line-height: 5vh;
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
</style>