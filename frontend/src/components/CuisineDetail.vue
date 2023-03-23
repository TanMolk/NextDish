<!--
 @Name CuisineDetail
 @Description The detail window for restaurant
 @author Wei Tan
 @createDate 2023/03/13
-->
<template>
  <div>
    <nut-tabs
        v-model="selectedTab"
        :background="'#9faab8'"
    >
      <template v-slot:titles>
        <div
            class="nut-tabs__titles-item tab-class"
            v-for="item in items"
            :key="item"
            @click="selectedTab = item"
        >{{ item }}
        </div>
      </template>

      <nut-tab-pane
          :pane-key="items[0]"
      >
        <template v-slot>
          <div class="detail-pane">
            {{ detail?.name }}
            <br>
            <br>
            Distance: {{ directionDetail?.routes[0].legs[0].distance.value }}m
            <br>
            <br>
            <button
                @click="directionButtonClick"
            >Start Go
            </button>
          </div>
        </template>
      </nut-tab-pane>
      <nut-tab-pane
          :pane-key="items[1]"
      >
        <template v-slot>
          <div class="detail-pane">
            <!--            <div style="background-color: #43bbad;width: 140px;height: 140px;margin: 33px"></div>-->
          </div>
        </template>
      </nut-tab-pane>
      <nut-tab-pane
          :pane-key="items[2]"
      >
        <template v-slot>
          <div v-show="detail" class="detail-pane">
            <p v-for="item in detail?.opening_hours.weekday_text">
              {{ item }}
            </p>
          </div>
        </template>
      </nut-tab-pane>
      <nut-tab-pane
          :pane-key="items[3]"
      >
        <template v-slot>
          <div v-show="detail" class="detail-pane">
            <div v-for="review in detail?.reviews">
              <p>{{ review.author_name }}</p>
              <p>{{ review.rating }}</p>
              <p>{{ review.text }}</p>
            </div>
          </div>
        </template>
      </nut-tab-pane>
    </nut-tabs>
  </div>
</template>

<script>

import GoogleMapPlaceService from "@/service/GoogleMapPlaceService";
import StorageUtil from "@/utils/StorageUtil";
import Constants from "@/constants/Constants";

export default {
  name: "CuisineDetail",
  props: {
    placeId: String,
    mapInstance: Object,
  },
  data() {
    return {
      items: ['Location', 'Menu', 'Open Time', 'Reviews'],
      selectedTab: "Location",
      detail: null,
      directionDetail: null,
      //service of direction
      directionService: null,
    }
  },
  watch: {
    placeId() {
      this.getDetail();
    }
  },
  methods: {
    async getDetail() {
      if (this.placeId) {
        let resp = await GoogleMapPlaceService.getPlaceDetail(this.placeId);
        this.detail = resp.data.result;
        await this.getDirection();
      }
    },
    /**
     * Get information for direction, best used in the getDetail
     * @returns {Promise<void>}
     */
    async getDirection() {
      if (this.placeId) {
        //if it doesn't exist, initialize
        if (!this.directionService) {
          this.directionService = new this.mapInstance.api.DirectionsService();
        }

        //request direction
        let directionsService = this.directionService;
        let request = {
          origin: {
            location: {
              lat: Number(StorageUtil.get(Constants.STORAGE_USER_LOCATION_LATITUDE,)),
              lng: Number(StorageUtil.get(Constants.STORAGE_USER_LOCATION_LONGITUDE,))
            }
          },
          destination: {
            location: this.detail.geometry.location
          },
          travelMode: 'WALKING'
        };
        directionsService.route(request, (result, status) => {
          if (status === 'OK') {
            this.directionDetail = result;
          }
        });
      }
    },
    directionButtonClick() {
      this.$emit("direction-request", this.directionDetail);
    }
  }
}
</script>
<style scoped>

.tab-class {
  background-color: white;
  border-radius: 1em;

  margin: 5px 5px;
}

.detail-pane {
  height: 31vh;
}
</style>