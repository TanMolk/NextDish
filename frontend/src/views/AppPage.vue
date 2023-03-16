<template>
  <GoogleMap
      ref="mapInstance"
      class="map"
      :map-id="Constants.GOOGLE_MAP_ID"
      :api-key="Constants.GOOGLE_MAP_API_KEY"
      :center="userLocation"
      :zoom="17"
      :disable-default-ui="true"
  >
    <CustomMarker
        :options="userMarkerOption"
    >
      <img style="opacity: 0.7;width: 2em;height: 2em" src="@/assets/me.png" alt="me"/>
    </CustomMarker>
    <Circle
        :options="userCircleOption"
    />

    <Marker
        v-for="item in restaurantData.items"
        :options="{
          label: item.name,
          position: item.geometry.location,
          title: item.rating + '',
        }"
        @click="marketClick(item)"
    />
  </GoogleMap>
  <CuisineTypeSelect
      class="cuisine-select-wrapper"
      @option-change="selectOptionChange"
  />
  <CuisineList
      v-show="listShowState"
      :items="restaurantData.items"
      :detail="restaurantData.detail"
      class="app-cuisine-list"
      @click-X="this.listShowState = false"
  />
</template>

<script>
import {GoogleMap, Marker, CustomMarker, Circle} from 'vue3-google-map'
import {mileToMeter} from "@/utils/MileUtil";
import CuisineTypeSelect from "@/components/CuisineTypeSelect.vue";
import CuisineList from "@/components/CuisineList.vue";
import Constants from "@/constants/Constants";
import GoogleMapPlaceService from "@/service/GoogleMapPlaceService";

export default {
  name: "AppPage",
  computed: {
    Constants() {
      return Constants
    }
  },
  components: {CuisineList, CuisineTypeSelect, GoogleMap, Marker, CustomMarker, Circle},
  data() {
    return {
      mapInstance: null,
      userLocation: {
        lat: 55,
        lng: -1.6,
      },
      restaurantData: {
        items: [],
        detail: null
      },
      userMarkerOption: {
        position: this.userLocation,
        visible: this.ifInitialized
      },
      userCircleOption: {
        center: this.userLocation,
        radius: mileToMeter(1),
        strokeColor: '#d7dbdf',
        visible: this.ifInitialized
      },
      ifInitialized: false,
      listShowState: true,
      showingRestaurantMarker: "",
    }
  },
  methods: {
    freshUserMakersOptions() {
      this.userMarkerOption = {
        position: this.userLocation,
        visible: this.ifInitialized
      }

      this.userCircleOption = {
        center: this.userLocation,
        radius: mileToMeter(1),
        strokeColor: '#d7dbdf',
        visible: this.ifInitialized
      }
    },
    freshUserLocation() {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition((position) => {
          this.userLocation = {
            lat: position.coords.latitude,
            lng: position.coords.longitude
          }
          if (!this.ifInitialized) {
            this.loadRecentRestaurant("");
            this.ifInitialized = true;
            this.mapInstance = this.$refs.mapInstance;
            this.$loading({
              fullscreen: true
            }).close();
          }

          this.freshUserMakersOptions();
        });
      }
      return {lat: this.userLocation.lat, lng: this.userLocation.lng}
    },
    selectOptionChange(type) {
      this.loadRecentRestaurant(type);
    },
    async loadRecentRestaurant(type) {
      let response = await GoogleMapPlaceService.getRestaurantWithKeywordInOneMile(this.userLocation, type);
      this.restaurantData.items = response.data.results;
    },
    marketClick(item) {
      this.listShowState = true;
      this.mapInstance.map.panTo(item.geometry.location)
    },
  },
  created() {
    this.$loading({
      fullscreen: true
    });
    this.freshUserLocation();
  }
}
</script>

<style scoped>
.map {
  width: 100vw;
  height: 100vh;
  color: #d7dbdf;

}

.cuisine-select-wrapper {
  position: absolute;
  top: 5%;
  right: 5%;
}

.app-cuisine-list {
  /*max-height: 50%;*/
}
</style>